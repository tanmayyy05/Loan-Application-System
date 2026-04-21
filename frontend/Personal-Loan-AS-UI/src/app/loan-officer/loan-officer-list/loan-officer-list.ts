import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { LoanOfficer } from '../../core/models/loan-officer.model';
import { LoanOfficerDialogComponent } from '../loan-officer-dialog/loan-officer-dialog';

@Component({
  standalone: true,
  selector: 'app-loan-officer-list',
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './loan-officer-list.html'
})
export class LoanOfficerListComponent {

  columns = ['fullName', 'email', 'mobileNumber', 'gender', 'dateOfBirth', 'actions'];
  dataSource: LoanOfficer[] = [];

  constructor(private dialog: MatDialog) {
    this.loadLoanOfficers();
  }

  loadLoanOfficers() {
    // 🔹 GET API
    this.dataSource = [
      {
        id: 1,
        fullName: 'Rahul Sharma',
        email: 'rahul@bank.com',
        mobileNumber: '9876543210',
        gender: 'Male',
        age: 30,
        dateOfBirth: '1994-01-01',
        address: 'MG Road',
        city: 'Mumbai',
        state: 'MH',
        pincode: '400001'
      }
    ];
  }

  openDialog(data?: LoanOfficer) {
    const dialogRef = this.dialog.open(LoanOfficerDialogComponent, {
      width: '700px',
      data: data || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadLoanOfficers();
      }
    });
  }
}
