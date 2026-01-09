import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFamilyDisease, NewFamilyDisease } from '../family-disease.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFamilyDisease for edit and NewFamilyDiseaseFormGroupInput for create.
 */
type FamilyDiseaseFormGroupInput = IFamilyDisease | PartialWithRequiredKeyOf<NewFamilyDisease>;

type FamilyDiseaseFormDefaults = Pick<NewFamilyDisease, 'id'>;

type FamilyDiseaseFormGroupContent = {
  id: FormControl<IFamilyDisease['id'] | NewFamilyDisease['id']>;
  affectedPerson: FormControl<IFamilyDisease['affectedPerson']>;
  relationshipToPatient: FormControl<IFamilyDisease['relationshipToPatient']>;
  patient: FormControl<IFamilyDisease['patient']>;
};

export type FamilyDiseaseFormGroup = FormGroup<FamilyDiseaseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FamilyDiseaseFormService {
  createFamilyDiseaseFormGroup(familyDisease: FamilyDiseaseFormGroupInput = { id: null }): FamilyDiseaseFormGroup {
    const familyDiseaseRawValue = {
      ...this.getFormDefaults(),
      ...familyDisease,
    };
    return new FormGroup<FamilyDiseaseFormGroupContent>({
      id: new FormControl(
        { value: familyDiseaseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      affectedPerson: new FormControl(familyDiseaseRawValue.affectedPerson, {
        validators: [Validators.required],
      }),
      relationshipToPatient: new FormControl(familyDiseaseRawValue.relationshipToPatient),
      patient: new FormControl(familyDiseaseRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getFamilyDisease(form: FamilyDiseaseFormGroup): IFamilyDisease | NewFamilyDisease {
    return form.getRawValue() as IFamilyDisease | NewFamilyDisease;
  }

  resetForm(form: FamilyDiseaseFormGroup, familyDisease: FamilyDiseaseFormGroupInput): void {
    const familyDiseaseRawValue = { ...this.getFormDefaults(), ...familyDisease };
    form.reset(
      {
        ...familyDiseaseRawValue,
        id: { value: familyDiseaseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FamilyDiseaseFormDefaults {
    return {
      id: null,
    };
  }
}
