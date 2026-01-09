import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vaccine.test-samples';

import { VaccineFormService } from './vaccine-form.service';

describe('Vaccine Form Service', () => {
  let service: VaccineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VaccineFormService);
  });

  describe('Service methods', () => {
    describe('createVaccineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVaccineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            notVaccinated: expect.any(Object),
            injectionDate: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            vaccinationForBaby: expect.any(Object),
          }),
        );
      });

      it('passing IVaccine should create a new form with FormGroup', () => {
        const formGroup = service.createVaccineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            notVaccinated: expect.any(Object),
            injectionDate: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            vaccinationForBaby: expect.any(Object),
          }),
        );
      });
    });

    describe('getVaccine', () => {
      it('should return NewVaccine for default Vaccine initial value', () => {
        const formGroup = service.createVaccineFormGroup(sampleWithNewData);

        const vaccine = service.getVaccine(formGroup) as any;

        expect(vaccine).toMatchObject(sampleWithNewData);
      });

      it('should return NewVaccine for empty Vaccine initial value', () => {
        const formGroup = service.createVaccineFormGroup();

        const vaccine = service.getVaccine(formGroup) as any;

        expect(vaccine).toMatchObject({});
      });

      it('should return IVaccine', () => {
        const formGroup = service.createVaccineFormGroup(sampleWithRequiredData);

        const vaccine = service.getVaccine(formGroup) as any;

        expect(vaccine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVaccine should not enable id FormControl', () => {
        const formGroup = service.createVaccineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVaccine should disable id FormControl', () => {
        const formGroup = service.createVaccineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
