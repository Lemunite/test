import dayjs from 'dayjs/esm';

import { IVaccine, NewVaccine } from './vaccine.model';

export const sampleWithRequiredData: IVaccine = {
  id: 29262,
  name: 'BAI_LIET_2',
};

export const sampleWithPartialData: IVaccine = {
  id: 673,
  name: 'BAI_LIET_3',
  notVaccinated: true,
  reaction: 'failing word',
};

export const sampleWithFullData: IVaccine = {
  id: 1459,
  name: 'SOI_1',
  notVaccinated: false,
  injectionDate: dayjs('2026-01-09'),
  reaction: 'greatly qua',
  nextAppointment: dayjs('2026-01-08'),
};

export const sampleWithNewData: NewVaccine = {
  name: 'VGB_SO_SINH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
