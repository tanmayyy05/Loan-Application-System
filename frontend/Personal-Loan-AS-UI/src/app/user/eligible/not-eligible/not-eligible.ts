import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-not-eligible',
  standalone: true,
  imports: [MatCardModule],
  templateUrl: './not-eligible.html',
  styleUrl: './not-eligible.css',
})
export class NotEligible {

  constructor(private router: Router) {}

  backToDashboard() {
     this.router.navigate(['user/dashboard']);
    }
}
