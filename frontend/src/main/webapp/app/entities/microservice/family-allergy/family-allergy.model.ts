import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { AllergyType } from 'app/entities/enumerations/allergy-type.model';

export interface IFamilyAllergy {
  id: number;
  type?: keyof typeof AllergyType | null;
  description?: string | null;
  affectedPerson?: string | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewFamilyAllergy = Omit<IFamilyAllergy, 'id'> & { id: null };
