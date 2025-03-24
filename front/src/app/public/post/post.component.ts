import { Component } from '@angular/core';
import { PostService } from './service/post.service';

@Component({
  selector: 'app-post',
  imports: [],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postService.getPublicPosts(1, 10).subscribe(response => {
      console.log(response);
    });
  }



}
