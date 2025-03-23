import { Component } from '@angular/core';
import { RegisterService } from './service/register.service';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { hasSpecialCharacter, hasNumber, hasUpperCase, hasLowerCase } from '../validators';
import { AlertsService } from '../../services/alerts.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  constructor(private registerService: RegisterService,
    private alertsService: AlertsService,
    private router: Router
  ) {}

  registerForm = new FormGroup(
    {
      firstNames: new FormControl('', [Validators.required]),
      lastNames: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        hasSpecialCharacter,
        hasNumber,
        hasUpperCase,
        hasLowerCase,
      ]),
      passwordConfirmar: new FormControl('', [Validators.required,this.confirmarClave,]),
    },
  );

  submit() {
    const credentials = {
      email: this.registerForm.value.email ?? '',
      password: this.registerForm.value.password ?? '',
      firstNames: this.registerForm.value.firstNames ?? '',
      lastNames: this.registerForm.value.lastNames ?? '',
    };

    this.registerService.register(credentials).subscribe({
      next: (res) => {
        this.alertsService.success("Registro exitoso");
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        console.error('Error during registration:', error);
      }
    }
    );
  }

  get fControls() {
    return this.registerForm.controls;
  }

  confirmarClave(control: FormControl): {[s: string]: boolean} {
    const formGroup = control.parent;
    if (formGroup) {
      const claveControl = formGroup.get('password');
      const claveConfirmarControl = formGroup.get('passwordConfirmar');
      if (claveControl && claveConfirmarControl) {
        if (claveControl.value === claveConfirmarControl.value) {
          return {}; // La validación es exitosa
        }
      }
    }
    return { 'noCoincide': true }; // La validación falla
  }

  // goToNotificationsPage() {
  //   this.navController.navigateForward('admin/inicio');
  // }

}
