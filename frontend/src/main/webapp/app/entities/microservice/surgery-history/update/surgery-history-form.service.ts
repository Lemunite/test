import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISurgeryHistory, NewSurgeryHistory } from '../surgery-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISurgeryHistory for edit and NewSurgeryHistoryFormGroupInput for create.
 */
type SurgeryHistoryFormGroupInput = ISurgeryHistory | PartialWithRequiredKeyOf<NewSurgeryHistory>;

type SurgeryHistoryFormDefaults = Pick<NewSurgeryHistory, 'id'>;

type SurgeryHistoryFormGroupContent = {
  id: FormControl<ISurgeryHistory['id'] | NewSurgeryHistory['id']>;
  bodyPart: FormControl<ISurgeryHistory['bodyPart']>;
  surgeryYear: FormControl<ISurgeryHistory['surgeryYear']>;
  note: FormControl<ISurgeryHistory['note']>;
  patient: FormControl<ISurgeryHistory['patient']>;
};

export type SurgeryHistoryFormGroup = FormGroup<SurgeryHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SurgeryHistoryFormService {
  createSurgeryHistoryFormGroup(surgeryHistory: SurgeryHistoryFormGroupInput = { id: null }): SurgeryHistoryFormGroup {
    const surgeryHistoryRawValue = {
      ...this.getFormDefaults(),
      ...surgeryHistory,
    };
    return new FormGroup<SurgeryHistoryFormGroupContent>({
      id: new FormControl(
        { value: surgeryHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bodyPart: new FormControl(surgeryHistoryRawValue.bodyPart),
      surgeryYear: new FormControl(surgeryHistoryRawValue.surgeryYear),
      note: new FormControl(surgeryHistoryRawValue.note),
      patient: new FormControl(surgeryHistoryRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getSurgeryHistory(form: SurgeryHistoryFormGroup): ISurgeryHistory | NewSurgeryHistory {
    return form.getRawValue() as ISurgeryHistory | NewSurgeryHistory;
  }

  resetForm(form: SurgeryHistoryFormGroup, surgeryHistory: SurgeryHistoryFormGroupInput): void {
    const surgeryHistoryRawValue = { ...this.getFormDefaults(), ...surgeryHistory };
    form.reset(
      {
        ...surgeryHistoryRawValue,
        id: { value: surgeryHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SurgeryHistoryFormDefaults {
    return {
      id: null,
    };
  }
}
