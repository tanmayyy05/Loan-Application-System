import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-admin-audit-logs',
  imports: [CommonModule],
  templateUrl: './audit-logs.html',
})
export class AuditLogs {
  logs = [
    {
      user: 'Admin',
      action: 'Approved Loan',
      target: 'PL-2031',
      date: '2025-01-12 10:30',
    },
    {
      user: 'Officer A',
      action: 'Verified Documents',
      target: 'PL-2031',
      date: '2025-01-11 15:10',
    },
  ];
}
