import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurgeryHistory } from '../surgery-history.model';
import { SurgeryHistoryService } from '../service/surgery-history.service';

const surgeryHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ISurgeryHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(SurgeryHistoryService)
      .find(id)
      .pipe(
        mergeMap((surgeryHistory: HttpResponse<ISurgeryHistory>) => {
          if (surgeryHistory.body) {
            return of(surgeryHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default surgeryHistoryResolve;
