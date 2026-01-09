import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdditionalInformation } from '../additional-information.model';
import { AdditionalInformationService } from '../service/additional-information.service';

const additionalInformationResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdditionalInformation> => {
  const id = route.params.id;
  if (id) {
    return inject(AdditionalInformationService)
      .find(id)
      .pipe(
        mergeMap((additionalInformation: HttpResponse<IAdditionalInformation>) => {
          if (additionalInformation.body) {
            return of(additionalInformation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default additionalInformationResolve;
