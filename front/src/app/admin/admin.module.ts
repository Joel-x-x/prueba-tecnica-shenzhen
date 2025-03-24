import { NgModule } from '@angular/core';
import { CommonModule} from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserFormComponent } from './user/user-form/user-form.component';
import { UserComponent } from './user/user.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RouterModule } from '@angular/router';
import { AdminRoutingModule } from './admin-routing.module';
import { PostComponent } from './post/post.component';
import { PostFormComponent } from './post/post-form/post-form.component';

@NgModule({
  declarations: [
    UserComponent,
    UserFormComponent,
    PostComponent,
    PostFormComponent],
  imports: [
    MatTableModule,
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
    RouterModule,
    AdminRoutingModule
  ],
})
export class AdminModule { }
