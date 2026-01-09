import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaccine } from '../vaccine.model';
import { VaccineService } from '../service/vaccine.service';

const vaccineResolve = (route: ActivatedRouteSnapshot): Observable<null | IVaccine> => {
  const id = route.params.id;
  if (id) {
    return inject(VaccineService)
      .find(id)
      .pipe(
        mergeMap((vaccine: HttpResponse<IVaccine>) => {
          if (vaccine.body) {
            return of(vaccine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vaccineResolve;
