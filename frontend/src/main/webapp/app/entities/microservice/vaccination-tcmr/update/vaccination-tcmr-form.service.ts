import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVaccinationTCMR, NewVaccinationTCMR } from '../vaccination-tcmr.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVaccinationTCMR for edit and NewVaccinationTCMRFormGroupInput for create.
 */
type VaccinationTCMRFormGroupInput = IVaccinationTCMR | PartialWithRequiredKeyOf<NewVaccinationTCMR>;

type VaccinationTCMRFormDefaults = Pick<NewVaccinationTCMR, 'id' | 'notVaccinated' | 'vaccinated'>;

type VaccinationTCMRFormGroupContent = {
  id: FormControl<IVaccinationTCMR['id'] | NewVaccinationTCMR['id']>;
  vaccine: FormControl<IVaccinationTCMR['vaccine']>;
  notVaccinated: FormControl<IVaccinationTCMR['notVaccinated']>;
  vaccinated: FormControl<IVaccinationTCMR['vaccinated']>;
  injectionDate: FormControl<IVaccinationTCMR['injectionDate']>;
  reaction: FormControl<IVaccinationTCMR['reaction']>;
  nextAppointment: FormControl<IVaccinationTCMR['nextAppointment']>;
  patient: FormControl<IVaccinationTCMR['patient']>;
};

export type VaccinationTCMRFormGroup = FormGroup<VaccinationTCMRFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VaccinationTCMRFormService {
  createVaccinationTCMRFormGroup(vaccinationTCMR: VaccinationTCMRFormGroupInput = { id: null }): VaccinationTCMRFormGroup {
    const vaccinationTCMRRawValue = {
      ...this.getFormDefaults(),
      ...vaccinationTCMR,
    };
    return new FormGroup<VaccinationTCMRFormGroupContent>({
      id: new FormControl(
        { value: vaccinationTCMRRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vaccine: new FormControl(vaccinationTCMRRawValue.vaccine, {
        validators: [Validators.required],
      }),
      notVaccinated: new FormControl(vaccinationTCMRRawValue.notVaccinated),
      vaccinated: new FormControl(vaccinationTCMRRawValue.vaccinated),
      injectionDate: new FormControl(vaccinationTCMRRawValue.injectionDate),
      reaction: new FormControl(vaccinationTCMRRawValue.reaction),
      nextAppointment: new FormControl(vaccinationTCMRRawValue.nextAppointment),
      patient: new FormControl(vaccinationTCMRRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getVaccinationTCMR(form: VaccinationTCMRFormGroup): IVaccinationTCMR | NewVaccinationTCMR {
    return form.getRawValue() as IVaccinationTCMR | NewVaccinationTCMR;
  }

  resetForm(form: VaccinationTCMRFormGroup, vaccinationTCMR: VaccinationTCMRFormGroupInput): void {
    const vaccinationTCMRRawValue = { ...this.getFormDefaults(), ...vaccinationTCMR };
    form.reset(
      {
        ...vaccinationTCMRRawValue,
        id: { value: vaccinationTCMRRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VaccinationTCMRFormDefaults {
    return {
      id: null,
      notVaccinated: false,
      vaccinated: false,
    };
  }
}
