import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  imports: [CommonModule, HttpClientModule,
    MatIconModule , MatButtonModule],
  templateUrl: './sanction-letter-dialog-component.html',
  styleUrls: ['./sanction-letter-dialog-component.css'],
})
export class SanctionLetterDialogComponent implements OnInit {

  sanctionLetter: any;
  isLoading = true;
  actionInProgress = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { loanApplicationId: number },
    private http: HttpClient,
    private dialogRef: MatDialogRef<SanctionLetterDialogComponent>,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fetchSanctionLetter();
  }

  fetchSanctionLetter() {
    this.http.get(
      `http://localhost:8080/api/sanction-letter/${this.data.loanApplicationId}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }
    ).subscribe({
      next: (res) => {
        this.sanctionLetter = res;
        this.isLoading = false;

        // ✅ force UI refresh inside dialog
        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  accept() {
    this.actionInProgress = true;

    this.http.put(
      `http://localhost:8080/api/sanction-letter/${this.sanctionLetter.id}/accept`,
      {},
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }
    ).subscribe({
      next: () => {
        this.showToast('Sanction Letter accepted successfully');
        this.dialogRef.close(true);
      },
      error: () => {
        this.actionInProgress = false;
        this.cdr.detectChanges();
      }
    });
  }

  reject() {
    this.actionInProgress = true;

    this.http.put(
      `http://localhost:8080/api/sanction-letter/${this.sanctionLetter.id}/reject`,
      {},
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }
    ).subscribe({
      next: () => {
        this.showToast('Sanction Letter rejected successfully');
        this.dialogRef.close(false);
      },
      error: () => {
        this.actionInProgress = false;
        this.cdr.detectChanges();
      }
    });
  }

  showToast(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      verticalPosition: 'top',
      horizontalPosition: 'right',
      panelClass: ['success-snackbar']
    });
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
