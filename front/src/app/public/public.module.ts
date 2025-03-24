import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PublicRoutingModule } from './public-routing.module';
import { MatPaginator } from '@angular/material/paginator';
import { PostComponent } from './post/post.component';
import { RouterLink } from '@angular/router';
import { PostDetailComponent } from './post/post-detail/post-detail.component';


@NgModule({
  declarations: [
    PostComponent,
    PostDetailComponent
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    MatPaginator,
    RouterLink
  ]
})
export class PublicModule { }
