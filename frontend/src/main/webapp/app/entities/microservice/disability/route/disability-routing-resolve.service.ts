import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisability } from '../disability.model';
import { DisabilityService } from '../service/disability.service';

const disabilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisability> => {
  const id = route.params.id;
  if (id) {
    return inject(DisabilityService)
      .find(id)
      .pipe(
        mergeMap((disability: HttpResponse<IDisability>) => {
          if (disability.body) {
            return of(disability.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default disabilityResolve;
