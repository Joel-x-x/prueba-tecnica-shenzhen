import { Component } from '@angular/core';
import { PostService } from './service/post.service';
import { PageEvent } from '@angular/material/paginator';
import { IPost } from './interface/post.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
  standalone: false
})
export class PostComponent {
  posts: IPost[] = [];
  pageIndex: number = 0;
  pageSize: number = 6;
  totalItems: number = 0;

  constructor(
    private postService: PostService,
    private router: Router) {}

  loadUsers(page: number = 0): void {
    this.postService.getPublicPosts(page, this.pageSize).subscribe(response => {
      this.posts = response.result.content;
      this.totalItems = response.result.totalElements;
    });
  }
  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.loadUsers(this.pageIndex);
  }

  ngOnInit(): void {
    this.loadUsers()
  }

}
