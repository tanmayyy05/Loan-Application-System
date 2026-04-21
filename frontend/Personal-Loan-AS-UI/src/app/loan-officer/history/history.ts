import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-loan-officer-history',
  imports: [CommonModule],
  templateUrl: './history.html',
})
export class History {
  history = [
    {
      applicationId: 'PL-1023',
      action: 'APPROVED',
      officer: 'Officer A',
      remarks: 'Income verified',
      date: '2025-01-12',
    },
    {
      applicationId: 'PL-1024',
      action: 'REJECTED',
      officer: 'Officer B',
      remarks: 'Low credit score',
      date: '2025-01-11',
    },
    {
      applicationId: 'PL-1025',
      action: 'APPROVED',
      officer: 'Officer A',
      remarks: 'Income verified',
      date: '2025-01-12',
    },
    {
      applicationId: 'PL-1026',
      action: 'REJECTED',
      officer: 'Officer B',
      remarks: 'Low credit score',
      date: '2025-01-11',
    },
  ];
}
