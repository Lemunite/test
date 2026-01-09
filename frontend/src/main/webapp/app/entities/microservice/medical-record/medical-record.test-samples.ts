import dayjs from 'dayjs/esm';

import { IMedicalRecord, NewMedicalRecord } from './medical-record.model';

export const sampleWithRequiredData: IMedicalRecord = {
  id: 1710,
  examinationDate: dayjs('2026-01-08'),
  diseaseName: 'poorly ack absentmindedly',
  docterName: 'antelope trusting marksman',
};

export const sampleWithPartialData: IMedicalRecord = {
  id: 9041,
  examinationDate: dayjs('2026-01-08'),
  medicalHistory: '../fake-data/blob/hipster.txt',
  leftEyeWithGlass: 22721.01,
  rightEyeWithGlass: 13743.94,
  pulse: 28374,
  bloodPressure: 'tuxedo',
  height: 982.57,
  bmi: 2565.86,
  diseaseName: 'sympathetically mysteriously collaboration',
  advice: '../fake-data/blob/hipster.txt',
  docterName: 'revitalise',
};

export const sampleWithFullData: IMedicalRecord = {
  id: 2454,
  examinationDate: dayjs('2026-01-08'),
  medicalHistory: '../fake-data/blob/hipster.txt',
  leftEyeNoGlass: 30863.88,
  rightEyeNoGlass: 9128.38,
  leftEyeWithGlass: 11771.6,
  rightEyeWithGlass: 29980.07,
  pulse: 16400,
  temperature: 19961.31,
  bloodPressure: 'between barring swiftly',
  respiratoryRate: 30529,
  weight: 6480.31,
  height: 27787.44,
  bmi: 23141.78,
  waist: 26344.21,
  skinMucosa: 'shadowy though',
  other: 'than scruple psst',
  diseaseName: 'old aggressive scuffle',
  diseaseCode: 'scare battle consequently',
  advice: '../fake-data/blob/hipster.txt',
  docterName: 'pish depot',
};

export const sampleWithNewData: NewMedicalRecord = {
  examinationDate: dayjs('2026-01-08'),
  diseaseName: 'softly shameful curse',
  docterName: 'team monthly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
