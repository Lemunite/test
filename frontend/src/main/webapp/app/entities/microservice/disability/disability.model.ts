import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { DisabilityType } from 'app/entities/enumerations/disability-type.model';

export interface IDisability {
  id: number;
  type?: keyof typeof DisabilityType | null;
  description?: string | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewDisability = Omit<IDisability, 'id'> & { id: null };
