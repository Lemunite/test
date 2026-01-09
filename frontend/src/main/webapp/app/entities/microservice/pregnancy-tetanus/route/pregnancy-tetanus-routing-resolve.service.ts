import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPregnancyTetanus } from '../pregnancy-tetanus.model';
import { PregnancyTetanusService } from '../service/pregnancy-tetanus.service';

const pregnancyTetanusResolve = (route: ActivatedRouteSnapshot): Observable<null | IPregnancyTetanus> => {
  const id = route.params.id;
  if (id) {
    return inject(PregnancyTetanusService)
      .find(id)
      .pipe(
        mergeMap((pregnancyTetanus: HttpResponse<IPregnancyTetanus>) => {
          if (pregnancyTetanus.body) {
            return of(pregnancyTetanus.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pregnancyTetanusResolve;
