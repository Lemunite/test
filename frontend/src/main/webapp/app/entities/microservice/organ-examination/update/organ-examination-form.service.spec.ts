import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../organ-examination.test-samples';

import { OrganExaminationFormService } from './organ-examination-form.service';

describe('OrganExamination Form Service', () => {
  let service: OrganExaminationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganExaminationFormService);
  });

  describe('Service methods', () => {
    describe('createOrganExaminationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganExaminationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cardiovascular: expect.any(Object),
            respiratory: expect.any(Object),
            digestive: expect.any(Object),
            urinary: expect.any(Object),
            musculoskeletal: expect.any(Object),
            endocrine: expect.any(Object),
            neurological: expect.any(Object),
            psychiatric: expect.any(Object),
            surgery: expect.any(Object),
            obstetricsAndGynecology: expect.any(Object),
            otolaryngology: expect.any(Object),
            dentistryAndMaxillofacialSurgery: expect.any(Object),
            eye: expect.any(Object),
            dermatology: expect.any(Object),
            nutrition: expect.any(Object),
            exercise: expect.any(Object),
            other: expect.any(Object),
            developmentAssessment: expect.any(Object),
          }),
        );
      });

      it('passing IOrganExamination should create a new form with FormGroup', () => {
        const formGroup = service.createOrganExaminationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cardiovascular: expect.any(Object),
            respiratory: expect.any(Object),
            digestive: expect.any(Object),
            urinary: expect.any(Object),
            musculoskeletal: expect.any(Object),
            endocrine: expect.any(Object),
            neurological: expect.any(Object),
            psychiatric: expect.any(Object),
            surgery: expect.any(Object),
            obstetricsAndGynecology: expect.any(Object),
            otolaryngology: expect.any(Object),
            dentistryAndMaxillofacialSurgery: expect.any(Object),
            eye: expect.any(Object),
            dermatology: expect.any(Object),
            nutrition: expect.any(Object),
            exercise: expect.any(Object),
            other: expect.any(Object),
            developmentAssessment: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganExamination', () => {
      it('should return NewOrganExamination for default OrganExamination initial value', () => {
        const formGroup = service.createOrganExaminationFormGroup(sampleWithNewData);

        const organExamination = service.getOrganExamination(formGroup) as any;

        expect(organExamination).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganExamination for empty OrganExamination initial value', () => {
        const formGroup = service.createOrganExaminationFormGroup();

        const organExamination = service.getOrganExamination(formGroup) as any;

        expect(organExamination).toMatchObject({});
      });

      it('should return IOrganExamination', () => {
        const formGroup = service.createOrganExaminationFormGroup(sampleWithRequiredData);

        const organExamination = service.getOrganExamination(formGroup) as any;

        expect(organExamination).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganExamination should not enable id FormControl', () => {
        const formGroup = service.createOrganExaminationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganExamination should disable id FormControl', () => {
        const formGroup = service.createOrganExaminationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
