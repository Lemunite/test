import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VaccinationTCMRResolve from './route/vaccination-tcmr-routing-resolve.service';

const vaccinationTCMRRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vaccination-tcmr.component').then(m => m.VaccinationTCMRComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vaccination-tcmr-detail.component').then(m => m.VaccinationTCMRDetailComponent),
    resolve: {
      vaccinationTCMR: VaccinationTCMRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vaccination-tcmr-update.component').then(m => m.VaccinationTCMRUpdateComponent),
    resolve: {
      vaccinationTCMR: VaccinationTCMRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vaccination-tcmr-update.component').then(m => m.VaccinationTCMRUpdateComponent),
    resolve: {
      vaccinationTCMR: VaccinationTCMRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vaccinationTCMRRoute;
