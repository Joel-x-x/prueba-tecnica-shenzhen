import { Component } from '@angular/core';
import { LoginService } from './service/login.service';
import { AlertsService } from '../../services/alerts.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(
    private loginService: LoginService,
    private authService: AuthService,
    private alertsService: AlertsService,
    private router: Router
  ) {}

  loginForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required
      ]),
    },
  );

  submit() {
    const credentials = {
      email: this.loginForm.value.email ?? '',
      password: this.loginForm.value.password ?? '',
    };

    this.loginService.login(credentials).subscribe({
      next: (res) => {
        this.alertsService.success("Login exitoso");
        this.router.navigate(['/admin/user']);
      },
      error: (error) => {
        console.error('Error: ', error);
      }
    }
    );
  }

  get fControls() {
    return this.loginForm.controls;
  }

}
