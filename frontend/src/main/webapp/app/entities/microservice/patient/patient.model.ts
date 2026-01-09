import dayjs from 'dayjs/esm';
import { IAdditionalInformation } from 'app/entities/microservice/additional-information/additional-information.model';
import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IPatient {
  id: number;
  fullName?: string | null;
  gender?: keyof typeof Gender | null;
  dateOfBirth?: dayjs.Dayjs | null;
  placeOfBirth?: string | null;
  bloodTypeAbo?: string | null;
  bloodTypeRh?: string | null;
  ethnic?: string | null;
  nationality?: string | null;
  religion?: string | null;
  job?: string | null;
  idNumber?: string | null;
  idIssueDate?: dayjs.Dayjs | null;
  idIssuePlace?: string | null;
  healthInsuranceNumber?: string | null;
  permanentAddress?: string | null;
  permanentWard?: string | null;
  permanentDistrict?: string | null;
  permanentProvince?: string | null;
  currentAddress?: string | null;
  currentWard?: string | null;
  currentDistrict?: string | null;
  currentProvince?: string | null;
  landlinePhone?: string | null;
  mobilePhone?: string | null;
  email?: string | null;
  motherName?: string | null;
  fatherName?: string | null;
  caregiverName?: string | null;
  caregiverRelation?: string | null;
  caregiverLandlinePhone?: string | null;
  caregiverMobilePhone?: string | null;
  familyCode?: string | null;
  additionalInfo?: Pick<IAdditionalInformation, 'id'> | null;
  vaccinationsForBaby?: Pick<IVaccinationForBaby, 'id'> | null;
}

export type NewPatient = Omit<IPatient, 'id'> & { id: null };
