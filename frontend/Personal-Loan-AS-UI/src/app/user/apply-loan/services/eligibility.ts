import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EligibilityService {

  constructor(private http: HttpClient) {}

  checkEligibility(payload: any): Observable<any> {
    return this.http.post(
      'http://localhost:8080/api/loan-applications/apply',
      payload
    );
  }

  uploadDocument(formData: FormData) {
  return this.http.post(
    'http://localhost:8080/api/documents/uploadSingleDocumnet',
    formData
  );
}

}
