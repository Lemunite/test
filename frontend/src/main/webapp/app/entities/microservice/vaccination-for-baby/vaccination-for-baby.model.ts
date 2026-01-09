import { VaccineType } from 'app/entities/enumerations/vaccine-type.model';

export interface IVaccinationForBaby {
  id: number;
  vaccine?: keyof typeof VaccineType | null;
  numberUse?: number | null;
}

export type NewVaccinationForBaby = Omit<IVaccinationForBaby, 'id'> & { id: null };
