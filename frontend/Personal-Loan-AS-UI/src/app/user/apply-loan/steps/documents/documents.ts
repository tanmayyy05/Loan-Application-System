import { Component } from '@angular/core';
import { FileUploadModule } from 'primeng/fileupload';
import { ApplyLoanStateService } from '../../services/apply-loan-state';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [FileUploadModule],
  templateUrl: './documents.html',
})
export class Documents {
upload() {
throw new Error('Method not implemented.');
}
  constructor(private state: ApplyLoanStateService, private router: Router) {}

  next() {
    this.router.navigate(['/user/apply-loan/review']);
  }
}
