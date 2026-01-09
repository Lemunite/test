import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVaccine, NewVaccine } from '../vaccine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVaccine for edit and NewVaccineFormGroupInput for create.
 */
type VaccineFormGroupInput = IVaccine | PartialWithRequiredKeyOf<NewVaccine>;

type VaccineFormDefaults = Pick<NewVaccine, 'id' | 'notVaccinated'>;

type VaccineFormGroupContent = {
  id: FormControl<IVaccine['id'] | NewVaccine['id']>;
  name: FormControl<IVaccine['name']>;
  notVaccinated: FormControl<IVaccine['notVaccinated']>;
  injectionDate: FormControl<IVaccine['injectionDate']>;
  reaction: FormControl<IVaccine['reaction']>;
  nextAppointment: FormControl<IVaccine['nextAppointment']>;
  vaccinationForBaby: FormControl<IVaccine['vaccinationForBaby']>;
};

export type VaccineFormGroup = FormGroup<VaccineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VaccineFormService {
  createVaccineFormGroup(vaccine: VaccineFormGroupInput = { id: null }): VaccineFormGroup {
    const vaccineRawValue = {
      ...this.getFormDefaults(),
      ...vaccine,
    };
    return new FormGroup<VaccineFormGroupContent>({
      id: new FormControl(
        { value: vaccineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(vaccineRawValue.name, {
        validators: [Validators.required],
      }),
      notVaccinated: new FormControl(vaccineRawValue.notVaccinated),
      injectionDate: new FormControl(vaccineRawValue.injectionDate),
      reaction: new FormControl(vaccineRawValue.reaction),
      nextAppointment: new FormControl(vaccineRawValue.nextAppointment),
      vaccinationForBaby: new FormControl(vaccineRawValue.vaccinationForBaby, {
        validators: [Validators.required],
      }),
    });
  }

  getVaccine(form: VaccineFormGroup): IVaccine | NewVaccine {
    return form.getRawValue() as IVaccine | NewVaccine;
  }

  resetForm(form: VaccineFormGroup, vaccine: VaccineFormGroupInput): void {
    const vaccineRawValue = { ...this.getFormDefaults(), ...vaccine };
    form.reset(
      {
        ...vaccineRawValue,
        id: { value: vaccineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VaccineFormDefaults {
    return {
      id: null,
      notVaccinated: false,
    };
  }
}
