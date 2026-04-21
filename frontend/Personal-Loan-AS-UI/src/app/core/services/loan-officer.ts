import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoanOfficer } from '../models/loan-officer.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoanOfficerService {

  private baseUrl = 'http://localhost:8080/api/loanOfficer';

  constructor(private http: HttpClient) {}

  addProfile(payload: LoanOfficer): Observable<any> {
    return this.http.post(`${this.baseUrl}/addProfile`, payload,{ withCredentials: true });
  }

  updateProfile(id: number, payload: LoanOfficer): Observable<any> {
    return this.http.put(`${this.baseUrl}/updateProfile/${id}`, payload);
  }
}
