import { IDisability, NewDisability } from './disability.model';

export const sampleWithRequiredData: IDisability = {
  id: 15598,
  type: 'VISION',
};

export const sampleWithPartialData: IDisability = {
  id: 17235,
  type: 'VISION',
  description: 'woot devise',
};

export const sampleWithFullData: IDisability = {
  id: 28233,
  type: 'CLEFT',
  description: 'sticky hmph',
};

export const sampleWithNewData: NewDisability = {
  type: 'VISION',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
