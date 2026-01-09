import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAllergy, NewAllergy } from '../allergy.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAllergy for edit and NewAllergyFormGroupInput for create.
 */
type AllergyFormGroupInput = IAllergy | PartialWithRequiredKeyOf<NewAllergy>;

type AllergyFormDefaults = Pick<NewAllergy, 'id'>;

type AllergyFormGroupContent = {
  id: FormControl<IAllergy['id'] | NewAllergy['id']>;
  type: FormControl<IAllergy['type']>;
  description: FormControl<IAllergy['description']>;
  patient: FormControl<IAllergy['patient']>;
};

export type AllergyFormGroup = FormGroup<AllergyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AllergyFormService {
  createAllergyFormGroup(allergy: AllergyFormGroupInput = { id: null }): AllergyFormGroup {
    const allergyRawValue = {
      ...this.getFormDefaults(),
      ...allergy,
    };
    return new FormGroup<AllergyFormGroupContent>({
      id: new FormControl(
        { value: allergyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(allergyRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(allergyRawValue.description),
      patient: new FormControl(allergyRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getAllergy(form: AllergyFormGroup): IAllergy | NewAllergy {
    return form.getRawValue() as IAllergy | NewAllergy;
  }

  resetForm(form: AllergyFormGroup, allergy: AllergyFormGroupInput): void {
    const allergyRawValue = { ...this.getFormDefaults(), ...allergy };
    form.reset(
      {
        ...allergyRawValue,
        id: { value: allergyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AllergyFormDefaults {
    return {
      id: null,
    };
  }
}
