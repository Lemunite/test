import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../family-disease.test-samples';

import { FamilyDiseaseFormService } from './family-disease-form.service';

describe('FamilyDisease Form Service', () => {
  let service: FamilyDiseaseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FamilyDiseaseFormService);
  });

  describe('Service methods', () => {
    describe('createFamilyDiseaseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFamilyDiseaseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            affectedPerson: expect.any(Object),
            relationshipToPatient: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing IFamilyDisease should create a new form with FormGroup', () => {
        const formGroup = service.createFamilyDiseaseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            affectedPerson: expect.any(Object),
            relationshipToPatient: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getFamilyDisease', () => {
      it('should return NewFamilyDisease for default FamilyDisease initial value', () => {
        const formGroup = service.createFamilyDiseaseFormGroup(sampleWithNewData);

        const familyDisease = service.getFamilyDisease(formGroup) as any;

        expect(familyDisease).toMatchObject(sampleWithNewData);
      });

      it('should return NewFamilyDisease for empty FamilyDisease initial value', () => {
        const formGroup = service.createFamilyDiseaseFormGroup();

        const familyDisease = service.getFamilyDisease(formGroup) as any;

        expect(familyDisease).toMatchObject({});
      });

      it('should return IFamilyDisease', () => {
        const formGroup = service.createFamilyDiseaseFormGroup(sampleWithRequiredData);

        const familyDisease = service.getFamilyDisease(formGroup) as any;

        expect(familyDisease).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFamilyDisease should not enable id FormControl', () => {
        const formGroup = service.createFamilyDiseaseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFamilyDisease should disable id FormControl', () => {
        const formGroup = service.createFamilyDiseaseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
