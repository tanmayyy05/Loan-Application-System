import { Injectable } from '@angular/core';
import { ApplicationInitStatus } from '../models/application-init-status.model';
import { BehaviorSubject } from 'rxjs';

interface ApplicationState {
  applicationId: string;
  status: ApplicationInitStatus;
}

@Injectable({
  providedIn: 'root',
})
export class ApplicationStateService {
  private application$ = new BehaviorSubject<ApplicationState>({
    applicationId: 'APP001',
    status: 'USER_CREATED',
  });

  getApplication() {
    return this.application$.asObservable();
  }

  updateStatus(status: ApplicationInitStatus) {
    this.application$.next({
      ...this.application$.value,
      status,
    });
  }
}
