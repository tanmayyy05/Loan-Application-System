import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplyLoanStateService } from '../../services/apply-loan-state';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './review-submit.html',
})
export class ReviewSubmit {
  constructor(public state: ApplyLoanStateService) {}

  submit() {
    console.log('FINAL PAYLOAD', this.state);
    alert('Loan Application Submitted!');
  }
}
