import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../patient.test-samples';

import { PatientFormService } from './patient-form.service';

describe('Patient Form Service', () => {
  let service: PatientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatientFormService);
  });

  describe('Service methods', () => {
    describe('createPatientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPatientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            gender: expect.any(Object),
            dateOfBirth: expect.any(Object),
            placeOfBirth: expect.any(Object),
            bloodTypeAbo: expect.any(Object),
            bloodTypeRh: expect.any(Object),
            ethnic: expect.any(Object),
            nationality: expect.any(Object),
            religion: expect.any(Object),
            job: expect.any(Object),
            idNumber: expect.any(Object),
            idIssueDate: expect.any(Object),
            idIssuePlace: expect.any(Object),
            healthInsuranceNumber: expect.any(Object),
            permanentAddress: expect.any(Object),
            permanentWard: expect.any(Object),
            permanentDistrict: expect.any(Object),
            permanentProvince: expect.any(Object),
            currentAddress: expect.any(Object),
            currentWard: expect.any(Object),
            currentDistrict: expect.any(Object),
            currentProvince: expect.any(Object),
            landlinePhone: expect.any(Object),
            mobilePhone: expect.any(Object),
            email: expect.any(Object),
            motherName: expect.any(Object),
            fatherName: expect.any(Object),
            caregiverName: expect.any(Object),
            caregiverRelation: expect.any(Object),
            caregiverLandlinePhone: expect.any(Object),
            caregiverMobilePhone: expect.any(Object),
            familyCode: expect.any(Object),
            additionalInfo: expect.any(Object),
            vaccinationsForBaby: expect.any(Object),
          }),
        );
      });

      it('passing IPatient should create a new form with FormGroup', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            gender: expect.any(Object),
            dateOfBirth: expect.any(Object),
            placeOfBirth: expect.any(Object),
            bloodTypeAbo: expect.any(Object),
            bloodTypeRh: expect.any(Object),
            ethnic: expect.any(Object),
            nationality: expect.any(Object),
            religion: expect.any(Object),
            job: expect.any(Object),
            idNumber: expect.any(Object),
            idIssueDate: expect.any(Object),
            idIssuePlace: expect.any(Object),
            healthInsuranceNumber: expect.any(Object),
            permanentAddress: expect.any(Object),
            permanentWard: expect.any(Object),
            permanentDistrict: expect.any(Object),
            permanentProvince: expect.any(Object),
            currentAddress: expect.any(Object),
            currentWard: expect.any(Object),
            currentDistrict: expect.any(Object),
            currentProvince: expect.any(Object),
            landlinePhone: expect.any(Object),
            mobilePhone: expect.any(Object),
            email: expect.any(Object),
            motherName: expect.any(Object),
            fatherName: expect.any(Object),
            caregiverName: expect.any(Object),
            caregiverRelation: expect.any(Object),
            caregiverLandlinePhone: expect.any(Object),
            caregiverMobilePhone: expect.any(Object),
            familyCode: expect.any(Object),
            additionalInfo: expect.any(Object),
            vaccinationsForBaby: expect.any(Object),
          }),
        );
      });
    });

    describe('getPatient', () => {
      it('should return NewPatient for default Patient initial value', () => {
        const formGroup = service.createPatientFormGroup(sampleWithNewData);

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject(sampleWithNewData);
      });

      it('should return NewPatient for empty Patient initial value', () => {
        const formGroup = service.createPatientFormGroup();

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject({});
      });

      it('should return IPatient', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPatient should not enable id FormControl', () => {
        const formGroup = service.createPatientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPatient should disable id FormControl', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
