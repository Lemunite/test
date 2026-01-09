import { IOrganExamination, NewOrganExamination } from './organ-examination.model';

export const sampleWithRequiredData: IOrganExamination = {
  id: 10692,
};

export const sampleWithPartialData: IOrganExamination = {
  id: 5925,
  respiratory: 'aftermath delectable',
  urinary: 'boohoo yowza duh',
  endocrine: 'when scared',
  otolaryngology: 'careless how yuck',
  eye: 'familiarize above',
  other: 'oof',
};

export const sampleWithFullData: IOrganExamination = {
  id: 28219,
  cardiovascular: 'standard',
  respiratory: 'bah uh-huh um',
  digestive: 'because under',
  urinary: 'lotion',
  musculoskeletal: 'scarcely mathematics accelerator',
  endocrine: 'disappointment',
  neurological: 'majestically marathon',
  psychiatric: 'utilized',
  surgery: 'floodlight',
  obstetricsAndGynecology: 'cash low demonstrate',
  otolaryngology: 'by',
  dentistryAndMaxillofacialSurgery: 'excluding gadzooks outside',
  eye: 'what',
  dermatology: 'for cycle',
  nutrition: 'royal rejigger ouch',
  exercise: 'however somber quarrelsomely',
  other: 'consequently',
  developmentAssessment: 'ugh',
};

export const sampleWithNewData: NewOrganExamination = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
