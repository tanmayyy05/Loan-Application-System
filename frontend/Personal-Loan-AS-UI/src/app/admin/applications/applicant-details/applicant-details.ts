import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface LoanApplication {
  applicationId: number;
  loanType: string;
  requestedAmount: number;
  tenureMonths: number;
  calculatedEmi: number;
  user: {
    fullName: string;
  };
  documents: BackendDocument[];
}

interface BackendDocument {
  documentId: number;
  documentType: string;
  documentStatus: string;
  remarks?: string;
}

interface UIDocument {
  documentId?: number;
  documentType: string;
  status: string;
  remarks?: string;
}

@Component({
  standalone: true,
  selector: 'app-applicant-details',
  imports: [CommonModule],
  templateUrl: './applicant-details.html',
})
export class ApplicantDetails implements OnInit {
  private readonly API = 'http://localhost:8080/api/loan-applications';

  loading = true;
  error: string | null = null;

  application: LoanApplication | null = null; // ✅ KEEP nullable
  documents: UIDocument[] = [];

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router,private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.error = 'Invalid application ID';
      this.loading = false;
      return;
    }

    this.http.get<LoanApplication>(`${this.API}/${id}`).subscribe({
      next: (res) => {
        console.log('API RESPONSE:', res); // ✅ keep
        this.application = res; // ✅ this IS happening
        this.buildDocuments(res.documents || []);
        this.loading = false; // ✅ important
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load application';
        this.loading = false;
      },
    });
  }

  private buildDocuments(docs: BackendDocument[]) {
    const REQUIRED = ['AADHAAR', 'PAN', 'SALARY_SLIP', 'BANK_STATEMENT'];

    this.documents = REQUIRED.map((type) => {
      const match = docs.find((d) => d.documentType === type);
      return {
        documentId: match?.documentId,
        documentType: type,
        status: match?.documentStatus || 'NOT_UPLOADED',
        remarks: match?.remarks,
      };
    });
  }

  viewDocument(doc: UIDocument) {
    if (!doc.documentId) return;

    this.http
      .get(`http://localhost:8080/api/documents/download/${doc.documentId}`, {
        responseType: 'blob',
      })
      .subscribe((blob) => {
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');
        this.cdr.detectChanges();
      });
  }

  approveApplication() {
    this.http
      .put(`${this.API}/approve`, {
        applicationId: this.application?.applicationId,
      })
      .subscribe(() => this.router.navigate(['/admin/applications']));
  }

  rejectApplication() {
    const reason = prompt('Enter rejection reason');
    if (!reason) return;

    this.http
      .put(`${this.API}/reject`, {
        applicationId: this.application?.applicationId,
        rejectionReason: reason,
      })
      .subscribe(() => this.router.navigate(['/admin/applications']));
  }

}
