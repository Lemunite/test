import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedicalRecord, NewMedicalRecord } from '../medical-record.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalRecord for edit and NewMedicalRecordFormGroupInput for create.
 */
type MedicalRecordFormGroupInput = IMedicalRecord | PartialWithRequiredKeyOf<NewMedicalRecord>;

type MedicalRecordFormDefaults = Pick<NewMedicalRecord, 'id'>;

type MedicalRecordFormGroupContent = {
  id: FormControl<IMedicalRecord['id'] | NewMedicalRecord['id']>;
  examinationDate: FormControl<IMedicalRecord['examinationDate']>;
  medicalHistory: FormControl<IMedicalRecord['medicalHistory']>;
  leftEyeNoGlass: FormControl<IMedicalRecord['leftEyeNoGlass']>;
  rightEyeNoGlass: FormControl<IMedicalRecord['rightEyeNoGlass']>;
  leftEyeWithGlass: FormControl<IMedicalRecord['leftEyeWithGlass']>;
  rightEyeWithGlass: FormControl<IMedicalRecord['rightEyeWithGlass']>;
  pulse: FormControl<IMedicalRecord['pulse']>;
  temperature: FormControl<IMedicalRecord['temperature']>;
  bloodPressure: FormControl<IMedicalRecord['bloodPressure']>;
  respiratoryRate: FormControl<IMedicalRecord['respiratoryRate']>;
  weight: FormControl<IMedicalRecord['weight']>;
  height: FormControl<IMedicalRecord['height']>;
  bmi: FormControl<IMedicalRecord['bmi']>;
  waist: FormControl<IMedicalRecord['waist']>;
  skinMucosa: FormControl<IMedicalRecord['skinMucosa']>;
  other: FormControl<IMedicalRecord['other']>;
  diseaseName: FormControl<IMedicalRecord['diseaseName']>;
  diseaseCode: FormControl<IMedicalRecord['diseaseCode']>;
  advice: FormControl<IMedicalRecord['advice']>;
  docterName: FormControl<IMedicalRecord['docterName']>;
  organExamination: FormControl<IMedicalRecord['organExamination']>;
  patient: FormControl<IMedicalRecord['patient']>;
};

export type MedicalRecordFormGroup = FormGroup<MedicalRecordFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalRecordFormService {
  createMedicalRecordFormGroup(medicalRecord: MedicalRecordFormGroupInput = { id: null }): MedicalRecordFormGroup {
    const medicalRecordRawValue = {
      ...this.getFormDefaults(),
      ...medicalRecord,
    };
    return new FormGroup<MedicalRecordFormGroupContent>({
      id: new FormControl(
        { value: medicalRecordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      examinationDate: new FormControl(medicalRecordRawValue.examinationDate, {
        validators: [Validators.required],
      }),
      medicalHistory: new FormControl(medicalRecordRawValue.medicalHistory),
      leftEyeNoGlass: new FormControl(medicalRecordRawValue.leftEyeNoGlass),
      rightEyeNoGlass: new FormControl(medicalRecordRawValue.rightEyeNoGlass),
      leftEyeWithGlass: new FormControl(medicalRecordRawValue.leftEyeWithGlass),
      rightEyeWithGlass: new FormControl(medicalRecordRawValue.rightEyeWithGlass),
      pulse: new FormControl(medicalRecordRawValue.pulse),
      temperature: new FormControl(medicalRecordRawValue.temperature),
      bloodPressure: new FormControl(medicalRecordRawValue.bloodPressure),
      respiratoryRate: new FormControl(medicalRecordRawValue.respiratoryRate),
      weight: new FormControl(medicalRecordRawValue.weight),
      height: new FormControl(medicalRecordRawValue.height),
      bmi: new FormControl(medicalRecordRawValue.bmi),
      waist: new FormControl(medicalRecordRawValue.waist),
      skinMucosa: new FormControl(medicalRecordRawValue.skinMucosa),
      other: new FormControl(medicalRecordRawValue.other),
      diseaseName: new FormControl(medicalRecordRawValue.diseaseName, {
        validators: [Validators.required],
      }),
      diseaseCode: new FormControl(medicalRecordRawValue.diseaseCode),
      advice: new FormControl(medicalRecordRawValue.advice),
      docterName: new FormControl(medicalRecordRawValue.docterName, {
        validators: [Validators.required],
      }),
      organExamination: new FormControl(medicalRecordRawValue.organExamination),
      patient: new FormControl(medicalRecordRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicalRecord(form: MedicalRecordFormGroup): IMedicalRecord | NewMedicalRecord {
    return form.getRawValue() as IMedicalRecord | NewMedicalRecord;
  }

  resetForm(form: MedicalRecordFormGroup, medicalRecord: MedicalRecordFormGroupInput): void {
    const medicalRecordRawValue = { ...this.getFormDefaults(), ...medicalRecord };
    form.reset(
      {
        ...medicalRecordRawValue,
        id: { value: medicalRecordRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalRecordFormDefaults {
    return {
      id: null,
    };
  }
}
