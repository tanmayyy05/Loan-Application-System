import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UIApplicationStage } from '../../../core/models/ui-application-stage.model';

@Component({
  selector: 'app-application-timeline',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './application-timeline.html',
})
export class ApplicationTimeline {
  @Input() currentStage!: UIApplicationStage;

  stages: { key: UIApplicationStage; label: string }[] = [
    { key: 'CREATED', label: 'Created' },
    { key: 'SUBMITTED', label: 'Submitted' },
    { key: 'DOCUMENT_VERIFICATION', label: 'Document Verification' },
    { key: 'LOAN_APPROVAL', label: 'Loan Approval' },
    { key: 'DISBURSED', label: 'Disbursed' },
    { key: 'CLOSED', label: 'Closed' },
  ];

  isCompleted(stage: UIApplicationStage): boolean {
    return (
      this.stages.findIndex((s) => s.key === stage) <
      this.stages.findIndex((s) => s.key === this.currentStage)
    );
  }

  isActive(stage: UIApplicationStage): boolean {
    return stage === this.currentStage;
  }
}
