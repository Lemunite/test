import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vaccination-for-baby.test-samples';

import { VaccinationForBabyFormService } from './vaccination-for-baby-form.service';

describe('VaccinationForBaby Form Service', () => {
  let service: VaccinationForBabyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VaccinationForBabyFormService);
  });

  describe('Service methods', () => {
    describe('createVaccinationForBabyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVaccinationForBabyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vaccine: expect.any(Object),
            numberUse: expect.any(Object),
          }),
        );
      });

      it('passing IVaccinationForBaby should create a new form with FormGroup', () => {
        const formGroup = service.createVaccinationForBabyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vaccine: expect.any(Object),
            numberUse: expect.any(Object),
          }),
        );
      });
    });

    describe('getVaccinationForBaby', () => {
      it('should return NewVaccinationForBaby for default VaccinationForBaby initial value', () => {
        const formGroup = service.createVaccinationForBabyFormGroup(sampleWithNewData);

        const vaccinationForBaby = service.getVaccinationForBaby(formGroup) as any;

        expect(vaccinationForBaby).toMatchObject(sampleWithNewData);
      });

      it('should return NewVaccinationForBaby for empty VaccinationForBaby initial value', () => {
        const formGroup = service.createVaccinationForBabyFormGroup();

        const vaccinationForBaby = service.getVaccinationForBaby(formGroup) as any;

        expect(vaccinationForBaby).toMatchObject({});
      });

      it('should return IVaccinationForBaby', () => {
        const formGroup = service.createVaccinationForBabyFormGroup(sampleWithRequiredData);

        const vaccinationForBaby = service.getVaccinationForBaby(formGroup) as any;

        expect(vaccinationForBaby).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVaccinationForBaby should not enable id FormControl', () => {
        const formGroup = service.createVaccinationForBabyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVaccinationForBaby should disable id FormControl', () => {
        const formGroup = service.createVaccinationForBabyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
