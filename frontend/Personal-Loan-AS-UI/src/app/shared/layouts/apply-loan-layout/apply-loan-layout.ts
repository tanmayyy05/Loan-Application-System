import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { StepperModule } from 'primeng/stepper';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apply-loan-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, StepperModule, ButtonModule],
  templateUrl: './apply-loan-layout.html',
})
export class ApplyLoanLayout {
  step = 1;

  constructor(private router: Router) {
    this.syncStepWithUrl();
  }

  syncStepWithUrl() {
    const url = this.router.url;

    if (url.includes('basic-details')) this.step = 1;
    else if (url.includes('personal-details')) this.step = 2;
    else if (url.includes('employment-details')) this.step = 3;
    else if (url.includes('document')) this.step = 4;
    else if (url.includes('review-submit')) this.step = 5;
  }

  go(step: number, route: string) {
    this.step = step;
    this.router.navigate(['/apply-loan', route]);
  }
}
