import {
  ApplicationConfig,
  importProvidersFrom,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { FormsModule } from '@angular/forms';

import { providePrimeNG } from 'primeng/config';
// import { Lara } from '@primeng/themes/lara';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/interceptor/auth-interceptor';
import { provideNativeDateAdapter } from '@angular/material/core';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    importProvidersFrom(FormsModule),
    provideNativeDateAdapter(),
    provideHttpClient(withInterceptors([authInterceptor]))
    // providePrimeNG({
    //   theme: {
    //     preset: Lara,
    //   },
    // }),
  ],
};
