import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganExamination } from '../organ-examination.model';
import { OrganExaminationService } from '../service/organ-examination.service';

const organExaminationResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganExamination> => {
  const id = route.params.id;
  if (id) {
    return inject(OrganExaminationService)
      .find(id)
      .pipe(
        mergeMap((organExamination: HttpResponse<IOrganExamination>) => {
          if (organExamination.body) {
            return of(organExamination.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default organExaminationResolve;
