import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IUserResponse } from '../interfaces/user.interface';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(page: number, pageSize: number): Observable<IUserResponse> {
    const offset = (page - 1) * pageSize;
    const token = localStorage.getItem('accessToken');

    let headers = new HttpHeaders().set('Authorization', `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3NDI3NDk5NzUsImV4cCI6MTc0MjgzNjM3NX0.6K3NVAnE2_lwW7AnFe8KLOraqpfvsEC5WzXExpraYlQ`);
    let params = new HttpParams()
      .set('offset', offset.toString())
      .set('limit', pageSize.toString());

    return this.http.get<IUserResponse>(`${environment.apiUrlBase}/users`, { params, headers });
  }
}

