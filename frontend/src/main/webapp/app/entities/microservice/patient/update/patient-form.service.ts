import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPatient, NewPatient } from '../patient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPatient for edit and NewPatientFormGroupInput for create.
 */
type PatientFormGroupInput = IPatient | PartialWithRequiredKeyOf<NewPatient>;

type PatientFormDefaults = Pick<NewPatient, 'id'>;

type PatientFormGroupContent = {
  id: FormControl<IPatient['id'] | NewPatient['id']>;
  fullName: FormControl<IPatient['fullName']>;
  gender: FormControl<IPatient['gender']>;
  dateOfBirth: FormControl<IPatient['dateOfBirth']>;
  placeOfBirth: FormControl<IPatient['placeOfBirth']>;
  bloodTypeAbo: FormControl<IPatient['bloodTypeAbo']>;
  bloodTypeRh: FormControl<IPatient['bloodTypeRh']>;
  ethnic: FormControl<IPatient['ethnic']>;
  nationality: FormControl<IPatient['nationality']>;
  religion: FormControl<IPatient['religion']>;
  job: FormControl<IPatient['job']>;
  idNumber: FormControl<IPatient['idNumber']>;
  idIssueDate: FormControl<IPatient['idIssueDate']>;
  idIssuePlace: FormControl<IPatient['idIssuePlace']>;
  healthInsuranceNumber: FormControl<IPatient['healthInsuranceNumber']>;
  permanentAddress: FormControl<IPatient['permanentAddress']>;
  permanentWard: FormControl<IPatient['permanentWard']>;
  permanentDistrict: FormControl<IPatient['permanentDistrict']>;
  permanentProvince: FormControl<IPatient['permanentProvince']>;
  currentAddress: FormControl<IPatient['currentAddress']>;
  currentWard: FormControl<IPatient['currentWard']>;
  currentDistrict: FormControl<IPatient['currentDistrict']>;
  currentProvince: FormControl<IPatient['currentProvince']>;
  landlinePhone: FormControl<IPatient['landlinePhone']>;
  mobilePhone: FormControl<IPatient['mobilePhone']>;
  email: FormControl<IPatient['email']>;
  motherName: FormControl<IPatient['motherName']>;
  fatherName: FormControl<IPatient['fatherName']>;
  caregiverName: FormControl<IPatient['caregiverName']>;
  caregiverRelation: FormControl<IPatient['caregiverRelation']>;
  caregiverLandlinePhone: FormControl<IPatient['caregiverLandlinePhone']>;
  caregiverMobilePhone: FormControl<IPatient['caregiverMobilePhone']>;
  familyCode: FormControl<IPatient['familyCode']>;
  additionalInfo: FormControl<IPatient['additionalInfo']>;
  vaccinationsForBaby: FormControl<IPatient['vaccinationsForBaby']>;
};

export type PatientFormGroup = FormGroup<PatientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PatientFormService {
  createPatientFormGroup(patient: PatientFormGroupInput = { id: null }): PatientFormGroup {
    const patientRawValue = {
      ...this.getFormDefaults(),
      ...patient,
    };
    return new FormGroup<PatientFormGroupContent>({
      id: new FormControl(
        { value: patientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(patientRawValue.fullName, {
        validators: [Validators.required],
      }),
      gender: new FormControl(patientRawValue.gender, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(patientRawValue.dateOfBirth),
      placeOfBirth: new FormControl(patientRawValue.placeOfBirth),
      bloodTypeAbo: new FormControl(patientRawValue.bloodTypeAbo),
      bloodTypeRh: new FormControl(patientRawValue.bloodTypeRh),
      ethnic: new FormControl(patientRawValue.ethnic),
      nationality: new FormControl(patientRawValue.nationality),
      religion: new FormControl(patientRawValue.religion),
      job: new FormControl(patientRawValue.job),
      idNumber: new FormControl(patientRawValue.idNumber),
      idIssueDate: new FormControl(patientRawValue.idIssueDate),
      idIssuePlace: new FormControl(patientRawValue.idIssuePlace),
      healthInsuranceNumber: new FormControl(patientRawValue.healthInsuranceNumber),
      permanentAddress: new FormControl(patientRawValue.permanentAddress),
      permanentWard: new FormControl(patientRawValue.permanentWard),
      permanentDistrict: new FormControl(patientRawValue.permanentDistrict),
      permanentProvince: new FormControl(patientRawValue.permanentProvince),
      currentAddress: new FormControl(patientRawValue.currentAddress),
      currentWard: new FormControl(patientRawValue.currentWard),
      currentDistrict: new FormControl(patientRawValue.currentDistrict),
      currentProvince: new FormControl(patientRawValue.currentProvince),
      landlinePhone: new FormControl(patientRawValue.landlinePhone),
      mobilePhone: new FormControl(patientRawValue.mobilePhone),
      email: new FormControl(patientRawValue.email),
      motherName: new FormControl(patientRawValue.motherName),
      fatherName: new FormControl(patientRawValue.fatherName),
      caregiverName: new FormControl(patientRawValue.caregiverName),
      caregiverRelation: new FormControl(patientRawValue.caregiverRelation),
      caregiverLandlinePhone: new FormControl(patientRawValue.caregiverLandlinePhone),
      caregiverMobilePhone: new FormControl(patientRawValue.caregiverMobilePhone),
      familyCode: new FormControl(patientRawValue.familyCode),
      additionalInfo: new FormControl(patientRawValue.additionalInfo),
      vaccinationsForBaby: new FormControl(patientRawValue.vaccinationsForBaby),
    });
  }

  getPatient(form: PatientFormGroup): IPatient | NewPatient {
    return form.getRawValue() as IPatient | NewPatient;
  }

  resetForm(form: PatientFormGroup, patient: PatientFormGroupInput): void {
    const patientRawValue = { ...this.getFormDefaults(), ...patient };
    form.reset(
      {
        ...patientRawValue,
        id: { value: patientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PatientFormDefaults {
    return {
      id: null,
    };
  }
}
