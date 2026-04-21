import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-loan-stepper',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './loan-stepper.html',
})
export class LoanStepper {
  steps = [
    { label: 'Basic', route: 'basic' },
    { label: 'Personal', route: 'personal' },
    { label: 'Employment', route: 'employment' },
    { label: 'Documents', route: 'documents' },
    { label: 'Review', route: 'review' },
  ];

  constructor(private router: Router) {}

  isActive(step: string): boolean {
    return this.router.url.includes(step);
  }
}
