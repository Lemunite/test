import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ParaclinicalResultResolve from './route/paraclinical-result-routing-resolve.service';

const paraclinicalResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/paraclinical-result.component').then(m => m.ParaclinicalResultComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/paraclinical-result-detail.component').then(m => m.ParaclinicalResultDetailComponent),
    resolve: {
      paraclinicalResult: ParaclinicalResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/paraclinical-result-update.component').then(m => m.ParaclinicalResultUpdateComponent),
    resolve: {
      paraclinicalResult: ParaclinicalResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/paraclinical-result-update.component').then(m => m.ParaclinicalResultUpdateComponent),
    resolve: {
      paraclinicalResult: ParaclinicalResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paraclinicalResultRoute;
