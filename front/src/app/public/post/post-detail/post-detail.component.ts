import { Component } from '@angular/core';
import { IPost } from '../interface/post.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.scss',
  standalone: false
})
export class PostDetailComponent {
  post: IPost = {
    id: "",
    title: "",
    content: "",
    isPublic: false,
    created: new Date(),
  };
  postId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router) {}

  ngOnInit() {
    this.postId = this.route.snapshot.paramMap.get('id');

    if (this.postId) {
      this.postService.getPost(this.postId).subscribe({
        next: response => {
          this.post = response.result;
        },
        error: error => {
          console.error(error);
        }
      });
    }
  }

  volver() {
    this.router.navigate(['/public/post']);
  }
}
