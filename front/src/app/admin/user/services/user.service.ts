import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IUserRequest, IUserPaginateResponse, IUserResponse } from '../interfaces/user.interface';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrlBase}/users`;

  constructor(private http: HttpClient) {}

  getUsers(page: number, pageSize: number): Observable<IUserPaginateResponse> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('size', pageSize.toString());

    return this.http.get<IUserPaginateResponse>(this.apiUrl, { params });
  }

  getUser(id: string): Observable<IUserResponse> {
    return this.http.get<IUserResponse>(`${this.apiUrl}/${id}`);
  }

  updateUser(user: IUserRequest): Observable<IUserResponse> {
    return this.http.put<IUserResponse>(`${this.apiUrl}/${user.id}`, user);
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  createUser(user: IUserRequest): Observable<IUserResponse> {
    return this.http.post<IUserResponse>(this.apiUrl, user);
  }
}
