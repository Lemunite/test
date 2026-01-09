import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pregnancy-tetanus.test-samples';

import { PregnancyTetanusFormService } from './pregnancy-tetanus-form.service';

describe('PregnancyTetanus Form Service', () => {
  let service: PregnancyTetanusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PregnancyTetanusFormService);
  });

  describe('Service methods', () => {
    describe('createPregnancyTetanusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPregnancyTetanusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dose: expect.any(Object),
            notInjected: expect.any(Object),
            injectionDate: expect.any(Object),
            pregnancyMonth: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            numberOfDosesReceived: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing IPregnancyTetanus should create a new form with FormGroup', () => {
        const formGroup = service.createPregnancyTetanusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dose: expect.any(Object),
            notInjected: expect.any(Object),
            injectionDate: expect.any(Object),
            pregnancyMonth: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            numberOfDosesReceived: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getPregnancyTetanus', () => {
      it('should return NewPregnancyTetanus for default PregnancyTetanus initial value', () => {
        const formGroup = service.createPregnancyTetanusFormGroup(sampleWithNewData);

        const pregnancyTetanus = service.getPregnancyTetanus(formGroup) as any;

        expect(pregnancyTetanus).toMatchObject(sampleWithNewData);
      });

      it('should return NewPregnancyTetanus for empty PregnancyTetanus initial value', () => {
        const formGroup = service.createPregnancyTetanusFormGroup();

        const pregnancyTetanus = service.getPregnancyTetanus(formGroup) as any;

        expect(pregnancyTetanus).toMatchObject({});
      });

      it('should return IPregnancyTetanus', () => {
        const formGroup = service.createPregnancyTetanusFormGroup(sampleWithRequiredData);

        const pregnancyTetanus = service.getPregnancyTetanus(formGroup) as any;

        expect(pregnancyTetanus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPregnancyTetanus should not enable id FormControl', () => {
        const formGroup = service.createPregnancyTetanusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPregnancyTetanus should disable id FormControl', () => {
        const formGroup = service.createPregnancyTetanusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
