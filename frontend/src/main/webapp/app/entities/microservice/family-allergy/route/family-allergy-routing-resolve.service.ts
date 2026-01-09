import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamilyAllergy } from '../family-allergy.model';
import { FamilyAllergyService } from '../service/family-allergy.service';

const familyAllergyResolve = (route: ActivatedRouteSnapshot): Observable<null | IFamilyAllergy> => {
  const id = route.params.id;
  if (id) {
    return inject(FamilyAllergyService)
      .find(id)
      .pipe(
        mergeMap((familyAllergy: HttpResponse<IFamilyAllergy>) => {
          if (familyAllergy.body) {
            return of(familyAllergy.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default familyAllergyResolve;
