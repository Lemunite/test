import dayjs from 'dayjs/esm';

import { IPatient, NewPatient } from './patient.model';

export const sampleWithRequiredData: IPatient = {
  id: 2514,
  fullName: 'custody if who',
  gender: 'MALE',
};

export const sampleWithPartialData: IPatient = {
  id: 6781,
  fullName: 'weekly unimpressively',
  gender: 'MALE',
  dateOfBirth: dayjs('2026-01-08'),
  placeOfBirth: 'ill-fated',
  bloodTypeRh: 'whereas circa poorly',
  ethnic: 'phooey gymnast',
  nationality: 'narrowcast wicked naturally',
  religion: 'orderly scarcely aside',
  idIssueDate: dayjs('2026-01-08'),
  idIssuePlace: 'apprehensive put',
  permanentAddress: 'vivid loyalty',
  permanentProvince: 'sparkling compromise',
  currentWard: 'likable',
  currentProvince: 'strong until',
  landlinePhone: 'following license well-groomed',
  caregiverName: 'reassemble deliquesce',
  caregiverRelation: 'obediently when',
  caregiverLandlinePhone: 'ah millet victorious',
};

export const sampleWithFullData: IPatient = {
  id: 9819,
  fullName: 'an',
  gender: 'FEMALE',
  dateOfBirth: dayjs('2026-01-08'),
  placeOfBirth: 'pace jump',
  bloodTypeAbo: 'miscalculate dependable pfft',
  bloodTypeRh: 'fencing atop',
  ethnic: 'concerning chubby broadly',
  nationality: 'tectonics captain',
  religion: 'oof gorgeous',
  job: 'icebreaker',
  idNumber: 'though smuggle',
  idIssueDate: dayjs('2026-01-08'),
  idIssuePlace: 'as',
  healthInsuranceNumber: 'courageously yummy veto',
  permanentAddress: 'after',
  permanentWard: 'parody',
  permanentDistrict: 'upliftingly correctly',
  permanentProvince: 'misread',
  currentAddress: 'never',
  currentWard: 'furthermore once yippee',
  currentDistrict: 'whoa',
  currentProvince: 'plus',
  landlinePhone: 'beyond since',
  mobilePhone: 'gadzooks',
  email: 'Rachael_Schaefer-Lemke76@hotmail.com',
  motherName: 'charming lest technologist',
  fatherName: 'gosh a barring',
  caregiverName: 'hmph',
  caregiverRelation: 'upbeat malfunction',
  caregiverLandlinePhone: 'whenever even rundown',
  caregiverMobilePhone: 'instead neaten',
  familyCode: 'suckle',
};

export const sampleWithNewData: NewPatient = {
  fullName: 'kick labourer brr',
  gender: 'OTHERS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
