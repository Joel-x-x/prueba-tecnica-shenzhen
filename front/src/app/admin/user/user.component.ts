import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Subscription } from 'rxjs';
import { UserFormComponent } from './user-form/user-form.component';
import { IUser } from './interfaces/user.interface';
import { UserService } from './services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  standalone: false
})
export class UserComponent {
  private subscriptions: Subscription = new Subscription();
  displayedColumns: string[] = ['firstNames', 'lastNames', 'email', 'roles', 'actions'];
  dataSource = new MatTableDataSource<IUser>();
  totalUsers: number = 0;
  pageSize: number = 5;
  pageIndex: number = 0;
  users: IUser[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
  @ViewChild(MatSort) sort: MatSort | undefined;

  constructor(
    private userService: UserService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
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

  OpenUserForm(): void {
    const dialogRef = this.dialog.open(UserFormComponent, {
      data: { id: '' }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadUsers();
    });
  }

  OpenUserFormEdit(id: number): void {
    const dialogRef = this.dialog.open(UserFormComponent, {
      data: { id: id }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadUsers();
    });
  }

  delete(id: string): void {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.snackBar.open('Usuario eliminado correctamente', 'Cerrar', {
            duration: 3000,
          });
          this.loadUsers();
        },
        error: () => {
          this.snackBar.open('Error al eliminar usuario', 'Cerrar', {
            duration: 3000,
          });
        }
      });
    }
  }
}
