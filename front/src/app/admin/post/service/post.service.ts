import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPost, IPostPaginateResponse } from '../interface/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrlBase}/posts`;

  constructor(private http: HttpClient) {}

  getPosts(page: number, pageSize: number):
  Observable<IPostPaginateResponse> {
    const offset = (page -1) * pageSize;
    const params = new HttpParams()
      .set('offset', offset.toString())
      .set('limit', pageSize.toString());

    return this.http.get<IPostPaginateResponse>(this.apiUrl, { params });
  }

  getPost(id: string): Observable<IPost> {
    return this.http.get<IPost>(`${this.apiUrl}/${id}`);
  }

  updatePost(post: IPost): Observable<IPost> {
    return this.http.put<IPost>(`${this.apiUrl}/${post.id}`, post);
  }

  deletePost(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  createPost(post: IPost): Observable<IPost> {
    return this.http.post<IPost>(this.apiUrl, post);
  }

}
