import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { TetanusDose } from 'app/entities/enumerations/tetanus-dose.model';

export interface IPregnancyTetanus {
  id: number;
  dose?: keyof typeof TetanusDose | null;
  notInjected?: boolean | null;
  injectionDate?: dayjs.Dayjs | null;
  pregnancyMonth?: number | null;
  reaction?: string | null;
  nextAppointment?: dayjs.Dayjs | null;
  numberOfDosesReceived?: number | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewPregnancyTetanus = Omit<IPregnancyTetanus, 'id'> & { id: null };
