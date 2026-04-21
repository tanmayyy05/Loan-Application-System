import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-decision-modal',
  imports: [CommonModule],
  templateUrl: './decision-modal.html',
})
export class DecisionModal {
  @Input() action!: 'APPROVE' | 'REJECT';
  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
}
