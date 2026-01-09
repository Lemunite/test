import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AllergyResolve from './route/allergy-routing-resolve.service';

const allergyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/allergy.component').then(m => m.AllergyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/allergy-detail.component').then(m => m.AllergyDetailComponent),
    resolve: {
      allergy: AllergyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/allergy-update.component').then(m => m.AllergyUpdateComponent),
    resolve: {
      allergy: AllergyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/allergy-update.component').then(m => m.AllergyUpdateComponent),
    resolve: {
      allergy: AllergyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default allergyRoute;
