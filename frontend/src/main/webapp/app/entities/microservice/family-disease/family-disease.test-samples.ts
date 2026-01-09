import { IFamilyDisease, NewFamilyDisease } from './family-disease.model';

export const sampleWithRequiredData: IFamilyDisease = {
  id: 18407,
  affectedPerson: 'instead intellect',
};

export const sampleWithPartialData: IFamilyDisease = {
  id: 15324,
  affectedPerson: 'powerfully',
};

export const sampleWithFullData: IFamilyDisease = {
  id: 22811,
  affectedPerson: 'whoever pish orderly',
  relationshipToPatient: 'meh selfishly geez',
};

export const sampleWithNewData: NewFamilyDisease = {
  affectedPerson: 'begonia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
