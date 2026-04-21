import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-user-profile',
  imports: [CommonModule, RouterLink, HttpClientModule],
  templateUrl: './profile.html',
})
export class UserProfile implements OnInit {
  loading = true;
  error = '';

  user: any = null;

  private API = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.http
      .get(`${this.API}/viewProfile`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      })
      .subscribe({
        next: (res) => {
          console.log('Profile response:', res);
          this.user = res;
          this.loading = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          console.error('Profile API error:', err);
          this.error = 'Failed to load profile';
          this.loading = false;
        },
      });
  }

  getInitials(name: string): string {
    if (!name) return '';
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase();
  }
}
