import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VaccineResolve from './route/vaccine-routing-resolve.service';

const vaccineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vaccine.component').then(m => m.VaccineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vaccine-detail.component').then(m => m.VaccineDetailComponent),
    resolve: {
      vaccine: VaccineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vaccine-update.component').then(m => m.VaccineUpdateComponent),
    resolve: {
      vaccine: VaccineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vaccine-update.component').then(m => m.VaccineUpdateComponent),
    resolve: {
      vaccine: VaccineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vaccineRoute;
