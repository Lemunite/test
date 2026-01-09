import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { PregnancyTetanusService } from '../service/pregnancy-tetanus.service';
import { IPregnancyTetanus } from '../pregnancy-tetanus.model';
import { PregnancyTetanusFormService } from './pregnancy-tetanus-form.service';

import { PregnancyTetanusUpdateComponent } from './pregnancy-tetanus-update.component';

describe('PregnancyTetanus Management Update Component', () => {
  let comp: PregnancyTetanusUpdateComponent;
  let fixture: ComponentFixture<PregnancyTetanusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pregnancyTetanusFormService: PregnancyTetanusFormService;
  let pregnancyTetanusService: PregnancyTetanusService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PregnancyTetanusUpdateComponent],
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
      .overrideTemplate(PregnancyTetanusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PregnancyTetanusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pregnancyTetanusFormService = TestBed.inject(PregnancyTetanusFormService);
    pregnancyTetanusService = TestBed.inject(PregnancyTetanusService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const pregnancyTetanus: IPregnancyTetanus = { id: 27406 };
      const patient: IPatient = { id: 16668 };
      pregnancyTetanus.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pregnancyTetanus });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const pregnancyTetanus: IPregnancyTetanus = { id: 27406 };
      const patient: IPatient = { id: 16668 };
      pregnancyTetanus.patient = patient;

      activatedRoute.data = of({ pregnancyTetanus });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.pregnancyTetanus).toEqual(pregnancyTetanus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPregnancyTetanus>>();
      const pregnancyTetanus = { id: 5738 };
      jest.spyOn(pregnancyTetanusFormService, 'getPregnancyTetanus').mockReturnValue(pregnancyTetanus);
      jest.spyOn(pregnancyTetanusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pregnancyTetanus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pregnancyTetanus }));
      saveSubject.complete();

      // THEN
      expect(pregnancyTetanusFormService.getPregnancyTetanus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pregnancyTetanusService.update).toHaveBeenCalledWith(expect.objectContaining(pregnancyTetanus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPregnancyTetanus>>();
      const pregnancyTetanus = { id: 5738 };
      jest.spyOn(pregnancyTetanusFormService, 'getPregnancyTetanus').mockReturnValue({ id: null });
      jest.spyOn(pregnancyTetanusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pregnancyTetanus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pregnancyTetanus }));
      saveSubject.complete();

      // THEN
      expect(pregnancyTetanusFormService.getPregnancyTetanus).toHaveBeenCalled();
      expect(pregnancyTetanusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPregnancyTetanus>>();
      const pregnancyTetanus = { id: 5738 };
      jest.spyOn(pregnancyTetanusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pregnancyTetanus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pregnancyTetanusService.update).toHaveBeenCalled();
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
