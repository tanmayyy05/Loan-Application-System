import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../authservice/auth-service';
import { inject } from '@angular/core';

// export const AuthGuard: CanActivateFn = () => {
//   const auth = inject(AuthService);
//   const router = inject(Router);

//   const token = auth.getToken();

//   if (token) {
//     return true;
//   }

//    auth.logout();
//    router.navigate(['/login']);
//   return false;
// };
export const AuthGuard: CanActivateFn = () => {
  const token = localStorage.getItem('token');
  console.log('AuthGuard fired, token =', token);
  if (token) {
    console.log('✅ token exists, allow navigation');
    return true;
  }
  console.log('❌ no token, redirecting to login');
  const router = inject(Router);
  router.navigate(['/login']);
  return false;
};
