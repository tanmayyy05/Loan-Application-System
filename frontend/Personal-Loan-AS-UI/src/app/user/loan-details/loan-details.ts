import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

/* ================= INTERFACES ================= */

interface Emi {
  id: number;
  emiNumber: number;
  dueDate: string;
  emiAmount: number;
  penaltyAmount: number;
  totalPayableAmount: number;
  emiStatus: string;
}

interface LoanAccount {
  id: number;
  applicationId: number;
  userId: number;
  loanAmount: number;
  interestRate: number;
  tenureMonths: number;
  emiAmount: number;
  outstandingBalance: number;
  loanAccountStatus: string;
  closureType?: string;
  disbursedDate: string;
  closureDate?: string;
}

@Component({
  selector: 'app-loan-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './loan-details.html',
  styleUrls: ['./loan-details.css'],
})
export class LoanDetails implements OnInit {
  loanAccountId!: number;

  loanAccount!: LoanAccount;
  emis: Emi[] = [];

  loading = true;
  error = '';

  private EMI_API = 'http://localhost:8080/api/emi';
  private LOAN_API = 'http://localhost:8080/api/loan-accounts';

  constructor(private route: ActivatedRoute, private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const param = this.route.snapshot.paramMap.get('loanAccountId');

    if (!param || isNaN(+param)) {
      this.error = 'Invalid loan account';
      this.loading = false;
      return;
    }

    this.loanAccountId = +param;

    // 🔥 Load both APIs
    this.fetchLoanAccount();
    this.fetchEmiDetails();
  }

  /* ================= LOAN ACCOUNT ================= */

  fetchLoanAccount(): void {
    this.http.get<LoanAccount>(`${this.LOAN_API}/${this.loanAccountId}`).subscribe({
      next: (data) => {
        this.loanAccount = data;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load loan account details';
      },
    });
  }

  /* ================= EMI DETAILS ================= */

  fetchEmiDetails(): void {
    this.http.get<Emi[]>(`${this.EMI_API}/getByLoanAccountId/${this.loanAccountId}`).subscribe({
      next: (data) => {
        this.emis = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load EMI details';
        this.loading = false;
      },
    });
  }

   payEmi(emi: any): void {

    const payload = {
      loanAccountId: null, // from loan summary
      emiId: emi.id,
      paidAmount: emi.totalPayableAmount,
      paymentMode: 'UPI',        // HARD CODED
      paymentType: 'EMI'         // HARD CODED
    };

    this.http.post(
      'http://localhost:8080/api/repayments/pay',
      payload
    ).subscribe({
      next: (res) => {
        console.log('Payment success', res);

        // Update UI immediately
        emi.emiStatus = 'PAID';

        // Optional: refresh loan account / EMI list
        // this.loadLoanAccount();
        // this.loadEmis();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        alert('Payment failed. Please try again.');
      }
    });
  }

}
