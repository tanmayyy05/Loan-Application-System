import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-system-rules',
  imports: [CommonModule, FormsModule],
  templateUrl: './system-rules.html',
})
export class SystemRules {
  rules = {
    maxLoanAmount: 1000000,
    minCreditScore: 650,
    maxTenure: 60,
    interestRate: 11.5,
  };

  save() {
    alert('System rules saved');
  }
}
