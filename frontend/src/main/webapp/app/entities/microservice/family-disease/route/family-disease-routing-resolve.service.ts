import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamilyDisease } from '../family-disease.model';
import { FamilyDiseaseService } from '../service/family-disease.service';

const familyDiseaseResolve = (route: ActivatedRouteSnapshot): Observable<null | IFamilyDisease> => {
  const id = route.params.id;
  if (id) {
    return inject(FamilyDiseaseService)
      .find(id)
      .pipe(
        mergeMap((familyDisease: HttpResponse<IFamilyDisease>) => {
          if (familyDisease.body) {
            return of(familyDisease.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default familyDiseaseResolve;
