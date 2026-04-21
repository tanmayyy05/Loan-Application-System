import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { LoanOfficer } from '../../core/models/loan-officer.model';
import { EditLoanOfficer } from '../edit-loan-officer/edit-loan-officer';
import { HeaderComponent } from '../../shared/header/header';

@Component({
  standalone: true,
  selector: 'app-loan-officers',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatDialogModule],
  templateUrl: './loan-officers.html',
})
export class LoanOfficers implements OnInit {
  columns = ['fullName', 'email', 'mobileNumber', 'gender', 'dateOfBirth', 'actions'];
  dataSource: LoanOfficer[] = [];

  loading = false;
  error = '';

  private readonly API = 'http://localhost:8080/api/loanOfficer';

  constructor(
    private dialog: MatDialog,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadLoanOfficers();
  }

  /* ================= LOAD LOAN OFFICERS ================= */

  loadLoanOfficers(): void {
    this.loading = true;
    this.error = '';

    const token = localStorage.getItem('token');

    if (!token) {
      this.error = 'Unauthorized access';
      this.loading = false;
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http.get<LoanOfficer[]>(`${this.API}/viewAllProfiles`, { headers }).subscribe({
      next: (res) => {
        this.dataSource = res || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Loan Officer API error:', err);
        this.error = 'Failed to load loan officers';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  /* ================= EDIT / ADD ================= */

  openDialog(data?: LoanOfficer): void {
    const dialogRef = this.dialog.open(EditLoanOfficer, {
      width: '700px',
      data: data || null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadLoanOfficers();
      }
    });
  }
}
