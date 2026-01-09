import { IAllergy, NewAllergy } from './allergy.model';

export const sampleWithRequiredData: IAllergy = {
  id: 21345,
  type: 'CHEMICALS',
};

export const sampleWithPartialData: IAllergy = {
  id: 17779,
  type: 'FOOD',
  description: 'courageously showboat supposing',
};

export const sampleWithFullData: IAllergy = {
  id: 29445,
  type: 'CHEMICALS',
  description: 'astride likewise',
};

export const sampleWithNewData: NewAllergy = {
  type: 'OTHER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
