import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AlertsService } from '../../../services/alerts.service';
import { UserService } from '../services/user.service';
import { IUser, IUserRequest } from '../interfaces/user.interface';
import { hasLowerCase, hasNumber, hasSpecialCharacter, hasUpperCase } from '../../../auth/validators';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss',
  standalone: false
})

export class UserFormComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();
  user: IUserRequest | undefined;
  userResponse: IUser | undefined;

  userForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    firstNames: new FormControl('', [Validators.required]),
    lastNames: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      hasSpecialCharacter,
      hasNumber,
      hasUpperCase,
      hasLowerCase,
    ]),
    passwordConfirmar: new FormControl('', [Validators.required,this.confirmarClave,]),
  });

  constructor(
    public dialogRef: MatDialogRef<UserFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: string },
    private userService: UserService,
    private alertService: AlertsService
  ) {}

  ngOnInit() {
    if (this.data.id) {
      this.getUsuario();
      this.removeValidationsForEdit();
    }
  }

  getUsuario() {
    this.subscriptions.add(
      this.userService.getUser(this.data.id).subscribe({
        next: (data) => {
          this.userForm.patchValue({
            email: data.result.email,
            firstNames: data.result.firstNames,
            lastNames: data.result.lastNames,
            role: data.result.roles[0].name
          });
          this.userResponse = data.result;
        },
        error: (error) => {
          console.error('Error al obtener usuario:', error);
          this.alertService.error('No se pudo obtener la información del usuario.');
        }
      })
    );
  }

  get fControls() {
    return this.userForm.controls;
  }

  onSubmitUser() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched(); // Muestra errores en el formulario
      return;
    }

    const user: IUserRequest = {
      ...(this.userForm.value as unknown as IUserRequest),
      id: this.data.id
    };

    if (this.data.id) {
      // Actualizar usuario
      this.subscriptions.add(
        this.userService.updateUser(user).subscribe({
          next: () => {
            this.alertService.success("Usuario actualizado correctamente");
            this.cerrarModal();
          },
          error: (error) => {
            console.error('Error al actualizar usuario:', error);
            this.alertService.error('No se pudo actualizar el usuario.');
          }
        })
      );
    } else {
      // Crear nuevo usuario
      this.subscriptions.add(
        this.userService.createUser(user).subscribe({
          next: () => {
            this.alertService.success("Usuario creado correctamente");
            this.cerrarModal();
          },
          error: (error) => {
            console.error('Error al crear usuario:', error);
            this.alertService.error('No se pudo crear el usuario.');
          }
        })
      );
    }
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

  removeValidationsForEdit() {
    this.userForm.get('firstNames')?.clearValidators();
    this.userForm.get('lastNames')?.clearValidators();
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('passwordConfirmar')?.clearValidators();
    this.userForm.updateValueAndValidity();
  }

  cerrarModal() {
    this.dialogRef.close();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
