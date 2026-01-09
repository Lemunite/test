import { IFamilyDisease } from 'app/entities/microservice/family-disease/family-disease.model';
import { DiseaseName } from 'app/entities/enumerations/disease-name.model';

export interface IDisease {
  id: number;
  name?: keyof typeof DiseaseName | null;
  specificType?: string | null;
  description?: string | null;
  familyDisease?: Pick<IFamilyDisease, 'id'> | null;
}

export type NewDisease = Omit<IDisease, 'id'> & { id: null };
