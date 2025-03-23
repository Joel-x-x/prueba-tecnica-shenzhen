import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IRegister, IRegisterResponse } from '../interface/register.interface';
import { catchError, Observable, of, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/enviroment';
import { AuthService } from '../../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  register(register: IRegister): Observable<IRegisterResponse> {
    return this.http.post<IRegisterResponse>(`${environment.apiUrlBase}/auth/register`, register).pipe(
      tap(res => {
        if(res.id){
          // this.authService.storeEncryptedToken("id", res.id);
          console.log("User registered successfully");
        }
      }),
      catchError(err => {
        console.error('Error registering:', err);
        return of({} as IRegisterResponse);
      })
    );
  }


}
