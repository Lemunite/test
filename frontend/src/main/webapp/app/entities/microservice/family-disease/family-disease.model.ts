import { IPatient } from 'app/entities/microservice/patient/patient.model';

export interface IFamilyDisease {
  id: number;
  affectedPerson?: string | null;
  relationshipToPatient?: string | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewFamilyDisease = Omit<IFamilyDisease, 'id'> & { id: null };
