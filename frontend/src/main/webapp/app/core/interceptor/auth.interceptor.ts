import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from '../config/application-config.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private applicationConfigService: ApplicationConfigService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('jhi-authenticationToken') ?? sessionStorage.getItem('jhi-authenticationToken');
    const serverApiUrl = this.applicationConfigService.getEndpointFor('');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      // Log for debugging (only in development)
      if (request.url.includes('/api/') && !request.url.includes('/authenticate')) {
        console.debug(`[AuthInterceptor] Adding token to request: ${request.method} ${request.url}`);
      }
    } else if (request.url.includes('/api/') && !request.url.includes('/authenticate')) {
      console.warn(`[AuthInterceptor] No token available for request: ${request.method} ${request.url}`);
    }
    return next.handle(request);
  }
}
