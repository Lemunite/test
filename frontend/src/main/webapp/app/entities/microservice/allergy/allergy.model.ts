import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { AllergyType } from 'app/entities/enumerations/allergy-type.model';

export interface IAllergy {
  id: number;
  type?: keyof typeof AllergyType | null;
  description?: string | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewAllergy = Omit<IAllergy, 'id'> & { id: null };
