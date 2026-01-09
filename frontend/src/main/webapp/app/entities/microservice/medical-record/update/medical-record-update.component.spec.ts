import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOrganExamination } from 'app/entities/microservice/organ-examination/organ-examination.model';
import { OrganExaminationService } from 'app/entities/microservice/organ-examination/service/organ-examination.service';
import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordService } from '../service/medical-record.service';
import { MedicalRecordFormService } from './medical-record-form.service';

import { MedicalRecordUpdateComponent } from './medical-record-update.component';

describe('MedicalRecord Management Update Component', () => {
  let comp: MedicalRecordUpdateComponent;
  let fixture: ComponentFixture<MedicalRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalRecordFormService: MedicalRecordFormService;
  let medicalRecordService: MedicalRecordService;
  let organExaminationService: OrganExaminationService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicalRecordUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MedicalRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalRecordFormService = TestBed.inject(MedicalRecordFormService);
    medicalRecordService = TestBed.inject(MedicalRecordService);
    organExaminationService = TestBed.inject(OrganExaminationService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call organExamination query and add missing value', () => {
      const medicalRecord: IMedicalRecord = { id: 9576 };
      const organExamination: IOrganExamination = { id: 8565 };
      medicalRecord.organExamination = organExamination;

      const organExaminationCollection: IOrganExamination[] = [{ id: 8565 }];
      jest.spyOn(organExaminationService, 'query').mockReturnValue(of(new HttpResponse({ body: organExaminationCollection })));
      const expectedCollection: IOrganExamination[] = [organExamination, ...organExaminationCollection];
      jest.spyOn(organExaminationService, 'addOrganExaminationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      expect(organExaminationService.query).toHaveBeenCalled();
      expect(organExaminationService.addOrganExaminationToCollectionIfMissing).toHaveBeenCalledWith(
        organExaminationCollection,
        organExamination,
      );
      expect(comp.organExaminationsCollection).toEqual(expectedCollection);
    });

    it('should call Patient query and add missing value', () => {
      const medicalRecord: IMedicalRecord = { id: 9576 };
      const patient: IPatient = { id: 16668 };
      medicalRecord.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const medicalRecord: IMedicalRecord = { id: 9576 };
      const organExamination: IOrganExamination = { id: 8565 };
      medicalRecord.organExamination = organExamination;
      const patient: IPatient = { id: 16668 };
      medicalRecord.patient = patient;

      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      expect(comp.organExaminationsCollection).toContainEqual(organExamination);
      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.medicalRecord).toEqual(medicalRecord);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 17381 };
      jest.spyOn(medicalRecordFormService, 'getMedicalRecord').mockReturnValue(medicalRecord);
      jest.spyOn(medicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalRecord }));
      saveSubject.complete();

      // THEN
      expect(medicalRecordFormService.getMedicalRecord).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalRecordService.update).toHaveBeenCalledWith(expect.objectContaining(medicalRecord));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 17381 };
      jest.spyOn(medicalRecordFormService, 'getMedicalRecord').mockReturnValue({ id: null });
      jest.spyOn(medicalRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalRecord }));
      saveSubject.complete();

      // THEN
      expect(medicalRecordFormService.getMedicalRecord).toHaveBeenCalled();
      expect(medicalRecordService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 17381 };
      jest.spyOn(medicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalRecordService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrganExamination', () => {
      it('should forward to organExaminationService', () => {
        const entity = { id: 8565 };
        const entity2 = { id: 28596 };
        jest.spyOn(organExaminationService, 'compareOrganExamination');
        comp.compareOrganExamination(entity, entity2);
        expect(organExaminationService.compareOrganExamination).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePatient', () => {
      it('should forward to patientService', () => {
        const entity = { id: 16668 };
        const entity2 = { id: 16914 };
        jest.spyOn(patientService, 'comparePatient');
        comp.comparePatient(entity, entity2);
        expect(patientService.comparePatient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
