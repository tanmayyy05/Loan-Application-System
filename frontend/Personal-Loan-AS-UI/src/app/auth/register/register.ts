import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../authservice/auth-service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
})
export class Register {
  registerForm: FormGroup;
  loading = false;
  errorMsg = '';

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService) {
    this.registerForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      dateOfBirth: ['', Validators.required],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  submit() {
  if (this.registerForm.invalid) return;

  console.log('Submitting register form');

  this.authService.register(this.registerForm.value).subscribe({
    next: (res) => {
      console.log('Register success:', res);
      this.router.navigate(['/login']);
    },
    error: (err) => {
      console.error('Register failed:', err);
    }
  });
}

}
