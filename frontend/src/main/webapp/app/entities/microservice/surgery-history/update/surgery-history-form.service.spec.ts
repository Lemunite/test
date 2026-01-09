import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../surgery-history.test-samples';

import { SurgeryHistoryFormService } from './surgery-history-form.service';

describe('SurgeryHistory Form Service', () => {
  let service: SurgeryHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SurgeryHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createSurgeryHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSurgeryHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bodyPart: expect.any(Object),
            surgeryYear: expect.any(Object),
            note: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });

      it('passing ISurgeryHistory should create a new form with FormGroup', () => {
        const formGroup = service.createSurgeryHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bodyPart: expect.any(Object),
            surgeryYear: expect.any(Object),
            note: expect.any(Object),
            patient: expect.any(Object),
          }),
        );
      });
    });

    describe('getSurgeryHistory', () => {
      it('should return NewSurgeryHistory for default SurgeryHistory initial value', () => {
        const formGroup = service.createSurgeryHistoryFormGroup(sampleWithNewData);

        const surgeryHistory = service.getSurgeryHistory(formGroup) as any;

        expect(surgeryHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewSurgeryHistory for empty SurgeryHistory initial value', () => {
        const formGroup = service.createSurgeryHistoryFormGroup();

        const surgeryHistory = service.getSurgeryHistory(formGroup) as any;

        expect(surgeryHistory).toMatchObject({});
      });

      it('should return ISurgeryHistory', () => {
        const formGroup = service.createSurgeryHistoryFormGroup(sampleWithRequiredData);

        const surgeryHistory = service.getSurgeryHistory(formGroup) as any;

        expect(surgeryHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISurgeryHistory should not enable id FormControl', () => {
        const formGroup = service.createSurgeryHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSurgeryHistory should disable id FormControl', () => {
        const formGroup = service.createSurgeryHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
