import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PregnancyTetanusResolve from './route/pregnancy-tetanus-routing-resolve.service';

const pregnancyTetanusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pregnancy-tetanus.component').then(m => m.PregnancyTetanusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pregnancy-tetanus-detail.component').then(m => m.PregnancyTetanusDetailComponent),
    resolve: {
      pregnancyTetanus: PregnancyTetanusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pregnancy-tetanus-update.component').then(m => m.PregnancyTetanusUpdateComponent),
    resolve: {
      pregnancyTetanus: PregnancyTetanusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pregnancy-tetanus-update.component').then(m => m.PregnancyTetanusUpdateComponent),
    resolve: {
      pregnancyTetanus: PregnancyTetanusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pregnancyTetanusRoute;
