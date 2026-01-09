import dayjs from 'dayjs/esm';
import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { VaccineType } from 'app/entities/enumerations/vaccine-type.model';

export interface IVaccine {
  id: number;
  name?: keyof typeof VaccineType | null;
  notVaccinated?: boolean | null;
  injectionDate?: dayjs.Dayjs | null;
  reaction?: string | null;
  nextAppointment?: dayjs.Dayjs | null;
  vaccinationForBaby?: Pick<IVaccinationForBaby, 'id'> | null;
}

export type NewVaccine = Omit<IVaccine, 'id'> & { id: null };
