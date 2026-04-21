import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';


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
  selector: 'app-admin-disbursement-list',
  imports: [CommonModule,FormsModule],
  templateUrl: './disbursement-list.html',
})
export class AdminDisbursementList {
  selectedId: number | null = null;
  applications: LoanApplication[] = [];
  isLoading = false;
  actionInProgress = false;

  

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

  selectedStatus = 'SANCTION_LETTER_ACCEPTED';


  
  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar,
  ) { }


   ngOnInit(): void {
    this.loadApplications();
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

  onStatusChange(): void {
    this.loadApplications();
  }

  trackByApplicationId(index: number, app: LoanApplication): number {
    return app.applicationId;
  }

  
  openConfirm(applicationId: number) {
  this.selectedId = applicationId;
}


  cancel() {
  this.selectedId = null;
}

confirm() {
  if (!this.selectedId) return;

  this.actionInProgress = true;

  this.http.post(
    `http://localhost:8080/api/loan-applications/${this.selectedId}/disburse`,
    {},
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    }
  ).subscribe({
    next: () => {
      this.showToast('Loan disbursed successfully');
      this.selectedId = null;
      this.actionInProgress = false;
      this.loadApplications();   // refresh table
      this.cdr.detectChanges();
    },
    error: () => {
      this.showToast('Failed to disburse loan');
      this.actionInProgress = false;
    }
  });
}

showToast(message: string) {
  this.snackBar.open(message, 'Close', {
    duration: 3000,
    verticalPosition: 'top',
    horizontalPosition: 'right'
  });
}

}
