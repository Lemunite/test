import dayjs from 'dayjs/esm';
import { IMedicalRecord } from 'app/entities/microservice/medical-record/medical-record.model';

export interface IParaclinicalResult {
  id: number;
  testName?: string | null;
  result?: string | null;
  resultDate?: dayjs.Dayjs | null;
  medicalRecord?: Pick<IMedicalRecord, 'id'> | null;
}

export type NewParaclinicalResult = Omit<IParaclinicalResult, 'id'> & { id: null };
