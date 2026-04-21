import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplyLoanStateService } from '../../services/apply-loan-state';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './basic-details.html',
})
export class BasicDetails {
  private fb = inject(FormBuilder);
  private state = inject(ApplyLoanStateService);
  private router = inject(Router);

  // form = this.fb.group({
  //   loanAmount: [null, Validators.required],
  //   tenure: [null, Validators.required],
  //   creditScore: [null, Validators.required],
  // });

  
  form = this.fb.group({
    loanType: ['', Validators.required],
    requestedAmount: ['', Validators.required],
    tenureMonths: ['', Validators.required],
  });

  // next() {
  //   if (this.form.valid && (this.form.value.creditScore ?? 0) >= 650) {
  //     this.state.set('basic', this.form.value);
  //     this.router.navigate(['../personal']);
  //   }
  // }

  next() {
    if (this.form.valid) {
      console.log('Basic details form valid:', this.form.value);
      this.state.set('basic', this.form.value);
      this.router.navigate(['../personal']);
    }
    console.log('Basic @@@@ bhar:', this.form.value);
  }
}
