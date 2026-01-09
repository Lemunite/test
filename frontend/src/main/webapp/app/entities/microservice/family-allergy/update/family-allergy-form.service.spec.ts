import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../family-allergy.test-samples';

import { FamilyAllergyFormService } from './family-allergy-form.service';

describe('FamilyAllergy Form Service', () => {
  let service: FamilyAllergyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FamilyAllergyFormService);
  });

  describe('Service methods', () => {
    describe('createFamilyAllergyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFamilyAllergyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            affectedPerson: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing IFamilyAllergy should create a new form with FormGroup', () => {
        const formGroup = service.createFamilyAllergyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            affectedPerson: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getFamilyAllergy', () => {
      it('should return NewFamilyAllergy for default FamilyAllergy initial value', () => {
        const formGroup = service.createFamilyAllergyFormGroup(sampleWithNewData);

        const familyAllergy = service.getFamilyAllergy(formGroup) as any;

        expect(familyAllergy).toMatchObject(sampleWithNewData);
      });

      it('should return NewFamilyAllergy for empty FamilyAllergy initial value', () => {
        const formGroup = service.createFamilyAllergyFormGroup();

        const familyAllergy = service.getFamilyAllergy(formGroup) as any;

        expect(familyAllergy).toMatchObject({});
      });

      it('should return IFamilyAllergy', () => {
        const formGroup = service.createFamilyAllergyFormGroup(sampleWithRequiredData);

        const familyAllergy = service.getFamilyAllergy(formGroup) as any;

        expect(familyAllergy).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFamilyAllergy should not enable id FormControl', () => {
        const formGroup = service.createFamilyAllergyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFamilyAllergy should disable id FormControl', () => {
        const formGroup = service.createFamilyAllergyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
