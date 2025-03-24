import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AlertsService } from '../../../services/alerts.service';
import { PostService } from '../service/post.service';
import { IPost } from '../interface/post.interface';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss'],
  standalone: false
})
export class PostFormComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription = new Subscription();
  postForm = new FormGroup({
    title: new FormControl('', [Validators.required]),
    content: new FormControl('', [Validators.required, Validators.minLength(10)]),
    isPublic: new FormControl(false)
  });

  constructor(
    public dialogRef: MatDialogRef<PostFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id?: string },
    private postService: PostService,
    private alertService: AlertsService
  ) {}

  ngOnInit() {
    if (this.data.id) {
      this.getPost();
    }
  }

  getPost() {
    this.subscriptions.add(
      this.postService.getPost(this.data.id!).subscribe({
        next: (data) => {
          this.postForm.patchValue({
            title: data.result.title,
            content: data.result.content,
            isPublic: data.result.isPublic
          });
        },
        error: (error) => {
          console.error('Error al obtener el post:', error);
          this.alertService.error('No se pudo obtener la información del post.');
        }
      })
    );
  }

  onSubmitPost() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    const post: IPost = {
      ...(this.postForm.value as unknown as IPost),
      id: this.data.id
    };

    if (this.data.id) {
      this.subscriptions.add(
        this.postService.updatePost(post).subscribe({
          next: () => {
            this.alertService.success('Post actualizado correctamente');
            this.cerrarModal();
          },
          error: (error) => {
            console.error('Error al actualizar post:', error);
            this.alertService.error('No se pudo actualizar el post.');
          }
        })
      );
    } else {
      this.subscriptions.add(
        this.postService.createPost(post).subscribe({
          next: () => {
            this.alertService.success('Post creado correctamente');
            this.cerrarModal();
          },
          error: (error) => {
            console.error('Error al crear post:', error);
            this.alertService.error('No se pudo crear el post.');
          }
        })
      );
    }
  }

  cerrarModal() {
    this.dialogRef.close();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
