import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPostPaginateResponse, IPostResponse } from '../interface/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrlBase}/posts/public`;

  constructor(private http: HttpClient) { }

  getPublicPosts(page: number, pageSize: number):
  Observable<IPostPaginateResponse> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('size', pageSize.toString());

  return this.http.get<IPostPaginateResponse>(this.apiUrl, { params });
  }

    getPost(id: string): Observable<IPostResponse> {
      return this.http.get<IPostResponse>(`${this.apiUrl}/${id}`);
    }

}
