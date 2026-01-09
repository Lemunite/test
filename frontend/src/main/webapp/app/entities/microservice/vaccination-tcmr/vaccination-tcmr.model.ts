import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { VaccineTCMRType } from 'app/entities/enumerations/vaccine-tcmr-type.model';

export interface IVaccinationTCMR {
  id: number;
  vaccine?: keyof typeof VaccineTCMRType | null;
  notVaccinated?: boolean | null;
  vaccinated?: boolean | null;
  injectionDate?: dayjs.Dayjs | null;
  reaction?: string | null;
  nextAppointment?: dayjs.Dayjs | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewVaccinationTCMR = Omit<IVaccinationTCMR, 'id'> & { id: null };
