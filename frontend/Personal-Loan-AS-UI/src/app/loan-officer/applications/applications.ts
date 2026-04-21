import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import {
  BehaviorSubject,
  combineLatest,
  map,
  Observable,
  shareReplay,
  catchError,
  throwError
} from 'rxjs';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  standalone: true,
  selector: 'app-loan-officer-applications',
  imports: [
    CommonModule,
    RouterModule,
    MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './applications.html',
})
export class Applications implements OnInit {

  private readonly API = 'http://localhost:8080/api/loan-applications';

  /* ================= INPUTS ================= */
  @Input() applications: any[] | null = null;   // ADMIN
  @Input() isAdminView = false;

  /* ================= API STREAM ================= */
  readonly applications$!: Observable<any[]>;

  /* ================= PAGINATION ================= */
  pageSizeOptions = [5, 10, 12, 20];
  totalItems = 0;

  private pageIndex$ = new BehaviorSubject<number>(0);
 pageSize$ = new BehaviorSubject<number>(10);

  paginatedApplications$!: Observable<any[]>;

  constructor(private http: HttpClient) {
    this.applications$ = this.http
      .get<{ content: any[] }>(this.API, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      })
      .pipe(
        map(res => res.content),
        shareReplay(1),
        catchError(err => {
          console.error('API error', err);
          return throwError(() => err);
        })
      );
  }

  ngOnInit(): void {

    const source$ = this.isAdminView
      ? new BehaviorSubject<any[]>(this.applications ?? [])
      : this.applications$;

    this.paginatedApplications$ = combineLatest([
      source$,
      this.pageIndex$,
      this.pageSize$
    ]).pipe(
      map(([apps, pageIndex, pageSize]) => {
        this.totalItems = apps.length;

        const start = pageIndex * pageSize;
        const end = start + pageSize;

        return apps.slice(start, end);
      })
    );
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex$.next(event.pageIndex);
    this.pageSize$.next(event.pageSize);
  }

  getReviewLink(app: any): any[] {
    return this.isAdminView
      ? ['/admin/application', app.applicationId]
      : ['/loan-officer/application', app.applicationId];
  }
}
