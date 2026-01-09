import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../additional-information.test-samples';

import { AdditionalInformationFormService } from './additional-information-form.service';

describe('AdditionalInformation Form Service', () => {
  let service: AdditionalInformationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdditionalInformationFormService);
  });

  describe('Service methods', () => {
    describe('createAdditionalInformationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdditionalInformationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            smoking: expect.any(Object),
            alcoholRiskLevel: expect.any(Object),
            alcoholGlassesPerDay: expect.any(Object),
            drugUse: expect.any(Object),
            physicalActivity: expect.any(Object),
            exposureFactor: expect.any(Object),
            exposureDate: expect.any(Object),
            typeToilet: expect.any(Object),
            environmentalRisk: expect.any(Object),
            cardiovascularDisease: expect.any(Object),
            hypertension: expect.any(Object),
            diabetes: expect.any(Object),
            stomachDisease: expect.any(Object),
            chronicLungDisease: expect.any(Object),
            asthma: expect.any(Object),
            goiter: expect.any(Object),
            hepatitis: expect.any(Object),
            congenitalHeartDisease: expect.any(Object),
            mentalDisorders: expect.any(Object),
            autism: expect.any(Object),
            epilepsy: expect.any(Object),
            cancer: expect.any(Object),
            tuberculosis: expect.any(Object),
            otherDiseases: expect.any(Object),
            contraceptiveMethod: expect.any(Object),
            lastPregnancy: expect.any(Object),
            numberOfPregnancies: expect.any(Object),
            numberOfMiscarriages: expect.any(Object),
            numberOfAbortions: expect.any(Object),
            numberOfBirths: expect.any(Object),
            vaginalDelivery: expect.any(Object),
            cesareanSection: expect.any(Object),
            difficultDelivery: expect.any(Object),
            numberOfFullTermBirths: expect.any(Object),
            numberOfPrematureBirths: expect.any(Object),
            numberOfChildrenAlive: expect.any(Object),
            gynecologicalDiseases: expect.any(Object),
            birthStatus: expect.any(Object),
            birthWeight: expect.any(Object),
            birthHeight: expect.any(Object),
            birthDefectNote: expect.any(Object),
            otherBirthNote: expect.any(Object),
            otherHealthNote: expect.any(Object),
          }),
        );
      });

      it('passing IAdditionalInformation should create a new form with FormGroup', () => {
        const formGroup = service.createAdditionalInformationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            smoking: expect.any(Object),
            alcoholRiskLevel: expect.any(Object),
            alcoholGlassesPerDay: expect.any(Object),
            drugUse: expect.any(Object),
            physicalActivity: expect.any(Object),
            exposureFactor: expect.any(Object),
            exposureDate: expect.any(Object),
            typeToilet: expect.any(Object),
            environmentalRisk: expect.any(Object),
            cardiovascularDisease: expect.any(Object),
            hypertension: expect.any(Object),
            diabetes: expect.any(Object),
            stomachDisease: expect.any(Object),
            chronicLungDisease: expect.any(Object),
            asthma: expect.any(Object),
            goiter: expect.any(Object),
            hepatitis: expect.any(Object),
            congenitalHeartDisease: expect.any(Object),
            mentalDisorders: expect.any(Object),
            autism: expect.any(Object),
            epilepsy: expect.any(Object),
            cancer: expect.any(Object),
            tuberculosis: expect.any(Object),
            otherDiseases: expect.any(Object),
            contraceptiveMethod: expect.any(Object),
            lastPregnancy: expect.any(Object),
            numberOfPregnancies: expect.any(Object),
            numberOfMiscarriages: expect.any(Object),
            numberOfAbortions: expect.any(Object),
            numberOfBirths: expect.any(Object),
            vaginalDelivery: expect.any(Object),
            cesareanSection: expect.any(Object),
            difficultDelivery: expect.any(Object),
            numberOfFullTermBirths: expect.any(Object),
            numberOfPrematureBirths: expect.any(Object),
            numberOfChildrenAlive: expect.any(Object),
            gynecologicalDiseases: expect.any(Object),
            birthStatus: expect.any(Object),
            birthWeight: expect.any(Object),
            birthHeight: expect.any(Object),
            birthDefectNote: expect.any(Object),
            otherBirthNote: expect.any(Object),
            otherHealthNote: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdditionalInformation', () => {
      it('should return NewAdditionalInformation for default AdditionalInformation initial value', () => {
        const formGroup = service.createAdditionalInformationFormGroup(sampleWithNewData);

        const additionalInformation = service.getAdditionalInformation(formGroup) as any;

        expect(additionalInformation).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdditionalInformation for empty AdditionalInformation initial value', () => {
        const formGroup = service.createAdditionalInformationFormGroup();

        const additionalInformation = service.getAdditionalInformation(formGroup) as any;

        expect(additionalInformation).toMatchObject({});
      });

      it('should return IAdditionalInformation', () => {
        const formGroup = service.createAdditionalInformationFormGroup(sampleWithRequiredData);

        const additionalInformation = service.getAdditionalInformation(formGroup) as any;

        expect(additionalInformation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdditionalInformation should not enable id FormControl', () => {
        const formGroup = service.createAdditionalInformationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdditionalInformation should disable id FormControl', () => {
        const formGroup = service.createAdditionalInformationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
