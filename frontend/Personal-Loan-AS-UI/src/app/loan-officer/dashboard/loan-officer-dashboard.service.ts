import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoanOfficerDashboardResponse {
  totalApplications: number;
  pendingVerification: number;
  documentsReturned: number;
  documentsRejected: number;
}

@Injectable({ providedIn: 'root' })
export class LoanOfficerDashboardService {

  private apiUrl = 'http://localhost:8080/api/loan-officer/dashboard';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<LoanOfficerDashboardResponse> {
    return this.http.get<LoanOfficerDashboardResponse>(this.apiUrl,{
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
  }
}
