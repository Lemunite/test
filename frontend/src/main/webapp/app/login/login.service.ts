import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Login } from './login.model';
import { AccountService } from 'app/core/auth/account.service';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private accountService: AccountService,
    private router: Router,
  ) {}

  login(credentials: Login): Observable<void> {
    return this.http
      .post<{ id_token?: string }>(this.applicationConfigService.getEndpointFor('api/authenticate'), credentials, { observe: 'response' })
      .pipe(
        tap(response => {
          // Try to get token from Authorization header first
          let token = '';
          const bearer = response.headers.get('Authorization');
          if (bearer) {
            token = bearer.replace('Bearer ', '');
          }
          // Fallback to body's id_token if header is not present
          else if (response.body?.id_token) {
            token = response.body.id_token;
          }

          if (token) {
            this.storeAuthenticationToken(token);
            console.debug('Token stored successfully from authentication endpoint');
          } else {
            console.warn('No token found in authentication response');
          }
        }),
        switchMap(() => this.accountService.identity(true)),
        map(() => void 0),
      );
  }

  logout(): void {
    this.removeAuthenticationToken();
    this.accountService.authenticate(null);
    this.router.navigate(['/login']).then(() => {
      // Force page refresh to clear any cached data
      window.location.reload();
    });
  }

  private storeAuthenticationToken(jwt: string): void {
    localStorage.setItem('jhi-authenticationToken', jwt);
    sessionStorage.setItem('jhi-authenticationToken', jwt);
  }

  private removeAuthenticationToken(): void {
    localStorage.removeItem('jhi-authenticationToken');
    sessionStorage.removeItem('jhi-authenticationToken');
  }
}
