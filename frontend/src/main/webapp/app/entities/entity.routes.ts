import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gatewayApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'patient',
    data: { pageTitle: 'gatewayApp.microservicePatient.home.title' },
    loadChildren: () => import('./microservice/patient/patient.routes'),
  },
  {
    path: 'additional-information',
    data: { pageTitle: 'gatewayApp.microserviceAdditionalInformation.home.title' },
    loadChildren: () => import('./microservice/additional-information/additional-information.routes'),
  },
  {
    path: 'allergy',
    data: { pageTitle: 'gatewayApp.microserviceAllergy.home.title' },
    loadChildren: () => import('./microservice/allergy/allergy.routes'),
  },
  {
    path: 'disability',
    data: { pageTitle: 'gatewayApp.microserviceDisability.home.title' },
    loadChildren: () => import('./microservice/disability/disability.routes'),
  },
  {
    path: 'surgery-history',
    data: { pageTitle: 'gatewayApp.microserviceSurgeryHistory.home.title' },
    loadChildren: () => import('./microservice/surgery-history/surgery-history.routes'),
  },
  {
    path: 'family-allergy',
    data: { pageTitle: 'gatewayApp.microserviceFamilyAllergy.home.title' },
    loadChildren: () => import('./microservice/family-allergy/family-allergy.routes'),
  },
  {
    path: 'disease',
    data: { pageTitle: 'gatewayApp.microserviceDisease.home.title' },
    loadChildren: () => import('./microservice/disease/disease.routes'),
  },
  {
    path: 'family-disease',
    data: { pageTitle: 'gatewayApp.microserviceFamilyDisease.home.title' },
    loadChildren: () => import('./microservice/family-disease/family-disease.routes'),
  },
  {
    path: 'vaccine',
    data: { pageTitle: 'gatewayApp.microserviceVaccine.home.title' },
    loadChildren: () => import('./microservice/vaccine/vaccine.routes'),
  },
  {
    path: 'vaccination-for-baby',
    data: { pageTitle: 'gatewayApp.microserviceVaccinationForBaby.home.title' },
    loadChildren: () => import('./microservice/vaccination-for-baby/vaccination-for-baby.routes'),
  },
  {
    path: 'vaccination-tcmr',
    data: { pageTitle: 'gatewayApp.microserviceVaccinationTcmr.home.title' },
    loadChildren: () => import('./microservice/vaccination-tcmr/vaccination-tcmr.routes'),
  },
  {
    path: 'pregnancy-tetanus',
    data: { pageTitle: 'gatewayApp.microservicePregnancyTetanus.home.title' },
    loadChildren: () => import('./microservice/pregnancy-tetanus/pregnancy-tetanus.routes'),
  },
  {
    path: 'medical-record',
    data: { pageTitle: 'gatewayApp.microserviceMedicalRecord.home.title' },
    loadChildren: () => import('./microservice/medical-record/medical-record.routes'),
  },
  {
    path: 'organ-examination',
    data: { pageTitle: 'gatewayApp.microserviceOrganExamination.home.title' },
    loadChildren: () => import('./microservice/organ-examination/organ-examination.routes'),
  },
  {
    path: 'paraclinical-result',
    data: { pageTitle: 'gatewayApp.microserviceParaclinicalResult.home.title' },
    loadChildren: () => import('./microservice/paraclinical-result/paraclinical-result.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
