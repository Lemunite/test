import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdditionalInformation, NewAdditionalInformation } from '../additional-information.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdditionalInformation for edit and NewAdditionalInformationFormGroupInput for create.
 */
type AdditionalInformationFormGroupInput = IAdditionalInformation | PartialWithRequiredKeyOf<NewAdditionalInformation>;

type AdditionalInformationFormDefaults = Pick<
  NewAdditionalInformation,
  | 'id'
  | 'cardiovascularDisease'
  | 'hypertension'
  | 'diabetes'
  | 'stomachDisease'
  | 'chronicLungDisease'
  | 'asthma'
  | 'goiter'
  | 'hepatitis'
  | 'congenitalHeartDisease'
  | 'mentalDisorders'
  | 'autism'
  | 'epilepsy'
>;

type AdditionalInformationFormGroupContent = {
  id: FormControl<IAdditionalInformation['id'] | NewAdditionalInformation['id']>;
  smoking: FormControl<IAdditionalInformation['smoking']>;
  alcoholRiskLevel: FormControl<IAdditionalInformation['alcoholRiskLevel']>;
  alcoholGlassesPerDay: FormControl<IAdditionalInformation['alcoholGlassesPerDay']>;
  drugUse: FormControl<IAdditionalInformation['drugUse']>;
  physicalActivity: FormControl<IAdditionalInformation['physicalActivity']>;
  exposureFactor: FormControl<IAdditionalInformation['exposureFactor']>;
  exposureDate: FormControl<IAdditionalInformation['exposureDate']>;
  typeToilet: FormControl<IAdditionalInformation['typeToilet']>;
  environmentalRisk: FormControl<IAdditionalInformation['environmentalRisk']>;
  cardiovascularDisease: FormControl<IAdditionalInformation['cardiovascularDisease']>;
  hypertension: FormControl<IAdditionalInformation['hypertension']>;
  diabetes: FormControl<IAdditionalInformation['diabetes']>;
  stomachDisease: FormControl<IAdditionalInformation['stomachDisease']>;
  chronicLungDisease: FormControl<IAdditionalInformation['chronicLungDisease']>;
  asthma: FormControl<IAdditionalInformation['asthma']>;
  goiter: FormControl<IAdditionalInformation['goiter']>;
  hepatitis: FormControl<IAdditionalInformation['hepatitis']>;
  congenitalHeartDisease: FormControl<IAdditionalInformation['congenitalHeartDisease']>;
  mentalDisorders: FormControl<IAdditionalInformation['mentalDisorders']>;
  autism: FormControl<IAdditionalInformation['autism']>;
  epilepsy: FormControl<IAdditionalInformation['epilepsy']>;
  cancer: FormControl<IAdditionalInformation['cancer']>;
  tuberculosis: FormControl<IAdditionalInformation['tuberculosis']>;
  otherDiseases: FormControl<IAdditionalInformation['otherDiseases']>;
  contraceptiveMethod: FormControl<IAdditionalInformation['contraceptiveMethod']>;
  lastPregnancy: FormControl<IAdditionalInformation['lastPregnancy']>;
  numberOfPregnancies: FormControl<IAdditionalInformation['numberOfPregnancies']>;
  numberOfMiscarriages: FormControl<IAdditionalInformation['numberOfMiscarriages']>;
  numberOfAbortions: FormControl<IAdditionalInformation['numberOfAbortions']>;
  numberOfBirths: FormControl<IAdditionalInformation['numberOfBirths']>;
  vaginalDelivery: FormControl<IAdditionalInformation['vaginalDelivery']>;
  cesareanSection: FormControl<IAdditionalInformation['cesareanSection']>;
  difficultDelivery: FormControl<IAdditionalInformation['difficultDelivery']>;
  numberOfFullTermBirths: FormControl<IAdditionalInformation['numberOfFullTermBirths']>;
  numberOfPrematureBirths: FormControl<IAdditionalInformation['numberOfPrematureBirths']>;
  numberOfChildrenAlive: FormControl<IAdditionalInformation['numberOfChildrenAlive']>;
  gynecologicalDiseases: FormControl<IAdditionalInformation['gynecologicalDiseases']>;
  birthStatus: FormControl<IAdditionalInformation['birthStatus']>;
  birthWeight: FormControl<IAdditionalInformation['birthWeight']>;
  birthHeight: FormControl<IAdditionalInformation['birthHeight']>;
  birthDefectNote: FormControl<IAdditionalInformation['birthDefectNote']>;
  otherBirthNote: FormControl<IAdditionalInformation['otherBirthNote']>;
  otherHealthNote: FormControl<IAdditionalInformation['otherHealthNote']>;
};

export type AdditionalInformationFormGroup = FormGroup<AdditionalInformationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdditionalInformationFormService {
  createAdditionalInformationFormGroup(
    additionalInformation: AdditionalInformationFormGroupInput = { id: null },
  ): AdditionalInformationFormGroup {
    const additionalInformationRawValue = {
      ...this.getFormDefaults(),
      ...additionalInformation,
    };
    return new FormGroup<AdditionalInformationFormGroupContent>({
      id: new FormControl(
        { value: additionalInformationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      smoking: new FormControl(additionalInformationRawValue.smoking),
      alcoholRiskLevel: new FormControl(additionalInformationRawValue.alcoholRiskLevel),
      alcoholGlassesPerDay: new FormControl(additionalInformationRawValue.alcoholGlassesPerDay),
      drugUse: new FormControl(additionalInformationRawValue.drugUse),
      physicalActivity: new FormControl(additionalInformationRawValue.physicalActivity),
      exposureFactor: new FormControl(additionalInformationRawValue.exposureFactor),
      exposureDate: new FormControl(additionalInformationRawValue.exposureDate),
      typeToilet: new FormControl(additionalInformationRawValue.typeToilet),
      environmentalRisk: new FormControl(additionalInformationRawValue.environmentalRisk),
      cardiovascularDisease: new FormControl(additionalInformationRawValue.cardiovascularDisease),
      hypertension: new FormControl(additionalInformationRawValue.hypertension),
      diabetes: new FormControl(additionalInformationRawValue.diabetes),
      stomachDisease: new FormControl(additionalInformationRawValue.stomachDisease),
      chronicLungDisease: new FormControl(additionalInformationRawValue.chronicLungDisease),
      asthma: new FormControl(additionalInformationRawValue.asthma),
      goiter: new FormControl(additionalInformationRawValue.goiter),
      hepatitis: new FormControl(additionalInformationRawValue.hepatitis),
      congenitalHeartDisease: new FormControl(additionalInformationRawValue.congenitalHeartDisease),
      mentalDisorders: new FormControl(additionalInformationRawValue.mentalDisorders),
      autism: new FormControl(additionalInformationRawValue.autism),
      epilepsy: new FormControl(additionalInformationRawValue.epilepsy),
      cancer: new FormControl(additionalInformationRawValue.cancer),
      tuberculosis: new FormControl(additionalInformationRawValue.tuberculosis),
      otherDiseases: new FormControl(additionalInformationRawValue.otherDiseases),
      contraceptiveMethod: new FormControl(additionalInformationRawValue.contraceptiveMethod),
      lastPregnancy: new FormControl(additionalInformationRawValue.lastPregnancy),
      numberOfPregnancies: new FormControl(additionalInformationRawValue.numberOfPregnancies),
      numberOfMiscarriages: new FormControl(additionalInformationRawValue.numberOfMiscarriages),
      numberOfAbortions: new FormControl(additionalInformationRawValue.numberOfAbortions),
      numberOfBirths: new FormControl(additionalInformationRawValue.numberOfBirths),
      vaginalDelivery: new FormControl(additionalInformationRawValue.vaginalDelivery),
      cesareanSection: new FormControl(additionalInformationRawValue.cesareanSection),
      difficultDelivery: new FormControl(additionalInformationRawValue.difficultDelivery),
      numberOfFullTermBirths: new FormControl(additionalInformationRawValue.numberOfFullTermBirths),
      numberOfPrematureBirths: new FormControl(additionalInformationRawValue.numberOfPrematureBirths),
      numberOfChildrenAlive: new FormControl(additionalInformationRawValue.numberOfChildrenAlive),
      gynecologicalDiseases: new FormControl(additionalInformationRawValue.gynecologicalDiseases),
      birthStatus: new FormControl(additionalInformationRawValue.birthStatus, {
        validators: [Validators.required],
      }),
      birthWeight: new FormControl(additionalInformationRawValue.birthWeight),
      birthHeight: new FormControl(additionalInformationRawValue.birthHeight),
      birthDefectNote: new FormControl(additionalInformationRawValue.birthDefectNote),
      otherBirthNote: new FormControl(additionalInformationRawValue.otherBirthNote),
      otherHealthNote: new FormControl(additionalInformationRawValue.otherHealthNote),
    });
  }

  getAdditionalInformation(form: AdditionalInformationFormGroup): IAdditionalInformation | NewAdditionalInformation {
    return form.getRawValue() as IAdditionalInformation | NewAdditionalInformation;
  }

  resetForm(form: AdditionalInformationFormGroup, additionalInformation: AdditionalInformationFormGroupInput): void {
    const additionalInformationRawValue = { ...this.getFormDefaults(), ...additionalInformation };
    form.reset(
      {
        ...additionalInformationRawValue,
        id: { value: additionalInformationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdditionalInformationFormDefaults {
    return {
      id: null,
      cardiovascularDisease: false,
      hypertension: false,
      diabetes: false,
      stomachDisease: false,
      chronicLungDisease: false,
      asthma: false,
      goiter: false,
      hepatitis: false,
      congenitalHeartDisease: false,
      mentalDisorders: false,
      autism: false,
      epilepsy: false,
    };
  }
}
