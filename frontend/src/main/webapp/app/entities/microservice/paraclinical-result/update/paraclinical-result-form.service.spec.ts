import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../paraclinical-result.test-samples';

import { ParaclinicalResultFormService } from './paraclinical-result-form.service';

describe('ParaclinicalResult Form Service', () => {
  let service: ParaclinicalResultFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParaclinicalResultFormService);
  });

  describe('Service methods', () => {
    describe('createParaclinicalResultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParaclinicalResultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testName: expect.any(Object),
            result: expect.any(Object),
            resultDate: expect.any(Object),
            medicalRecord: expect.any(Object),
          }),
        );
      });

      it('passing IParaclinicalResult should create a new form with FormGroup', () => {
        const formGroup = service.createParaclinicalResultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testName: expect.any(Object),
            result: expect.any(Object),
            resultDate: expect.any(Object),
            medicalRecord: expect.any(Object),
          }),
        );
      });
    });

    describe('getParaclinicalResult', () => {
      it('should return NewParaclinicalResult for default ParaclinicalResult initial value', () => {
        const formGroup = service.createParaclinicalResultFormGroup(sampleWithNewData);

        const paraclinicalResult = service.getParaclinicalResult(formGroup) as any;

        expect(paraclinicalResult).toMatchObject(sampleWithNewData);
      });

      it('should return NewParaclinicalResult for empty ParaclinicalResult initial value', () => {
        const formGroup = service.createParaclinicalResultFormGroup();

        const paraclinicalResult = service.getParaclinicalResult(formGroup) as any;

        expect(paraclinicalResult).toMatchObject({});
      });

      it('should return IParaclinicalResult', () => {
        const formGroup = service.createParaclinicalResultFormGroup(sampleWithRequiredData);

        const paraclinicalResult = service.getParaclinicalResult(formGroup) as any;

        expect(paraclinicalResult).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParaclinicalResult should not enable id FormControl', () => {
        const formGroup = service.createParaclinicalResultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParaclinicalResult should disable id FormControl', () => {
        const formGroup = service.createParaclinicalResultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
