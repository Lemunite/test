import dayjs from 'dayjs/esm';

import { IParaclinicalResult, NewParaclinicalResult } from './paraclinical-result.model';

export const sampleWithRequiredData: IParaclinicalResult = {
  id: 4619,
  testName: 'uh-huh',
};

export const sampleWithPartialData: IParaclinicalResult = {
  id: 17255,
  testName: 'disconnection',
};

export const sampleWithFullData: IParaclinicalResult = {
  id: 11538,
  testName: 'in wherever unlike',
  result: '../fake-data/blob/hipster.txt',
  resultDate: dayjs('2026-01-08'),
};

export const sampleWithNewData: NewParaclinicalResult = {
  testName: 'serialize',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
