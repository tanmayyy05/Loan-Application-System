import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmiResponse } from './emi.model';

@Injectable({ providedIn: 'root' })
export class EmiService {
  private apiUrl = 'http://localhost:8080/api/emi/current';

  constructor(private http: HttpClient) {}

  getCurrentEmis(): Observable<EmiResponse> {
    return this.http.get<EmiResponse>(this.apiUrl);
  }
}
