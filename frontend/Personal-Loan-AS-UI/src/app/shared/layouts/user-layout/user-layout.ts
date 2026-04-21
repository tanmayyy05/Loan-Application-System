import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-user-layout',
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './user-layout.html',
  styleUrl: './user-layout.css',
})
export class UserLayout {
  userProfile: any;
  userInitial = '';
  userFullName = '';
  userLevel = '';
  avatarBgClass = '';


  private readonly API = 'http://localhost:8080/api/loan-applications';

  constructor(private http: HttpClient, private router: Router,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar) { }


  ngOnInit(): void {
    this.loadUserProfile();
  }
  /* ================= APPLY LOAN GATE ================= */
  applyLoan(): void {
    // if (this.checkingApply) return;

    // this.checkingApply = true;
    const token = localStorage.getItem('token');

    this.http
      .get<{ canApply: boolean; reason?: string }>(`${this.API}/can-apply`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (res) => {
          // this.checkingApply = false;

          if (res.canApply) {
            console.log('✅ Navigating to loan-stepper2', res.canApply);
            // // this.router.navigateByUrl('/user/loan-stepper2');
            // this.checkingApply= false;

            // this.router.navigate(['/user/loan-stepper2']);
            this.router.navigate(['/user/loan-stepper2']);
            //  this.router.navigateByUrl('/user/loan-stepper2');
            //  this.router.navigateByUrl('/user/dashboard');

          } else {
            this.snackBar.open(res.reason || 'You cannot apply for a loan right now', 'OK', {
              duration: 6000,
              verticalPosition: 'top',
            });
            this.router.navigateByUrl('/user/my-loans');
          }
        },
        error: (err) => {
          // this.checkingApply = false;
          console.warn('⚠️ can-apply failed, redirecting anyway', err);
          this.router.navigateByUrl('/user/my-loans');
        },
      });
  }



setAvatarData(name: string) {
  this.userInitial = name?.charAt(0).toUpperCase() || '';
  this.avatarBgClass = this.getAvatarColor(this.userInitial);
}

getAvatarColor(letter: string): string {
  if (!letter) return 'from-slate-400 to-slate-600';

  if (letter >= 'A' && letter <= 'F') return 'from-blue-500 to-blue-700';
  if (letter >= 'G' && letter <= 'L') return 'from-emerald-500 to-emerald-700';
  if (letter >= 'M' && letter <= 'R') return 'from-purple-500 to-purple-700';
  return 'from-orange-500 to-orange-700';
}


  loadUserProfile() {
    this.http.get<any>('http://localhost:8080/api/user/viewProfile')
      .subscribe(res => {
        this.userProfile = res;

        this.userFullName = res.fullName;
        this.userLevel = res.role;
       this.userInitial = res.fullName.charAt(0).toUpperCase();
    this.avatarBgClass = this.getAvatarColor(this.userInitial);

    // 🔥 Tell Angular: update view safely
    this.cdr.detectChanges();
      });
  }

}
