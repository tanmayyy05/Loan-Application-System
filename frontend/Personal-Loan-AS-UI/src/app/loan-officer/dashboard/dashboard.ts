import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { LoanOfficerDashboardService } from './loan-officer-dashboard.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-loan-officer-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html'
})
export class LoanOfficerDashboardComponent implements OnInit {

  stats: any[] = [];

  constructor(private dashboardService: LoanOfficerDashboardService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.dashboardService.getDashboardStats().subscribe(res => {
      this.stats = [
        {
          label: 'Total Applications',
          value: res.totalApplications,
          bar: 'bg-gradient-to-r from-indigo-500 to-indigo-600',
          glow: 'bg-indigo-500'
        },
        {
          label: 'Pending Verification',
          value: res.pendingVerification,
          bar: 'bg-gradient-to-r from-yellow-400 to-amber-500',
          glow: 'bg-yellow-400'
        },
        {
          label: 'Documents Returned',
          value: res.documentsReturned,
          bar: 'bg-gradient-to-r from-emerald-500 to-emerald-700',
          glow: 'bg-emerald-600'
        },
        {
          label: 'Documents Rejected',
          value: res.documentsRejected,
          bar: 'bg-gradient-to-r from-rose-500 to-red-600',
          glow: 'bg-red-500'
        }
      ];


      this.cdr.detectChanges();

    });
  }


  pendingTasks = [
    {
      id: 'PL-3011',
      applicant: 'Amit Shah',
      stage: 'DOCUMENT_VERIFICATION',
      daysPending: 2,
    },
    {
      id: 'PL-3014',
      applicant: 'Sneha Patel',
      stage: 'READY_FOR_REVIEW',
      daysPending: 1,
    },
  ];
}
