// src/app/auth/auth.routes.ts
import { Routes } from '@angular/router';
import { AuthGuard } from '../auth/guard/auth-guard-guard';

export const AUTH_ROUTES: Routes = [
  {
    path: 'user/dashboard',
    loadComponent: () =>
      import('../user/dashboard/user-dashboard').then(m => m.UserDashboard),
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./register/register').then(m => m.Register)
  }
];
