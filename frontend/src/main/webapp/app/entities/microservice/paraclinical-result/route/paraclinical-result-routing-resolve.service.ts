import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParaclinicalResult } from '../paraclinical-result.model';
import { ParaclinicalResultService } from '../service/paraclinical-result.service';

const paraclinicalResultResolve = (route: ActivatedRouteSnapshot): Observable<null | IParaclinicalResult> => {
  const id = route.params.id;
  if (id) {
    return inject(ParaclinicalResultService)
      .find(id)
      .pipe(
        mergeMap((paraclinicalResult: HttpResponse<IParaclinicalResult>) => {
          if (paraclinicalResult.body) {
            return of(paraclinicalResult.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default paraclinicalResultResolve;
