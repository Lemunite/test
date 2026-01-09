import { IFamilyAllergy, NewFamilyAllergy } from './family-allergy.model';

export const sampleWithRequiredData: IFamilyAllergy = {
  id: 23763,
  type: 'DRUGS',
  affectedPerson: 'fisherman tame terribly',
};

export const sampleWithPartialData: IFamilyAllergy = {
  id: 15451,
  type: 'CHEMICALS',
  description: 'youthfully',
  affectedPerson: 'gee than volleyball',
};

export const sampleWithFullData: IFamilyAllergy = {
  id: 28372,
  type: 'CHEMICALS',
  description: 'yuck except',
  affectedPerson: 'that',
};

export const sampleWithNewData: NewFamilyAllergy = {
  type: 'OTHER',
  affectedPerson: 'along lawful boggle',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
