import { Component, ChangeDetectorRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup, FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { EligibilityService } from '../../../user/apply-loan/services/eligibility';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DocumentType } from '../../types/document-type';

@Component({
  selector: 'app-loan-stepper2',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule,
    RouterModule,
    FormsModule
],
  templateUrl: './loan-stepper2.html',
  styleUrls: ['./loan-stepper2.css'],
})
export class LoanStepper2 implements  AfterViewInit, OnInit {
  basicForm!: FormGroup;
  personalForm!: FormGroup;
  employmentForm!: FormGroup;

  applicationId = 0;
  // resumeMode = false;


  uploadedDocs: Record<DocumentType, boolean> = {
    AADHAAR: false,
    PAN: false,
    SALARY_SLIP: false,
    BANK_STATEMENT: false,
  };

  uploadedFiles: Partial<Record<DocumentType, any>> = {};
  allDocsUploaded = false;

  private API = 'http://localhost:8080/api/user';
   @ViewChild('stepper') stepper!: MatStepper;

  constructor(
    private fb: FormBuilder,
    private eligibilityService: EligibilityService,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
    private route: ActivatedRoute
  ) {
    this.initializeForms();
  }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  ngAfterViewInit(): void {
  this.route.queryParams.subscribe(params => {
    const stepIndex = Number(params['step']);

    if (!isNaN(stepIndex)) {
      console.log('Resuming application at step:', stepIndex);

      // this.resumeMode = true;        // 🔓 disable linear temporarily

      setTimeout(() => {
        this.stepper.selectedIndex = stepIndex;
        this.cdr.detectChanges();
      });
    }
  });
}


  /* ================= FORM INIT ================= */

  private initializeForms(): void {
    this.basicForm = this.fb.group({
      loanType: ['PERSONAL', Validators.required],
      requestedAmount: [10000, Validators.required],
      tenureMonths: [12, Validators.required],
    });

    this.personalForm = this.fb.group({
      fullName: [''],
      email: [''],
      mobileNumber: [''],
      dateOfBirth: [''],
      gender: [''],
      address: [''],
      city: [''],
      state: [''],
      pincode: [''],
    });

    this.employmentForm = this.fb.group({
      employmentType: ['', Validators.required],
      monthlyIncome: ['', Validators.required],
      companyName: ['', Validators.required],
      panNumber: ['', Validators.required],
      aadhaarNumber: ['', Validators.required],
      bankAccount: ['', Validators.required],
      ifscCode: ['', Validators.required],
    });
  }

  /* ================= PROFILE AUTO-FILL ================= */

  private loadUserProfile(): void {
    const token = localStorage.getItem('auth_token');

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http.get<any>(`${this.API}/viewProfile`).subscribe({
      next: (profile) => {
        this.personalForm.patchValue({
          fullName: profile.fullName,
          email: profile.email,
          mobileNumber: profile.mobileNumber,
          dateOfBirth: new Date(profile.dateOfBirth),
          gender: profile.gender,
          address: profile.address,
          city: profile.city,
          state: profile.state,
          pincode: profile.pincode,
        });

        /** 🔒 LOCK PERSONAL DETAILS */
        // this.personalForm.disable();

        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Failed to load profile data', 'Close', {
          duration: 4000,
        });
      },
    });
  }

  /* ================= ELIGIBILITY ================= */

  checkEligibility(stepper: any): void {
    const payload = {
      ...this.basicForm.value,
      ...this.personalForm.value,
      ...this.employmentForm.value,
    };

    this.eligibilityService.checkEligibility(payload).subscribe({
      next: (res) => {
        this.applicationId = res.applicationId;

        if (res.finalEligibility) {
          stepper.next();
        } else {
          this.router.navigate(['user/not-eligible']);
        }
      },
      error: () => {
        this.snackBar.open('Eligibility check failed', 'Close', {
          duration: 4000,
        });
      },
    });
  }

  /* ================= DOCUMENT UPLOAD ================= */

  uploadDocument(event: Event, docType: DocumentType): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const formData = new FormData();
    formData.append('file', input.files[0]);
    formData.append('documentType', docType);
    formData.append('loanApplicationId', this.applicationId.toString());

    this.eligibilityService.uploadDocument(formData).subscribe({
      next: (res) => {
        this.uploadedDocs[docType] = true;
        this.uploadedFiles[docType] = res;

        this.allDocsUploaded =
          this.uploadedDocs.AADHAAR &&
          this.uploadedDocs.PAN &&
          this.uploadedDocs.SALARY_SLIP &&
          this.uploadedDocs.BANK_STATEMENT;

        this.cdr.detectChanges();
        input.value = '';
      },
      error: () => {
        this.uploadedDocs[docType] = false;
        alert(`Failed to upload ${docType}`);
      },
    });
  }

  /* ================= FINAL SUBMIT ================= */

  submit(): void {

    const payload = {
      ...this.basicForm.value,
      ...this.personalForm.value,
      ...this.employmentForm.value,
      ...this.uploadedFiles,
    };
    if (!this.applicationId) {
      this.snackBar.open('Invalid application. Please restart the process.', 'Close', {
        duration: 4000,
      });
      return;
    }

    const token = localStorage.getItem('auth_token');

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http
      .put(
        `http://localhost:8080/api/loan-applications/submit/${this.applicationId}`,
        { headers }
      )
      .subscribe({
        next: () => {
          this.snackBar.open('Loan Application Submitted Successfully 🎉', 'Close', {
            duration: 5000,
            verticalPosition: 'top',
          });

          this.router.navigate(['user/dashboard']);
        },
        error: (err) => {
          console.error('Submit application error:', err);

          this.snackBar.open(err?.error?.message || 'Failed to submit loan application', 'Close', {
            duration: 5000,
          });
        },
      });
  }
}
