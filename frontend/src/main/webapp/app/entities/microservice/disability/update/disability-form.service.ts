import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDisability, NewDisability } from '../disability.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDisability for edit and NewDisabilityFormGroupInput for create.
 */
type DisabilityFormGroupInput = IDisability | PartialWithRequiredKeyOf<NewDisability>;

type DisabilityFormDefaults = Pick<NewDisability, 'id'>;

type DisabilityFormGroupContent = {
  id: FormControl<IDisability['id'] | NewDisability['id']>;
  type: FormControl<IDisability['type']>;
  description: FormControl<IDisability['description']>;
  patient: FormControl<IDisability['patient']>;
};

export type DisabilityFormGroup = FormGroup<DisabilityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DisabilityFormService {
  createDisabilityFormGroup(disability: DisabilityFormGroupInput = { id: null }): DisabilityFormGroup {
    const disabilityRawValue = {
      ...this.getFormDefaults(),
      ...disability,
    };
    return new FormGroup<DisabilityFormGroupContent>({
      id: new FormControl(
        { value: disabilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(disabilityRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(disabilityRawValue.description),
      patient: new FormControl(disabilityRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getDisability(form: DisabilityFormGroup): IDisability | NewDisability {
    return form.getRawValue() as IDisability | NewDisability;
  }

  resetForm(form: DisabilityFormGroup, disability: DisabilityFormGroupInput): void {
    const disabilityRawValue = { ...this.getFormDefaults(), ...disability };
    form.reset(
      {
        ...disabilityRawValue,
        id: { value: disabilityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DisabilityFormDefaults {
    return {
      id: null,
    };
  }
}
