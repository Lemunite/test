import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OrganExaminationResolve from './route/organ-examination-routing-resolve.service';

const organExaminationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/organ-examination.component').then(m => m.OrganExaminationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/organ-examination-detail.component').then(m => m.OrganExaminationDetailComponent),
    resolve: {
      organExamination: OrganExaminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/organ-examination-update.component').then(m => m.OrganExaminationUpdateComponent),
    resolve: {
      organExamination: OrganExaminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/organ-examination-update.component').then(m => m.OrganExaminationUpdateComponent),
    resolve: {
      organExamination: OrganExaminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organExaminationRoute;
