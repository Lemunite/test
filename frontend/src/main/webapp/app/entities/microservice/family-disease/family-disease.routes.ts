import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FamilyDiseaseResolve from './route/family-disease-routing-resolve.service';

const familyDiseaseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/family-disease.component').then(m => m.FamilyDiseaseComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/family-disease-detail.component').then(m => m.FamilyDiseaseDetailComponent),
    resolve: {
      familyDisease: FamilyDiseaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/family-disease-update.component').then(m => m.FamilyDiseaseUpdateComponent),
    resolve: {
      familyDisease: FamilyDiseaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/family-disease-update.component').then(m => m.FamilyDiseaseUpdateComponent),
    resolve: {
      familyDisease: FamilyDiseaseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default familyDiseaseRoute;
