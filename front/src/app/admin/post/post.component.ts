import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Subscription } from 'rxjs';
import { PostFormComponent } from './post-form/post-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { IPost } from './interface/post.interface';
import { PostService } from './service/post.service';
import { AlertsService } from '../../services/alerts.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
  standalone: false
})
export class PostComponent {
  private subscriptions: Subscription = new Subscription();
  displayedColumns: string[] = ['title', 'content', 'created', 'isPublic', 'actions'];
  dataSource = new MatTableDataSource<IPost>();
  totalPosts: number = 0;
  pageSize: number = 10;
  pageIndex: number = 0;
  posts: IPost[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
  @ViewChild(MatSort) sort: MatSort | undefined;

  constructor(
    private postService: PostService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar,
    private  alertsService:AlertsService
  ) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadPosts(page: number = 0): void {
    const subscription = this.postService.getPosts(page, this.pageSize).subscribe(response => {
      this.totalPosts = response.result.totalElements;
      this.posts = response.result.content;
      this.dataSource.data = this.posts;
    });
    this.subscriptions.add(subscription);
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.loadPosts(this.pageIndex);
  }

  openPostForm(): void {
    const dialogRef = this.dialog.open(PostFormComponent, {
      data: { id: '' }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadPosts();
    });
  }

  openPostFormEdit(id: string): void {
    const dialogRef = this.dialog.open(PostFormComponent, {
      data: { id: id }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadPosts();
    });
  }

  delete(id: string): void {

    this.alertsService.confirm("Estas a punto de eliminar tu post").subscribe({
      next: data => {
        if(data) {
          this.postService.deletePost(id).subscribe({
            next: () => {
              this.alertsService.success("Post eliminada correctamente");
              this.loadPosts();
            },
            error: () => {
              this.alertsService.error("Error al eliminar post");
            }
          });
        }
      }
    })
  }

  formatDateTime(isoString: string): string {
    const date = new Date(isoString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Meses van de 0-11
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }
}
