import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DisabilityResolve from './route/disability-routing-resolve.service';

const disabilityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/disability.component').then(m => m.DisabilityComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/disability-detail.component').then(m => m.DisabilityDetailComponent),
    resolve: {
      disability: DisabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/disability-update.component').then(m => m.DisabilityUpdateComponent),
    resolve: {
      disability: DisabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/disability-update.component').then(m => m.DisabilityUpdateComponent),
    resolve: {
      disability: DisabilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disabilityRoute;
