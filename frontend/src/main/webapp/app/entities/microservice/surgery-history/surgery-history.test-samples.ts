import { ISurgeryHistory, NewSurgeryHistory } from './surgery-history.model';

export const sampleWithRequiredData: ISurgeryHistory = {
  id: 8834,
};

export const sampleWithPartialData: ISurgeryHistory = {
  id: 12249,
  bodyPart: 'unfortunate closely',
  surgeryYear: 2153,
  note: 'what',
};

export const sampleWithFullData: ISurgeryHistory = {
  id: 7947,
  bodyPart: 'safely',
  surgeryYear: 108,
  note: 'veto gosh represent',
};

export const sampleWithNewData: NewSurgeryHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
