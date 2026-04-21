import { AfterViewInit, ChangeDetectorRef, Component, NgZone, QueryList, ViewChildren } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterModule } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';

import { ApplicationInitStatus } from '../../core/models/application-init-status.model';
import { mapStatusToStage } from '../../core/utils/application-status-mapper';
import { UIApplicationStage } from '../../core/models/ui-application-stage.model';
import { ApplicationTimeline } from '../../shared/components/application-timeline/application-timeline';
import { HeaderComponent } from '../../shared/header/header';

@Component({
  standalone: true,
  selector: 'app-user-dashboard',
  imports: [
    CommonModule,
    RouterModule,
    RouterLink,
    ApplicationTimeline,
    HeaderComponent,
    MatSnackBarModule, // ✅ IMPORTANT
    NgOptimizedImage
  ],
  templateUrl: './user-dashboard.html',
  styleUrl: './dashboard.css',
})
export class UserDashboard implements OnInit, OnDestroy {
  currentStatus: ApplicationInitStatus = 'DOCUMENT_VERIFICATION_PENDING';
  currentStage: UIApplicationStage = mapStatusToStage(this.currentStatus);

  checkingApply = false;

  private readonly API = 'http://localhost:8080/api/loan-applications';

   @ViewChildren('bannerImg') images!: QueryList<HTMLImageElement>;

  constructor(
    private http: HttpClient,
    private router: Router,
    private snackBar: MatSnackBar,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    
  ) {}

  /* ================= STATUS UI ================= */

  get currentStatusLabel(): string {
    switch (this.currentStatus) {
      case 'DOCUMENT_VERIFICATION_PENDING':
        return 'Document Verification in Progress';
      case 'LOAN_APPROVED':
        return 'Loan Approved';
      case 'LOAN_DISBURSED':
        return 'Loan Disbursed';
      default:
        return 'Application Submitted';
    }
  }

  get statusDescription(): string {
    switch (this.currentStatus) {
      case 'DOCUMENT_VERIFICATION_PENDING':
        return 'We are reviewing your uploaded documents. You’ll be notified if any action is required.';
      case 'LOAN_APPROVED':
        return 'Your loan has been approved. Please review and accept the sanction letter.';
      case 'LOAN_DISBURSED':
        return 'The loan amount has been successfully credited to your account.';
      default:
        return 'Your application has been received and is under initial review.';
    }
  }

  get primaryActionLabel(): string | null {
    if (this.currentStatus === 'DOCUMENT_VERIFICATION_PENDING') {
      return 'Upload Documents';
    }
    if (this.currentStatus === 'LOAN_APPROVED') {
      return 'Accept Sanction';
    }
    return null;
  }

  get primaryActionRoute(): any[] {
    if (this.currentStatus === 'DOCUMENT_VERIFICATION_PENDING') {
      return ['/user/upload-documents', 'PL-1023'];
    }
    if (this.currentStatus === 'LOAN_APPROVED') {
      return ['/user/loan-details', 'PL-1023'];
    }
    return [];
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
          if (res.canApply) {
            console.log('✅ Navigating to loan-stepper2',res.canApply);
            this.router.navigate(['/user/loan-stepper2']);
        
          } else {
            this.snackBar.open(res.reason || 'You cannot apply for a loan right now', 'OK', {
              duration: 6000,
              verticalPosition: 'top',
            });
            this.router.navigateByUrl('/user/dashboard');
          }
        },
        error: (err) => {
          console.warn('⚠️ can-apply failed, redirecting anyway', err);
          this.router.navigateByUrl('/user/dashboard');
        },
      });
  }

banners = [
    'assets/images/peronalloanbaner.png',
    'assets/images/webBondsBanner.png',
    'assets/images/Banner.png',
    'assets/images/webCCMPGenericBanner.png'
  ];

  // clones
  infiniteBanners: string[] = [];

  currentIndex = 1; // start from first REAL slide
  realIndex = 0;

  disableTransition = false;
  intervalId!: number;

  ngOnInit() {
    // clone last + first
    this.infiniteBanners = [
      this.banners[this.banners.length - 1],
      ...this.banners,
      this.banners[0],
    ];

    this.startAutoSlide();
  }

  startAutoSlide() {
    this.intervalId = window.setInterval(() => {
      this.currentIndex++;
      this.realIndex =
        (this.realIndex + 1) % this.banners.length;
    }, 1200); // 🔥 speed (1.2s)
  }

  onTransitionEnd() {
    // Jump from clone → real slide
    if (this.currentIndex === this.infiniteBanners.length - 1) {
      this.disableTransition = true;
      this.currentIndex = 1;

      requestAnimationFrame(() => {
        this.disableTransition = false;
      });
    }

    if (this.currentIndex === 0) {
      this.disableTransition = true;
      this.currentIndex = this.banners.length;

      requestAnimationFrame(() => {
        this.disableTransition = false;
      });
    }
  }
   nextSlide() {
  clearInterval(this.intervalId);
  this.currentIndex++;
  this.realIndex =
    (this.realIndex + 1) % this.banners.length;
  this.startAutoSlide();
}


  goToSlide(index: number) {
    this.currentIndex = index + 1;
    this.realIndex = index;
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

}
