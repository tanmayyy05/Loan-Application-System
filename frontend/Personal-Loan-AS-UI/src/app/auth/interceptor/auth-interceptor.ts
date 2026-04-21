import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../authservice/auth-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    if (isTokenExpired(token)) {
      authService.logout();
      authService.login(
        localStorage.getItem('email') || '',
        localStorage.getItem('password') || ''
      );
    }
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }
  return next(req);
  
};

function isTokenExpired(token: string): boolean {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = payload.exp * 1000;
    return Date.now() < expirationTime;
  } catch {
    return true;
  }
}
