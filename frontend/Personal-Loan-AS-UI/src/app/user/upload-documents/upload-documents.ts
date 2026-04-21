import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

type DocumentStatus = 'NOT_UPLOADED' | 'UPLOADED' | 'APPROVED' | 'REJECTED';

@Component({
  selector: 'app-upload-documents',
  imports: [CommonModule],
  templateUrl: './upload-documents.html',
})
export class UploadDocuments {
  loanId!: string;

  documents = [
    { name: 'Aadhaar Card', status: 'APPROVED' as DocumentStatus },
    { name: 'PAN Card', status: 'UPLOADED' as DocumentStatus },
    { name: 'Salary Slip (Last 3 months)', status: 'NOT_UPLOADED' as DocumentStatus },
    { name: 'Bank Statement', status: 'REJECTED' as DocumentStatus },
  ];

  constructor(private route: ActivatedRoute) {
    this.loanId = this.route.snapshot.paramMap.get('id')!;
  }

  onFileSelect(event: any, doc: any) {
    const file = event.target.files[0];
    if (file) {
      doc.status = 'UPLOADED';
    }
  }
}
