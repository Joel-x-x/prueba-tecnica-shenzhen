import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subject, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { catchError, switchMap, filter, take } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private excludedUrls = [
    '/public/post',
    '/auth/login',
    '/auth/register'
  ];

  private isRefreshing = false;
  private refreshTokenSubject: Subject<string | null> = new Subject<string | null>();

  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.excludedUrls.some(url => req.url.includes(url))) {
      return next.handle(req);
    }

    const accessToken = this.authService.getToken();
    let authReq = req;
    if (accessToken) {
      authReq = req.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return this.handle401Error(authReq, next);
        }
        return throwError(() => error);
      })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((newAccessToken: string) => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(newAccessToken);
          const clonedReq = req.clone({ setHeaders: { Authorization: `Bearer ${newAccessToken}` } });
          return next.handle(clonedReq);
        }),
        catchError(error => {
          this.isRefreshing = false;
          this.authService.logout();
          this.router.navigate(['/auth/login']);
          return throwError(() => error);
        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token !== null),
        take(1),
        switchMap(token => {
          const clonedReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
          return next.handle(clonedReq);
        })
      );
    }
  }
}
