import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MedicalRecordResolve from './route/medical-record-routing-resolve.service';

const medicalRecordRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medical-record.component').then(m => m.MedicalRecordComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medical-record-detail.component').then(m => m.MedicalRecordDetailComponent),
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medical-record-update.component').then(m => m.MedicalRecordUpdateComponent),
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medical-record-update.component').then(m => m.MedicalRecordUpdateComponent),
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalRecordRoute;
