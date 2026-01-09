import { IDisease, NewDisease } from './disease.model';

export const sampleWithRequiredData: IDisease = {
  id: 8371,
  name: 'ASTHMA',
};

export const sampleWithPartialData: IDisease = {
  id: 25415,
  name: 'EPILEPSY',
};

export const sampleWithFullData: IDisease = {
  id: 12461,
  name: 'MENTAL',
  specificType: 'out horn',
  description: 'whether howl',
};

export const sampleWithNewData: NewDisease = {
  name: 'EPILEPSY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
