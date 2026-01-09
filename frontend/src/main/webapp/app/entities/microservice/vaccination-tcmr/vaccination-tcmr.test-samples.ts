import dayjs from 'dayjs/esm';

import { IVaccinationTCMR, NewVaccinationTCMR } from './vaccination-tcmr.model';

export const sampleWithRequiredData: IVaccinationTCMR = {
  id: 17626,
  vaccine: 'CUM_3',
};

export const sampleWithPartialData: IVaccinationTCMR = {
  id: 7422,
  vaccine: 'TA_2',
  notVaccinated: false,
  vaccinated: true,
};

export const sampleWithFullData: IVaccinationTCMR = {
  id: 20410,
  vaccine: 'QUAI_BI_3',
  notVaccinated: false,
  vaccinated: false,
  injectionDate: dayjs('2026-01-09'),
  reaction: 'fall',
  nextAppointment: dayjs('2026-01-08'),
};

export const sampleWithNewData: NewVaccinationTCMR = {
  vaccine: 'QUAI_BI_1',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
