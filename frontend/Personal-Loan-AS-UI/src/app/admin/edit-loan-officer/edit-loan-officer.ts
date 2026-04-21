import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { LoanOfficer } from '../../core/models/loan-officer.model';
import { LoanOfficerService } from '../../core/services/loan-officer';


@Component({
  standalone: true,
  selector: 'app-loan-officer-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './edit-loan-officer.html',
})
export class EditLoanOfficer {
  form: FormGroup;
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    private service: LoanOfficerService,
    private dialogRef: MatDialogRef<EditLoanOfficer>,
    @Inject(MAT_DIALOG_DATA) public data: LoanOfficer | null
  ) {
    this.isEdit = !!data?.id;

    this.form = this.fb.group({
      fullName: [data?.fullName ?? '', Validators.required],
      email: [data?.email ?? '', [Validators.required, Validators.email]],
      mobileNumber: [data?.mobileNumber ?? '', Validators.required],
      dateOfBirth: [data?.dateOfBirth ? new Date(data.dateOfBirth) : null, Validators.required],
      gender: [data?.gender ?? '', Validators.required],
      age: [data?.age ?? null],
      address: [data?.address ?? '', Validators.required],
      city: [data?.city ?? '', Validators.required],
      state: [data?.state ?? '', Validators.required],
      pincode: [data?.pincode ?? '', Validators.required],
    });
  }

  save() {
    if (this.form.invalid) return;

    const value = this.form.value;

    const payload: LoanOfficer = {
      ...this.data,
      ...value,
      dateOfBirth: value.dateOfBirth ? value.dateOfBirth.toISOString().split('T')[0] : '',
    };

    if (this.isEdit && this.data?.id) {
      // 🔹 PUT API
      this.service.updateProfile(this.data.id, payload).subscribe({
        next: () => {
          console.log('UPDATED SUCCESS ✅');
          this.dialogRef.close(true);
        },
        error: (err) => console.error(err),
      });
    } else {
      // 🔹 POST API
      this.service.addProfile(payload).subscribe({
        next: () => {
          console.log('CREATED SUCCESS ✅');
          this.dialogRef.close(true);
        },
        error: (err) => console.error(err),
      });
    }
  }

  close() {
    this.dialogRef.close(false);
  }
}
