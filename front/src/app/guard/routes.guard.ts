import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivate } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../auth/service/auth.service';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RoutesGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> {

    return this.authService.getUser().pipe(
      map(response => {
        if (response?.result?.roles[0]?.name == "ADMIN") {
          return true; // Permitir acceso
        } else {
          return this.router.createUrlTree(['/admin/post']); // Redirigir
        }
      }),
      catchError(() => {
        return of(this.router.createUrlTree(['/admin/post'])); // En caso de error, redirigir
      })
    );
  }
}
