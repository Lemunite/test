import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFamilyAllergy, NewFamilyAllergy } from '../family-allergy.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFamilyAllergy for edit and NewFamilyAllergyFormGroupInput for create.
 */
type FamilyAllergyFormGroupInput = IFamilyAllergy | PartialWithRequiredKeyOf<NewFamilyAllergy>;

type FamilyAllergyFormDefaults = Pick<NewFamilyAllergy, 'id'>;

type FamilyAllergyFormGroupContent = {
  id: FormControl<IFamilyAllergy['id'] | NewFamilyAllergy['id']>;
  type: FormControl<IFamilyAllergy['type']>;
  description: FormControl<IFamilyAllergy['description']>;
  affectedPerson: FormControl<IFamilyAllergy['affectedPerson']>;
  patient: FormControl<IFamilyAllergy['patient']>;
};

export type FamilyAllergyFormGroup = FormGroup<FamilyAllergyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FamilyAllergyFormService {
  createFamilyAllergyFormGroup(familyAllergy: FamilyAllergyFormGroupInput = { id: null }): FamilyAllergyFormGroup {
    const familyAllergyRawValue = {
      ...this.getFormDefaults(),
      ...familyAllergy,
    };
    return new FormGroup<FamilyAllergyFormGroupContent>({
      id: new FormControl(
        { value: familyAllergyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(familyAllergyRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(familyAllergyRawValue.description),
      affectedPerson: new FormControl(familyAllergyRawValue.affectedPerson, {
        validators: [Validators.required],
      }),
      patient: new FormControl(familyAllergyRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getFamilyAllergy(form: FamilyAllergyFormGroup): IFamilyAllergy | NewFamilyAllergy {
    return form.getRawValue() as IFamilyAllergy | NewFamilyAllergy;
  }

  resetForm(form: FamilyAllergyFormGroup, familyAllergy: FamilyAllergyFormGroupInput): void {
    const familyAllergyRawValue = { ...this.getFormDefaults(), ...familyAllergy };
    form.reset(
      {
        ...familyAllergyRawValue,
        id: { value: familyAllergyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FamilyAllergyFormDefaults {
    return {
      id: null,
    };
  }
}
