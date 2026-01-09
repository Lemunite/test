import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { SurgeryHistoryService } from '../service/surgery-history.service';
import { ISurgeryHistory } from '../surgery-history.model';
import { SurgeryHistoryFormService } from './surgery-history-form.service';

import { SurgeryHistoryUpdateComponent } from './surgery-history-update.component';

describe('SurgeryHistory Management Update Component', () => {
  let comp: SurgeryHistoryUpdateComponent;
  let fixture: ComponentFixture<SurgeryHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let surgeryHistoryFormService: SurgeryHistoryFormService;
  let surgeryHistoryService: SurgeryHistoryService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SurgeryHistoryUpdateComponent],
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
      .overrideTemplate(SurgeryHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SurgeryHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    surgeryHistoryFormService = TestBed.inject(SurgeryHistoryFormService);
    surgeryHistoryService = TestBed.inject(SurgeryHistoryService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const surgeryHistory: ISurgeryHistory = { id: 18610 };
      const patient: IPatient = { id: 16668 };
      surgeryHistory.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ surgeryHistory });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const surgeryHistory: ISurgeryHistory = { id: 18610 };
      const patient: IPatient = { id: 16668 };
      surgeryHistory.patient = patient;

      activatedRoute.data = of({ surgeryHistory });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.surgeryHistory).toEqual(surgeryHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurgeryHistory>>();
      const surgeryHistory = { id: 2530 };
      jest.spyOn(surgeryHistoryFormService, 'getSurgeryHistory').mockReturnValue(surgeryHistory);
      jest.spyOn(surgeryHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surgeryHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surgeryHistory }));
      saveSubject.complete();

      // THEN
      expect(surgeryHistoryFormService.getSurgeryHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(surgeryHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(surgeryHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurgeryHistory>>();
      const surgeryHistory = { id: 2530 };
      jest.spyOn(surgeryHistoryFormService, 'getSurgeryHistory').mockReturnValue({ id: null });
      jest.spyOn(surgeryHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surgeryHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surgeryHistory }));
      saveSubject.complete();

      // THEN
      expect(surgeryHistoryFormService.getSurgeryHistory).toHaveBeenCalled();
      expect(surgeryHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurgeryHistory>>();
      const surgeryHistory = { id: 2530 };
      jest.spyOn(surgeryHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surgeryHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(surgeryHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
