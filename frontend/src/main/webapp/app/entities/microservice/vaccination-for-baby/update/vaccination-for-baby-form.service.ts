import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVaccinationForBaby, NewVaccinationForBaby } from '../vaccination-for-baby.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVaccinationForBaby for edit and NewVaccinationForBabyFormGroupInput for create.
 */
type VaccinationForBabyFormGroupInput = IVaccinationForBaby | PartialWithRequiredKeyOf<NewVaccinationForBaby>;

type VaccinationForBabyFormDefaults = Pick<NewVaccinationForBaby, 'id'>;

type VaccinationForBabyFormGroupContent = {
  id: FormControl<IVaccinationForBaby['id'] | NewVaccinationForBaby['id']>;
  vaccine: FormControl<IVaccinationForBaby['vaccine']>;
  numberUse: FormControl<IVaccinationForBaby['numberUse']>;
};

export type VaccinationForBabyFormGroup = FormGroup<VaccinationForBabyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VaccinationForBabyFormService {
  createVaccinationForBabyFormGroup(vaccinationForBaby: VaccinationForBabyFormGroupInput = { id: null }): VaccinationForBabyFormGroup {
    const vaccinationForBabyRawValue = {
      ...this.getFormDefaults(),
      ...vaccinationForBaby,
    };
    return new FormGroup<VaccinationForBabyFormGroupContent>({
      id: new FormControl(
        { value: vaccinationForBabyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vaccine: new FormControl(vaccinationForBabyRawValue.vaccine),
      numberUse: new FormControl(vaccinationForBabyRawValue.numberUse),
    });
  }

  getVaccinationForBaby(form: VaccinationForBabyFormGroup): IVaccinationForBaby | NewVaccinationForBaby {
    return form.getRawValue() as IVaccinationForBaby | NewVaccinationForBaby;
  }

  resetForm(form: VaccinationForBabyFormGroup, vaccinationForBaby: VaccinationForBabyFormGroupInput): void {
    const vaccinationForBabyRawValue = { ...this.getFormDefaults(), ...vaccinationForBaby };
    form.reset(
      {
        ...vaccinationForBabyRawValue,
        id: { value: vaccinationForBabyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VaccinationForBabyFormDefaults {
    return {
      id: null,
    };
  }
}
