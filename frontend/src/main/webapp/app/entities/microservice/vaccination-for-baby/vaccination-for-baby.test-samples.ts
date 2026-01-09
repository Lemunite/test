import { IVaccinationForBaby, NewVaccinationForBaby } from './vaccination-for-baby.model';

export const sampleWithRequiredData: IVaccinationForBaby = {
  id: 4233,
};

export const sampleWithPartialData: IVaccinationForBaby = {
  id: 435,
  vaccine: 'VGB_SO_SINH',
  numberUse: 5459,
};

export const sampleWithFullData: IVaccinationForBaby = {
  id: 28917,
  vaccine: 'SOI_2',
  numberUse: 16662,
};

export const sampleWithNewData: NewVaccinationForBaby = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
