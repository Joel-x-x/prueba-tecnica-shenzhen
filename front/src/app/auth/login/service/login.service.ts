import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ILogin, ILoginResponse } from '../../register/interface/login.interface';
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
        if (res.acessToken) {
          this.authService.storeEncryptedToken(res.acessToken);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return error
      })
    );
  }
}
