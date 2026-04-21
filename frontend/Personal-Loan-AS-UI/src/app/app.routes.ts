import { Routes } from '@angular/router';
import { Home } from './pages/home/home';

// Auth
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

// User
import { UserLayout } from './shared/layouts/user-layout/user-layout';
import { UserDashboard } from './user/dashboard/user-dashboard';

// Loan Officer
import { LoanOfficerLayout } from './shared/layouts/loan-officer-layout/loan-officer-layout';
import {  LoanOfficerDashboardComponent } from './loan-officer/dashboard/dashboard';
import { Applications } from './loan-officer/applications/applications';
import { ApplicationDetails } from './loan-officer/application-details/application-details';
import { History } from './loan-officer/history/history';

// Admin
import { AdminDashboard } from './admin/dashboard/admin-dashboard';

import { MyLoans } from './user/my-loans/my-loans';
import { EmiSchedule } from './user/emi-schedule/emi-schedule';
import { UserProfile } from './user/profile/profile';
import { UserLoanDetails } from './user/my-loans/user-loan-details/user-loan-details';
import { PayEmi } from './user/pay-emi/pay-emi';
import { UploadDocuments } from './user/upload-documents/upload-documents';
import { AdminLayout } from './shared/layouts/admin-layout/admin-layout';
import { ApplicationList as AdminApplicationList } from './admin/applications/application-list/application-list';
import { ApplicantDetails as AdminApplicantDetails } from './admin/applications/applicant-details/applicant-details';
import { AdminDisbursementList } from './admin/disbursement/disbursement-list/disbursement-list';
import { SystemRules } from './admin/system-rules/system-rules/system-rules';
import { AuditLogs } from './admin/audit-logs/audit-logs/audit-logs';
import { ProfileEditComponent } from './user/profile-edit/profile-edit';
import { BasicDetails } from './user/apply-loan/steps/basic-details/basic-details';
import { PersonalDetails } from './user/apply-loan/steps/personal-details/personal-details';
import { EmploymentDetails } from './user/apply-loan/steps/employment-details/employment-details';
import { Documents } from './user/apply-loan/steps/documents/documents';
import { ReviewSubmit } from './user/apply-loan/steps/review-submit/review-submit';
import { ApplyLoanLayout } from './shared/layouts/apply-loan-layout/apply-loan-layout';
import { AUTH_ROUTES } from './auth/auth.routes';
import { LoanStepper2 } from './shared/components/loan-stepper2/loan-stepper2';
import { NotEligible } from './user/eligible/not-eligible/not-eligible';
import { LoanOfficers } from './admin/loan-officers/loan-officers';
import { EditLoanOfficer } from './admin/edit-loan-officer/edit-loan-officer';
import { Applicants } from './admin/applications/applicants/applicants';
import { LoanDetails } from './user/loan-details/loan-details';
import { AuthGuard } from '../app/auth/guard/auth-guard-guard';

export const routes: Routes = [
  // HOME
  { path: '', component: Home },

  // AUTH
  ...AUTH_ROUTES,

  // USER
  {
    path: 'user',
    component: UserLayout,
    //canActivateChild: [authGuard, roleGuard],
    // data: { roles: ['USER'] },
    canActivateChild: [AuthGuard],
    children: [
      // Dashboard
      { path: 'dashboard', component: UserDashboard },
      {
        path: 'loan-details/:loanAccountId',
        component: LoanDetails,
      },

       { path: 'loan-stepper2', component: LoanStepper2 },


      {
        path: 'loan-stepper2',
        loadComponent: () =>
          import('./shared/components/loan-stepper2/loan-stepper2').then(m => m.LoanStepper2)
      },

      // Apply Loan (multi-step)
      {
        path: 'apply-loan',
        component: ApplyLoanLayout,
        children: [
          { path: '', redirectTo: 'basic', pathMatch: 'full' },
          { path: 'basic', component: BasicDetails },
          { path: 'personal', component: PersonalDetails },
          { path: 'employment', component: EmploymentDetails },
          { path: 'documents', component: Documents },
          { path: 'review', component: ReviewSubmit },
        ],
      },

      {
        path: 'not-eligible',
        component: NotEligible,
      },

      // Other user pages
      { path: 'my-loans', component: MyLoans },
      { path: 'my-loans/:id', component: UserLoanDetails },
      { path: 'emi-schedule', component: EmiSchedule },

      { path: 'profile', component: UserProfile },
     { path: 'profile-edit', component: ProfileEditComponent },
      { path: 'upload-documents/:id', component: UploadDocuments },
      { path: 'pay-emi/:id', component: PayEmi },
      { path: 'emi-schedule/:id', component: EmiSchedule },

      // Default
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  // LOAN OFFICER
  {
    path: 'loan-officer',
    component: LoanOfficerLayout,
    children: [
      { path: 'dashboard', component: LoanOfficerDashboardComponent },
      { path: 'applications', component: Applications },
      { path: 'application/:id', component: ApplicationDetails },
      { path: 'history', component: History },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  // ADMIN
  {
    path: 'admin',
    component: AdminLayout,
    // canActivate: [AdminRoleGuard],
    children: [
      { path: 'dashboard', component: AdminDashboard },
      { path: 'applications', component: Applicants },
      { path: 'applications/list', component: AdminApplicationList },
      { path: 'applications/:id', component: AdminApplicantDetails },
      { path: 'disbursements', component: AdminDisbursementList },
      { path: 'system-rules', component: SystemRules },
      { path: 'audit-logs', component: AuditLogs },
      { path: 'loan-officers', component: LoanOfficers },
      { path: 'edit-loan-officers', component: EditLoanOfficer },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  // FALLBACK
  { path: '**', redirectTo: '' },
];
