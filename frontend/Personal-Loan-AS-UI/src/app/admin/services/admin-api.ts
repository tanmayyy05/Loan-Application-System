import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface DashboardResponse {
  totalUsers: number;
  totalApplications: number;
  pendingLoanApprovals: number;
  totalAmountDisbursed: number;
}

@Injectable({ providedIn: 'root' })
export class AdminDashboardService  {

    private apiUrl = 'http://localhost:8080/api/admin/dashboard';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(this.apiUrl,{
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
  }

  // getDashboardStats() {
  //   return of({
  //     users: 1240,
  //     activeLoans: 860,
  //     pendingApplications: 12,
  //   });
  // }

  getApplications() {
    return of([
      { id: 'PL-1023', applicant: 'Rahul', amount: 500000, status: 'PENDING' },
      { id: 'PL-1024', applicant: 'Anita', amount: 300000, status: 'PENDING' },
    ]);
  }

  authorizeDisbursement(id: string) {
    return of({ success: true });
  }
}
