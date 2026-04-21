import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfile } from '../../auth/authservice/auth-service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class HeaderComponent {
  user: UserProfile | null = null;

  constructor(private http: HttpClient, private router: Router, private cdr:ChangeDetectorRef) {
    this.http.get<UserProfile>('http://localhost:8080/api/user/viewProfile',
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }).subscribe((user) => {
      this.user = user;
      this.cdr.detectChanges();
    });
  }

  logout() {
    // this.authService.logout();
    this.router.navigate(['/login']);
  }
}
