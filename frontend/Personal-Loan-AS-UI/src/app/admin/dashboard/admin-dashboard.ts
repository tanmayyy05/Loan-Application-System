import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AdminDashboardService } from '../services/admin-api';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);


interface LoanApplication {
  applicationId: number;
  applicantName: string | null;
  loanType: string;
  requestedAmount: number;
  tenureMonths: number;
  calculatedEmi: number;
  applicationStatus: string;
  appliedAt: string;
  finalEligibility: boolean;
  userId: number;
}

interface LoanApiResponse {
  content: LoanApplication[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

@Component({
  standalone: true,
  selector: 'app-admin-dashboard',
  imports: [CommonModule, RouterLink, HttpClientModule, FormsModule],
  templateUrl: './admin-dashboard.html',
})
export class AdminDashboard implements OnInit {
// hardcoded stats for demo
  activities = [ { text: 'Loan PL-2031 approved by Loan Officer', time: '10 mins ago', }, { text: 'Documents verified for PL-2030', time: '1 hour ago', }, { text: 'Disbursement completed for PL-2028', time: 'Yesterday', }, ];
  stats: any[] = [];
  
  applications: LoanApplication[] = [];
  isLoading = false;

  // Status dropdown
  statuses = [
    'ELIGIBLE',
    'NOT_ELIGIBLE',
    'DOCUMENT_VERIFICATION_PENDING',
    'DOCUMENT_RETURNED_FOR_CORRECTION',
    'DOCUMENT_REJECTED',
    'DOCUMENT_APPROVED',
    'LOAN_APPROVED',
    'LOAN_REJECTED',
    'SANCTION_LETTER_SENT',
    'SANCTION_LETTER_ACCEPTED',
    'SANCTION_LETTER_REJECTED',
    'LOAN_DISBURSED',
  ];

  selectedStatus = 'DOCUMENT_VERIFICATION_PENDING';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private dashboardService: AdminDashboardService,
  ) { }

  ngOnInit(): void {
    this.loadApplications();
    this.loadDashboard();
  }

  loadApplications(): void {
    this.isLoading = true;

    this.http
      .get<LoanApiResponse>(
        `http://localhost:8080/api/loan-applications?status=${this.selectedStatus}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        }
      )
      .subscribe({
        next: (res) => {
          this.applications = res.content;
          this.isLoading = false;

          // ✅ force UI refresh (safe)
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to load applications', err);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
  }

//   loadDashboard(): void {
//   this.dashboardService.getDashboardStats().subscribe({
//     next: (res) => {
//       this.stats = [
//         {
//           label: 'Total Users',
//           value: res.totalUsers,
//           sub: 'All users',
//           color: 'text-indigo-600'
//         },
//         {
//           label: 'Total Applications',
//           value: res.totalApplications,
//           sub: 'All time',
//           color: 'text-blue-600'
//         },
//         {
//           label: 'Pending Approvals',
//           value: res.pendingLoanApprovals,
//           sub: 'Action required',
//           color: 'text-red-600',
//           link: '/admin/applications'
//         },
//         {
//           label: 'Amount Disbursed',
//           value: `₹${res.totalAmountDisbursed.toLocaleString('en-IN')}`,
//           sub: 'Total',
//           color: 'text-green-600'
//         }
//       ];

//       // ✅ MUST be here
//       this.cdr.detectChanges();
//     },
//     error: (err) => {
//       console.error('Dashboard API failed', err);
//     }
//   });
// }
loadDashboard(): void {
  this.dashboardService.getDashboardStats().subscribe({
    next: (res) => {
      this.stats = [
        { label: 'Total Users', value: res.totalUsers, color: 'text-indigo-600' },
        { label: 'Total Applications', value: res.totalApplications, color: 'text-blue-600' },
        { label: 'Pending Approvals', value: res.pendingLoanApprovals, color: 'text-red-600' },
        { label: 'Amount Disbursed', value: res.totalAmountDisbursed, color: 'text-green-600' }
      ];

      this.renderPieChart(res);
      this.cdr.markForCheck();
    }
  });
}

renderPieChart(res: any): void {
  const ctx = document.getElementById('dashboardPieChart') as HTMLCanvasElement;

  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: [
        'Total Users',
        'Total Applications',
        'Pending Approvals',
        //  'Amount Disbursed'
      ],
      datasets: [
        {
          data: [
            res.totalUsers,
            res.totalApplications,
            res.pendingLoanApprovals,
            //  res.totalAmountDisbursed
          ],
          backgroundColor: [
            '#6366F1', // indigo
            '#ffff04', // blue
            '#EF4444', // red
            '#22C55E'  // green
          ]
        }
      ]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom'
        }
      }
    }
  });
}



  onStatusChange(): void {
    this.loadApplications();
  }

  trackByApplicationId(index: number, app: LoanApplication): number {
    return app.applicationId;
  }
}
