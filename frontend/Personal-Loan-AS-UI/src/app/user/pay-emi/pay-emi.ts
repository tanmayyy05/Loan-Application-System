import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-pay-emi',
  imports: [CommonModule],
  templateUrl: './pay-emi.html',
})
export class PayEmi {
  loanId!: string;

  emiSchedule = [
    { month: 'Jan 2025', amount: 14500, status: 'PAID' },
    { month: 'Feb 2025', amount: 14500, status: 'PAID' },
    { month: 'Mar 2025', amount: 14500, status: 'DUE' },
    { month: 'Apr 2025', amount: 14500, status: 'UPCOMING' },
  ];

  constructor(private route: ActivatedRoute) {
    this.loanId = this.route.snapshot.paramMap.get('id')!;
  }

  payEmi() {
    alert('Redirecting to payment gateway (later)');
  }
}
