import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaccinationTCMR } from '../vaccination-tcmr.model';
import { VaccinationTCMRService } from '../service/vaccination-tcmr.service';

const vaccinationTCMRResolve = (route: ActivatedRouteSnapshot): Observable<null | IVaccinationTCMR> => {
  const id = route.params.id;
  if (id) {
    return inject(VaccinationTCMRService)
      .find(id)
      .pipe(
        mergeMap((vaccinationTCMR: HttpResponse<IVaccinationTCMR>) => {
          if (vaccinationTCMR.body) {
            return of(vaccinationTCMR.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vaccinationTCMRResolve;
