import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Registration } from './register.model';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  private readonly http = inject(HttpClient);
  private readonly applicationConfigService = inject(ApplicationConfigService);

  save(registration: Registration): Observable<{}> {
    return this.http.post(this.applicationConfigService.getEndpointFor('api/register'), registration).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Registration error:', error);

        // Return the error so component can handle specific error types
        if (error.error?.type === 'urn:jhipster:problem:login-already-used') {
          return throwError(() => ({
            status: error.status,
            type: 'LOGIN_ALREADY_USED',
            message: 'Login name already registered',
          }));
        } else if (error.error?.type === 'urn:jhipster:problem:email-already-used') {
          return throwError(() => ({
            status: error.status,
            type: 'EMAIL_ALREADY_USED',
            message: 'Email address already registered',
          }));
        } else if (error.message?.includes('already exists')) {
          // Handle Keycloak registration conflicts
          if (error.message?.includes('Username')) {
            return throwError(() => ({
              status: error.status,
              type: 'LOGIN_ALREADY_USED',
              message: 'Username already exists in Keycloak',
            }));
          } else if (error.message?.includes('email')) {
            return throwError(() => ({
              status: error.status,
              type: 'EMAIL_ALREADY_USED',
              message: 'Email already exists in Keycloak',
            }));
          }
        }

        return throwError(() => error);
      }),
    );
  }
}
