import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaccinationForBaby } from '../vaccination-for-baby.model';
import { VaccinationForBabyService } from '../service/vaccination-for-baby.service';

const vaccinationForBabyResolve = (route: ActivatedRouteSnapshot): Observable<null | IVaccinationForBaby> => {
  const id = route.params.id;
  if (id) {
    return inject(VaccinationForBabyService)
      .find(id)
      .pipe(
        mergeMap((vaccinationForBaby: HttpResponse<IVaccinationForBaby>) => {
          if (vaccinationForBaby.body) {
            return of(vaccinationForBaby.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vaccinationForBabyResolve;
