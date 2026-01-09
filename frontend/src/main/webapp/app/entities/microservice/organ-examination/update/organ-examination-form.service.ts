import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrganExamination, NewOrganExamination } from '../organ-examination.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganExamination for edit and NewOrganExaminationFormGroupInput for create.
 */
type OrganExaminationFormGroupInput = IOrganExamination | PartialWithRequiredKeyOf<NewOrganExamination>;

type OrganExaminationFormDefaults = Pick<NewOrganExamination, 'id'>;

type OrganExaminationFormGroupContent = {
  id: FormControl<IOrganExamination['id'] | NewOrganExamination['id']>;
  cardiovascular: FormControl<IOrganExamination['cardiovascular']>;
  respiratory: FormControl<IOrganExamination['respiratory']>;
  digestive: FormControl<IOrganExamination['digestive']>;
  urinary: FormControl<IOrganExamination['urinary']>;
  musculoskeletal: FormControl<IOrganExamination['musculoskeletal']>;
  endocrine: FormControl<IOrganExamination['endocrine']>;
  neurological: FormControl<IOrganExamination['neurological']>;
  psychiatric: FormControl<IOrganExamination['psychiatric']>;
  surgery: FormControl<IOrganExamination['surgery']>;
  obstetricsAndGynecology: FormControl<IOrganExamination['obstetricsAndGynecology']>;
  otolaryngology: FormControl<IOrganExamination['otolaryngology']>;
  dentistryAndMaxillofacialSurgery: FormControl<IOrganExamination['dentistryAndMaxillofacialSurgery']>;
  eye: FormControl<IOrganExamination['eye']>;
  dermatology: FormControl<IOrganExamination['dermatology']>;
  nutrition: FormControl<IOrganExamination['nutrition']>;
  exercise: FormControl<IOrganExamination['exercise']>;
  other: FormControl<IOrganExamination['other']>;
  developmentAssessment: FormControl<IOrganExamination['developmentAssessment']>;
};

export type OrganExaminationFormGroup = FormGroup<OrganExaminationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganExaminationFormService {
  createOrganExaminationFormGroup(organExamination: OrganExaminationFormGroupInput = { id: null }): OrganExaminationFormGroup {
    const organExaminationRawValue = {
      ...this.getFormDefaults(),
      ...organExamination,
    };
    return new FormGroup<OrganExaminationFormGroupContent>({
      id: new FormControl(
        { value: organExaminationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cardiovascular: new FormControl(organExaminationRawValue.cardiovascular),
      respiratory: new FormControl(organExaminationRawValue.respiratory),
      digestive: new FormControl(organExaminationRawValue.digestive),
      urinary: new FormControl(organExaminationRawValue.urinary),
      musculoskeletal: new FormControl(organExaminationRawValue.musculoskeletal),
      endocrine: new FormControl(organExaminationRawValue.endocrine),
      neurological: new FormControl(organExaminationRawValue.neurological),
      psychiatric: new FormControl(organExaminationRawValue.psychiatric),
      surgery: new FormControl(organExaminationRawValue.surgery),
      obstetricsAndGynecology: new FormControl(organExaminationRawValue.obstetricsAndGynecology),
      otolaryngology: new FormControl(organExaminationRawValue.otolaryngology),
      dentistryAndMaxillofacialSurgery: new FormControl(organExaminationRawValue.dentistryAndMaxillofacialSurgery),
      eye: new FormControl(organExaminationRawValue.eye),
      dermatology: new FormControl(organExaminationRawValue.dermatology),
      nutrition: new FormControl(organExaminationRawValue.nutrition),
      exercise: new FormControl(organExaminationRawValue.exercise),
      other: new FormControl(organExaminationRawValue.other),
      developmentAssessment: new FormControl(organExaminationRawValue.developmentAssessment),
    });
  }

  getOrganExamination(form: OrganExaminationFormGroup): IOrganExamination | NewOrganExamination {
    return form.getRawValue() as IOrganExamination | NewOrganExamination;
  }

  resetForm(form: OrganExaminationFormGroup, organExamination: OrganExaminationFormGroupInput): void {
    const organExaminationRawValue = { ...this.getFormDefaults(), ...organExamination };
    form.reset(
      {
        ...organExaminationRawValue,
        id: { value: organExaminationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganExaminationFormDefaults {
    return {
      id: null,
    };
  }
}
