import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vaccination-tcmr.test-samples';

import { VaccinationTCMRFormService } from './vaccination-tcmr-form.service';

describe('VaccinationTCMR Form Service', () => {
  let service: VaccinationTCMRFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VaccinationTCMRFormService);
  });

  describe('Service methods', () => {
    describe('createVaccinationTCMRFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVaccinationTCMRFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vaccine: expect.any(Object),
            notVaccinated: expect.any(Object),
            vaccinated: expect.any(Object),
            injectionDate: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing IVaccinationTCMR should create a new form with FormGroup', () => {
        const formGroup = service.createVaccinationTCMRFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vaccine: expect.any(Object),
            notVaccinated: expect.any(Object),
            vaccinated: expect.any(Object),
            injectionDate: expect.any(Object),
            reaction: expect.any(Object),
            nextAppointment: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getVaccinationTCMR', () => {
      it('should return NewVaccinationTCMR for default VaccinationTCMR initial value', () => {
        const formGroup = service.createVaccinationTCMRFormGroup(sampleWithNewData);

        const vaccinationTCMR = service.getVaccinationTCMR(formGroup) as any;

        expect(vaccinationTCMR).toMatchObject(sampleWithNewData);
      });

      it('should return NewVaccinationTCMR for empty VaccinationTCMR initial value', () => {
        const formGroup = service.createVaccinationTCMRFormGroup();

        const vaccinationTCMR = service.getVaccinationTCMR(formGroup) as any;

        expect(vaccinationTCMR).toMatchObject({});
      });

      it('should return IVaccinationTCMR', () => {
        const formGroup = service.createVaccinationTCMRFormGroup(sampleWithRequiredData);

        const vaccinationTCMR = service.getVaccinationTCMR(formGroup) as any;

        expect(vaccinationTCMR).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVaccinationTCMR should not enable id FormControl', () => {
        const formGroup = service.createVaccinationTCMRFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVaccinationTCMR should disable id FormControl', () => {
        const formGroup = service.createVaccinationTCMRFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
