import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from "../../header/header";

@Component({
  standalone: true,
  selector: 'app-loan-officer-layout',
  imports: [CommonModule, RouterModule, HeaderComponent],
  templateUrl: './loan-officer-layout.html',
  styleUrls: ['./loan-officer-layout.css'],
})
export class LoanOfficerLayout {
  darkMode: any;
  role: any;
  toggleTheme() {
    throw new Error('Method not implemented.');
  }
}
