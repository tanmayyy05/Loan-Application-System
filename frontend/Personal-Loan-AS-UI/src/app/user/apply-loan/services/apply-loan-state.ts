import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ApplyLoanStateService {
  private data: any = {};

  set(step: string, value: any) {
    this.data[step] = value;
  }

  get(step: string) {
    return this.data[step];
  }

  getAll() {
    return this.data;
  }

  clear() {
    this.data = {};
  }
}
