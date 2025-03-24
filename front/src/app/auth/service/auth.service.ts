import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrlBase;

  constructor(
    private httpClient: HttpClient,
    private router: Router
  ) {}

  decodeJWT(token: string): any {
    try {
      const payload = token.split('.')[1]; // Tomamos la segunda parte del token (payload)
      const decoded = atob(payload); // Decodificamos la base64
      return JSON.parse(decoded); // Convertimos el JSON a objeto
    } catch (error) {
      console.error('Error al decodificar el JWT:', error);
      return null;
    }
  }

  getUser(): Observable<any> {
    const id = this.decodeJWT(this.getToken() ?? '')?.id;
    console.log(id);
    return this.httpClient.get<any>(`${this.apiUrl}/users/${id}`);
  }

  storeTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  refreshToken(): Observable<string> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No se encontró el refresh token'));
    }

    return this.httpClient.post<{ accessToken: string }>(`${this.apiUrl}/auth/refresh-token`, { refreshToken })
      .pipe(
        map(response => {
          this.storeTokens(response.accessToken, refreshToken);
          return response.accessToken;
        }),
        catchError(error => {
          this.logout();
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/auth/login']);
  }
}
