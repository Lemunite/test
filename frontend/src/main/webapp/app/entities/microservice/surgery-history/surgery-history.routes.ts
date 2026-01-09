import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SurgeryHistoryResolve from './route/surgery-history-routing-resolve.service';

const surgeryHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/surgery-history.component').then(m => m.SurgeryHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/surgery-history-detail.component').then(m => m.SurgeryHistoryDetailComponent),
    resolve: {
      surgeryHistory: SurgeryHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/surgery-history-update.component').then(m => m.SurgeryHistoryUpdateComponent),
    resolve: {
      surgeryHistory: SurgeryHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/surgery-history-update.component').then(m => m.SurgeryHistoryUpdateComponent),
    resolve: {
      surgeryHistory: SurgeryHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default surgeryHistoryRoute;
