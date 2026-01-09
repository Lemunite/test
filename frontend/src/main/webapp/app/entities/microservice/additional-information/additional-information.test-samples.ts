import dayjs from 'dayjs/esm';

import { IAdditionalInformation, NewAdditionalInformation } from './additional-information.model';

export const sampleWithRequiredData: IAdditionalInformation = {
  id: 19534,
  birthStatus: 'SUFFOCATE',
};

export const sampleWithPartialData: IAdditionalInformation = {
  id: 13715,
  alcoholRiskLevel: 'YES',
  alcoholGlassesPerDay: 22380,
  environmentalRisk: '../fake-data/blob/hipster.txt',
  cardiovascularDisease: false,
  diabetes: true,
  asthma: true,
  mentalDisorders: false,
  cancer: 'minus boo brightly',
  contraceptiveMethod: 'um',
  lastPregnancy: 22734,
  numberOfAbortions: 15163,
  numberOfBirths: 28317,
  cesareanSection: 31717,
  difficultDelivery: 1407,
  numberOfPrematureBirths: 2730,
  numberOfChildrenAlive: 15772,
  birthStatus: 'EARLY',
  birthDefectNote: '../fake-data/blob/hipster.txt',
  otherBirthNote: '../fake-data/blob/hipster.txt',
  otherHealthNote: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IAdditionalInformation = {
  id: 24603,
  smoking: 'FREQUENT',
  alcoholRiskLevel: 'YES',
  alcoholGlassesPerDay: 17051,
  drugUse: 'NO',
  physicalActivity: 'STOPPED',
  exposureFactor: '../fake-data/blob/hipster.txt',
  exposureDate: dayjs('2026-01-08'),
  typeToilet: 'FLUSH',
  environmentalRisk: '../fake-data/blob/hipster.txt',
  cardiovascularDisease: true,
  hypertension: true,
  diabetes: false,
  stomachDisease: true,
  chronicLungDisease: true,
  asthma: true,
  goiter: false,
  hepatitis: false,
  congenitalHeartDisease: false,
  mentalDisorders: true,
  autism: true,
  epilepsy: true,
  cancer: 'like er',
  tuberculosis: 'ha splay',
  otherDiseases: '../fake-data/blob/hipster.txt',
  contraceptiveMethod: 'brr phew',
  lastPregnancy: 5015,
  numberOfPregnancies: 4381,
  numberOfMiscarriages: 14793,
  numberOfAbortions: 7334,
  numberOfBirths: 7137,
  vaginalDelivery: 26226,
  cesareanSection: 956,
  difficultDelivery: 10384,
  numberOfFullTermBirths: 24998,
  numberOfPrematureBirths: 27777,
  numberOfChildrenAlive: 32742,
  gynecologicalDiseases: 'till',
  birthStatus: 'SUFFOCATE',
  birthWeight: 22499.18,
  birthHeight: 24304.36,
  birthDefectNote: '../fake-data/blob/hipster.txt',
  otherBirthNote: '../fake-data/blob/hipster.txt',
  otherHealthNote: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAdditionalInformation = {
  birthStatus: 'EARLY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
