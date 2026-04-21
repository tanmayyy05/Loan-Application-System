import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-profile-edit',
  standalone: true,
  imports: [FormsModule, HttpClientModule],
  templateUrl: './profile-edit.html'
})
export class ProfileEditComponent {

  profile: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.http.get('http://localhost:8080/api/user/profile')
      .subscribe((res: any) => {
        this.profile = res;
      });
  }

  saveProfile() {
    this.http.put('http://localhost:8080/api/user/update', this.profile)
      .subscribe({
        next: () => {
          alert("Profile Updated Successfully");
        },
        error: (err) => {
          console.error(err);
          alert("Error updating profile");
        }
      });
  }
}