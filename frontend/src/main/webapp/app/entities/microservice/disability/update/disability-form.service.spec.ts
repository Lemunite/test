import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../disability.test-samples';

import { DisabilityFormService } from './disability-form.service';

describe('Disability Form Service', () => {
  let service: DisabilityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisabilityFormService);
  });

  describe('Service methods', () => {
    describe('createDisabilityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDisabilityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing IDisability should create a new form with FormGroup', () => {
        const formGroup = service.createDisabilityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisability', () => {
      it('should return NewDisability for default Disability initial value', () => {
        const formGroup = service.createDisabilityFormGroup(sampleWithNewData);

        const disability = service.getDisability(formGroup) as any;

        expect(disability).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisability for empty Disability initial value', () => {
        const formGroup = service.createDisabilityFormGroup();

        const disability = service.getDisability(formGroup) as any;

        expect(disability).toMatchObject({});
      });

      it('should return IDisability', () => {
        const formGroup = service.createDisabilityFormGroup(sampleWithRequiredData);

        const disability = service.getDisability(formGroup) as any;

        expect(disability).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisability should not enable id FormControl', () => {
        const formGroup = service.createDisabilityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisability should disable id FormControl', () => {
        const formGroup = service.createDisabilityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
