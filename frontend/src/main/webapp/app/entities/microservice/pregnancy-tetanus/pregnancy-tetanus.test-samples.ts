import dayjs from 'dayjs/esm';

import { IPregnancyTetanus, NewPregnancyTetanus } from './pregnancy-tetanus.model';

export const sampleWithRequiredData: IPregnancyTetanus = {
  id: 27778,
  dose: 'UV2',
};

export const sampleWithPartialData: IPregnancyTetanus = {
  id: 17676,
  dose: 'UV4',
};

export const sampleWithFullData: IPregnancyTetanus = {
  id: 22771,
  dose: 'UV2',
  notInjected: true,
  injectionDate: dayjs('2026-01-08'),
  pregnancyMonth: 28483,
  reaction: 'presell hawk although',
  nextAppointment: dayjs('2026-01-08'),
  numberOfDosesReceived: 28090,
};

export const sampleWithNewData: NewPregnancyTetanus = {
  dose: 'UV3',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
