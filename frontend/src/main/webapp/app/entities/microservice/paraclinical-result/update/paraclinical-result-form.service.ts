import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IParaclinicalResult, NewParaclinicalResult } from '../paraclinical-result.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParaclinicalResult for edit and NewParaclinicalResultFormGroupInput for create.
 */
type ParaclinicalResultFormGroupInput = IParaclinicalResult | PartialWithRequiredKeyOf<NewParaclinicalResult>;

type ParaclinicalResultFormDefaults = Pick<NewParaclinicalResult, 'id'>;

type ParaclinicalResultFormGroupContent = {
  id: FormControl<IParaclinicalResult['id'] | NewParaclinicalResult['id']>;
  testName: FormControl<IParaclinicalResult['testName']>;
  result: FormControl<IParaclinicalResult['result']>;
  resultDate: FormControl<IParaclinicalResult['resultDate']>;
  medicalRecord: FormControl<IParaclinicalResult['medicalRecord']>;
};

export type ParaclinicalResultFormGroup = FormGroup<ParaclinicalResultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParaclinicalResultFormService {
  createParaclinicalResultFormGroup(paraclinicalResult: ParaclinicalResultFormGroupInput = { id: null }): ParaclinicalResultFormGroup {
    const paraclinicalResultRawValue = {
      ...this.getFormDefaults(),
      ...paraclinicalResult,
    };
    return new FormGroup<ParaclinicalResultFormGroupContent>({
      id: new FormControl(
        { value: paraclinicalResultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      testName: new FormControl(paraclinicalResultRawValue.testName, {
        validators: [Validators.required],
      }),
      result: new FormControl(paraclinicalResultRawValue.result),
      resultDate: new FormControl(paraclinicalResultRawValue.resultDate),
      medicalRecord: new FormControl(paraclinicalResultRawValue.medicalRecord, {
        validators: [Validators.required],
      }),
    });
  }

  getParaclinicalResult(form: ParaclinicalResultFormGroup): IParaclinicalResult | NewParaclinicalResult {
    return form.getRawValue() as IParaclinicalResult | NewParaclinicalResult;
  }

  resetForm(form: ParaclinicalResultFormGroup, paraclinicalResult: ParaclinicalResultFormGroupInput): void {
    const paraclinicalResultRawValue = { ...this.getFormDefaults(), ...paraclinicalResult };
    form.reset(
      {
        ...paraclinicalResultRawValue,
        id: { value: paraclinicalResultRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParaclinicalResultFormDefaults {
    return {
      id: null,
    };
  }
}
