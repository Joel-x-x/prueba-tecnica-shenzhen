import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPostPaginateResponse } from '../interface/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrlBase}/posts/public`;

  constructor(private http: HttpClient) { }

  getPublicPosts(page: number, pageSize: number):
  Observable<IPostPaginateResponse> {
    const offset = (page -1) * pageSize;
    const params = new HttpParams()
      .set('offset', offset.toString())
      .set('limit', pageSize.toString());

    return this.http.get<IPostPaginateResponse>(this.apiUrl, { params });
  }

}
