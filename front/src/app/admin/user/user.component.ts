import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { UserFormComponent } from './user-form/user-form.component';
import { IUser } from './interfaces/user.interface';
import { UserService } from './services/user.service';
@Component({
  selector: 'app-user',
  imports: [ MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent {
onSubmit() {
throw new Error('Method not implemented.');
}
 private subscriptions: Subscription = new Subscription();
 displayedColumns: string[] = ['firstNames',  'lastNames', 'email', 'role', 'actions'];
 dataSource = new MatTableDataSource<IUser>();
 totalUsers: number = 0;
 pageSize: number = 5;
 pageIndex: number = 0;
 users: IUser[] = []

 @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
 @ViewChild(MatSort) sort: MatSort | undefined;

 constructor(private userService: UserService,
 public dialog: MatDialog,


 ) {}

 ngOnInit(): void {
   this.loadUsers();

   if (this.paginator) {
     this.subscriptions.add(
       this.paginator.page.subscribe(() => this.onPageChange({ pageIndex: this.paginator!.pageIndex, pageSize: this.pageSize, length: this.totalUsers }))
     );
   }

   if (this.sort) {
     this.subscriptions.add(
       this.sort.sortChange.subscribe(() => {
         this.dataSource.sort = this.sort!;
       })
     );
   }
 }

  /**
  * Se ejecuta cuando el componente se destruye para evitar memory leaks.
  */
 ngOnDestroy(): void {
   this.subscriptions.unsubscribe();
 }
 ViewWillEnter() {
   this.loadUsers();

   if (this.paginator) {
     this.subscriptions.add(
       this.paginator.page.subscribe(() => this.onPageChange({ pageIndex: this.paginator!.pageIndex, pageSize: this.pageSize, length: this.totalUsers }))
     );
   }

   if (this.sort) {
     this.subscriptions.add(
       this.sort.sortChange.subscribe(() => {
         this.dataSource.sort = this.sort!;
       })
     );
   }
 }
 OpenDenuncias() {
     const dialogRef = this.dialog.open(UserFormComponent, {
     });
     const suscription = dialogRef.afterClosed().subscribe(() => {
     });
     this.subscriptions.add(suscription);
   }

 loadUsers(page: number = 1): void {
   const subscription = this.userService.getUsers(page, this.pageSize).subscribe(response => {
     this.totalUsers = response.result.totalElements;
     this.users = response.result.content;
     this.dataSource.data = this.users;
   });

   this.subscriptions.add(subscription);
 }

 onPageChange(event: PageEvent): void {
   this.pageIndex = event.pageIndex;
   this.loadUsers(this.pageIndex + 1);
 }




 OpenUserForm() {
   const dialogRef = this.dialog.open(UserFormComponent, {
     data: { id:'' } // Pasamos el ID

   });
   dialogRef.afterClosed().subscribe(() => {
     this.loadUsers();
   });
 }
 OpenUserFormEdit(id: number) {
   const dialogRef = this.dialog.open(UserFormComponent, {
     data: { id: id } // Pasamos el ID
   });

   dialogRef.afterClosed().subscribe(() => {
     console.log("Modal cerrado");
     this.loadUsers();
   });
 }
 delete(id: string){
  //  this.subscriptions.add(
  //    this.MascotasService.deleteMascota(id).subscribe({
  //      next: data => {
  //        console.log('Datos recibidos:', data);
  //        this.loadUsers();
  //      },
  //      error: error => {
  //        console.log('Error en la peticion', 'error');
  //      },
  //      complete: () => {
  //      }
  //    })
  //  )
 }
}
