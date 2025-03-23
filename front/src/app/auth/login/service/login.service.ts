import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ILogin, ILoginResponse } from '../interface/login.interface';
import { catchError, Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/enviroment';
import { AuthService } from '../../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  login(login: ILogin): Observable<ILoginResponse | any> {
    return this.http.post<ILoginResponse>(`${environment.apiUrlBase}/auth/login`,login).pipe(
      tap(res => {
        if (res.success) {
          console.log(res);
          this.authService.storeTokens(res.result.accessToken, res.result.refreshToken);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return error
      })
    );
  }
}
