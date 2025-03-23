import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { UserComponent } from '../user.component';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AlertsService } from '../../../services/alerts.service';
import { UserService } from '../services/user.service';
import { IUser } from '../interfaces/user.interface';

@Component({
  selector: 'app-user-form',
  imports: [FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss'
})
export class UserFormComponent {
  onSubmit() {
    throw new Error('Method not implemented.');
    }
     private subscriptions: Subscription = new Subscription();
      constructor(
        public dialogRef: MatDialogRef<UserComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { id: string }, private userService: UserService, private alertService: AlertsService
      ) {}
      user: IUser | undefined;
     userForm = new FormGroup({
        email: new FormControl('', [Validators.required]),
        firstNames: new FormControl('', [Validators.required]),
        lastNames: new FormControl('', [Validators.required]),
        roles: new FormControl('', [Validators.required]),
      });
      ngOnInit() {
        console.log("ID recibido:", this.data.id);
        if (this.data.id) {
          this.getUsuario()
        }
      }
      getUsuario(){
        this.userService.getUser(this.data.id).subscribe({
          next: data => {
            this.userForm.patchValue({ ...data });
            console.log('Configuración por ID:', data);
            this.user = data;
          },
          error: error => {

          },
          complete: () => {
          }
        });
      }
      onSubmitUser() {
         if (this.userForm.valid) {
              console.log('Formulario enviado:', this.userForm.value);

              if (this.user) {
                const user: IUser = {
                  ...(this.userForm.value as unknown as IUser),
                  'id': this.user.id,

                };
                console.log('Actualizando configuración:', user);
                this.userService.updateUser(user).subscribe({
                  next: data => {
                    console.log('Configuración actualizada:', data);
                  },
                  error: error => {
                  },
                  complete: () => {
                    this.alertService.success("Usuario actualizado correctamente")

                    this.cerrarModal()

                  }
                });
              } else {
                const user: IUser = {
                  ...(this.userForm.value as unknown as IUser),
                };
                this.userService.createUser(user).subscribe({
                  next: data => {
                    console.log('Configuración creada:', data);

                  },
                  error: error => {

                  },
                  complete: () => {
                    this.alertService.success("Usuario creado correctamente")
                    this.cerrarModal()
                  }
                });
              }

            }
      }

      cerrarModal() {
        this.dialogRef.close();
      }
}
