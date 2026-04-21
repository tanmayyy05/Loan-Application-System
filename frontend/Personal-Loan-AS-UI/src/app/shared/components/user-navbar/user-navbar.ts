import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-user-navbar',
  imports: [RouterLink],
  templateUrl: './user-navbar.html',
})
export class UserNavbar {}
