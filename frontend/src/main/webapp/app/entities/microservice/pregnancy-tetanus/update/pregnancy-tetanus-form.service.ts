import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPregnancyTetanus, NewPregnancyTetanus } from '../pregnancy-tetanus.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPregnancyTetanus for edit and NewPregnancyTetanusFormGroupInput for create.
 */
type PregnancyTetanusFormGroupInput = IPregnancyTetanus | PartialWithRequiredKeyOf<NewPregnancyTetanus>;

type PregnancyTetanusFormDefaults = Pick<NewPregnancyTetanus, 'id' | 'notInjected'>;

type PregnancyTetanusFormGroupContent = {
  id: FormControl<IPregnancyTetanus['id'] | NewPregnancyTetanus['id']>;
  dose: FormControl<IPregnancyTetanus['dose']>;
  notInjected: FormControl<IPregnancyTetanus['notInjected']>;
  injectionDate: FormControl<IPregnancyTetanus['injectionDate']>;
  pregnancyMonth: FormControl<IPregnancyTetanus['pregnancyMonth']>;
  reaction: FormControl<IPregnancyTetanus['reaction']>;
  nextAppointment: FormControl<IPregnancyTetanus['nextAppointment']>;
  numberOfDosesReceived: FormControl<IPregnancyTetanus['numberOfDosesReceived']>;
  patient: FormControl<IPregnancyTetanus['patient']>;
};

export type PregnancyTetanusFormGroup = FormGroup<PregnancyTetanusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PregnancyTetanusFormService {
  createPregnancyTetanusFormGroup(pregnancyTetanus: PregnancyTetanusFormGroupInput = { id: null }): PregnancyTetanusFormGroup {
    const pregnancyTetanusRawValue = {
      ...this.getFormDefaults(),
      ...pregnancyTetanus,
    };
    return new FormGroup<PregnancyTetanusFormGroupContent>({
      id: new FormControl(
        { value: pregnancyTetanusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dose: new FormControl(pregnancyTetanusRawValue.dose, {
        validators: [Validators.required],
      }),
      notInjected: new FormControl(pregnancyTetanusRawValue.notInjected),
      injectionDate: new FormControl(pregnancyTetanusRawValue.injectionDate),
      pregnancyMonth: new FormControl(pregnancyTetanusRawValue.pregnancyMonth),
      reaction: new FormControl(pregnancyTetanusRawValue.reaction),
      nextAppointment: new FormControl(pregnancyTetanusRawValue.nextAppointment),
      numberOfDosesReceived: new FormControl(pregnancyTetanusRawValue.numberOfDosesReceived),
      patient: new FormControl(pregnancyTetanusRawValue.patient),
    });
  }

  getPregnancyTetanus(form: PregnancyTetanusFormGroup): IPregnancyTetanus | NewPregnancyTetanus {
    return form.getRawValue() as IPregnancyTetanus | NewPregnancyTetanus;
  }

  resetForm(form: PregnancyTetanusFormGroup, pregnancyTetanus: PregnancyTetanusFormGroupInput): void {
    const pregnancyTetanusRawValue = { ...this.getFormDefaults(), ...pregnancyTetanus };
    form.reset(
      {
        ...pregnancyTetanusRawValue,
        id: { value: pregnancyTetanusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PregnancyTetanusFormDefaults {
    return {
      id: null,
      notInjected: false,
    };
  }
}
