import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from "../../header/header";

@Component({
  selector: 'app-admin-layout',
  imports: [RouterModule, HeaderComponent],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout {}
