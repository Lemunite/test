import dayjs from 'dayjs/esm';
import { IOrganExamination } from 'app/entities/microservice/organ-examination/organ-examination.model';
import { IPatient } from 'app/entities/microservice/patient/patient.model';

export interface IMedicalRecord {
  id: number;
  examinationDate?: dayjs.Dayjs | null;
  medicalHistory?: string | null;
  leftEyeNoGlass?: number | null;
  rightEyeNoGlass?: number | null;
  leftEyeWithGlass?: number | null;
  rightEyeWithGlass?: number | null;
  pulse?: number | null;
  temperature?: number | null;
  bloodPressure?: string | null;
  respiratoryRate?: number | null;
  weight?: number | null;
  height?: number | null;
  bmi?: number | null;
  waist?: number | null;
  skinMucosa?: string | null;
  other?: string | null;
  diseaseName?: string | null;
  diseaseCode?: string | null;
  advice?: string | null;
  docterName?: string | null;
  organExamination?: Pick<IOrganExamination, 'id'> | null;
  patient?: Pick<IPatient, 'id'> | null;
}

export type NewMedicalRecord = Omit<IMedicalRecord, 'id'> & { id: null };
