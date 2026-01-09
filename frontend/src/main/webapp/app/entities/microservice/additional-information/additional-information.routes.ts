import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdditionalInformationResolve from './route/additional-information-routing-resolve.service';

const additionalInformationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/additional-information.component').then(m => m.AdditionalInformationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/additional-information-detail.component').then(m => m.AdditionalInformationDetailComponent),
    resolve: {
      additionalInformation: AdditionalInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/additional-information-update.component').then(m => m.AdditionalInformationUpdateComponent),
    resolve: {
      additionalInformation: AdditionalInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/additional-information-update.component').then(m => m.AdditionalInformationUpdateComponent),
    resolve: {
      additionalInformation: AdditionalInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default additionalInformationRoute;
