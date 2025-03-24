import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPost, IPostPaginateResponse, IPostResponse } from '../interface/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrlBase}/posts`;

  constructor(private http: HttpClient) {}

  getPosts(page: number, pageSize: number):
  Observable<IPostPaginateResponse> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('size', pageSize.toString());

    return this.http.get<IPostPaginateResponse>(this.apiUrl, { params });
  }

  getPost(id: string): Observable<IPostResponse> {
    return this.http.get<IPostResponse>(`${this.apiUrl}/${id}`);
  }

  updatePost(post: IPost): Observable<IPostResponse> {
    return this.http.put<IPostResponse>(`${this.apiUrl}/${post.id}`, post);
  }

  deletePost(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  createPost(post: IPost): Observable<IPostResponse> {
    return this.http.post<IPostResponse>(this.apiUrl, post);
  }

}
