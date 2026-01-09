import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VaccinationForBabyResolve from './route/vaccination-for-baby-routing-resolve.service';

const vaccinationForBabyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vaccination-for-baby.component').then(m => m.VaccinationForBabyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vaccination-for-baby-detail.component').then(m => m.VaccinationForBabyDetailComponent),
    resolve: {
      vaccinationForBaby: VaccinationForBabyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vaccination-for-baby-update.component').then(m => m.VaccinationForBabyUpdateComponent),
    resolve: {
      vaccinationForBaby: VaccinationForBabyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vaccination-for-baby-update.component').then(m => m.VaccinationForBabyUpdateComponent),
    resolve: {
      vaccinationForBaby: VaccinationForBabyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vaccinationForBabyRoute;
