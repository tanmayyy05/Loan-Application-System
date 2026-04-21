import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-admin-application-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './application-list.html',
})
export class ApplicationList implements OnInit {
  applications: any[] = [];
  statusFilter = 'ALL';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchApplications();
  }

  fetchApplications(): void {
    this.http.get<any[]>('http://localhost:8080/api/admin/loan-applications').subscribe({
      next: (res) => (this.applications = res),
      error: (err) => console.error(err),
    });
  }

  get filteredApplications(): any[] {
    return this.statusFilter === 'ALL'
      ? this.applications
      : this.applications.filter((app) => app.applicationStatus === this.statusFilter);
  }
}
