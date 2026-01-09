import { IPatient } from 'app/entities/microservice/patient/patient.model';

export interface ISurgeryHistory {
  id: number;
  bodyPart?: string | null;
  surgeryYear?: number | null;
  note?: string | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewSurgeryHistory = Omit<ISurgeryHistory, 'id'> & { id: null };
