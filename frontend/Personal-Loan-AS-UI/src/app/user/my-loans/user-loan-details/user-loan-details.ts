import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApplicationTimeline } from '../../../shared/components/application-timeline/application-timeline';
import { mapStatusToStage } from '../../../core/utils/application-status-mapper';
import { ApplicationInitStatus } from '../../../core/models/application-init-status.model';

@Component({
  standalone: true,
  selector: 'app-user-loan-details',
  templateUrl: './user-loan-details.html',
  imports: [CommonModule, RouterLink, ApplicationTimeline],
})
export class UserLoanDetails {
  loanId = '';

  currentStatus: ApplicationInitStatus = 'DOCUMENT_VERIFICATION_PENDING';
  currentStage = mapStatusToStage(this.currentStatus);

  documents = [
    { name: 'Aadhar Card', status: 'APPROVED' },
    { name: 'PAN Card', status: 'APPROVED' },
    { name: 'Salary Slip', status: 'PENDING' },
  ];

  constructor(private route: ActivatedRoute) {
    // ✅ SAFE & CORRECT
    this.loanId = this.route.snapshot.paramMap.get('id') ?? '';
  }

  // ✅ UI permissions derived from status
  get canUploadDocuments(): boolean {
    return (
      this.currentStatus === 'DOCUMENT_VERIFICATION_PENDING' ||
      this.currentStatus === 'DOCUMENT_RETURNED'
    );
  }

  get canPayEmi(): boolean {
    return this.currentStatus === 'LOAN_DISBURSED';
  }
}
