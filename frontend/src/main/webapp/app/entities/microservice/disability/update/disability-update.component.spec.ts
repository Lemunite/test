import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { DisabilityService } from '../service/disability.service';
import { IDisability } from '../disability.model';
import { DisabilityFormService } from './disability-form.service';

import { DisabilityUpdateComponent } from './disability-update.component';

describe('Disability Management Update Component', () => {
  let comp: DisabilityUpdateComponent;
  let fixture: ComponentFixture<DisabilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disabilityFormService: DisabilityFormService;
  let disabilityService: DisabilityService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DisabilityUpdateComponent],
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
      .overrideTemplate(DisabilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisabilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disabilityFormService = TestBed.inject(DisabilityFormService);
    disabilityService = TestBed.inject(DisabilityService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const disability: IDisability = { id: 11805 };
      const patient: IPatient = { id: 16668 };
      disability.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disability });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const disability: IDisability = { id: 11805 };
      const patient: IPatient = { id: 16668 };
      disability.patient = patient;

      activatedRoute.data = of({ disability });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.disability).toEqual(disability);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisability>>();
      const disability = { id: 10283 };
      jest.spyOn(disabilityFormService, 'getDisability').mockReturnValue(disability);
      jest.spyOn(disabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disability }));
      saveSubject.complete();

      // THEN
      expect(disabilityFormService.getDisability).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(disabilityService.update).toHaveBeenCalledWith(expect.objectContaining(disability));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisability>>();
      const disability = { id: 10283 };
      jest.spyOn(disabilityFormService, 'getDisability').mockReturnValue({ id: null });
      jest.spyOn(disabilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disability: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disability }));
      saveSubject.complete();

      // THEN
      expect(disabilityFormService.getDisability).toHaveBeenCalled();
      expect(disabilityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisability>>();
      const disability = { id: 10283 };
      jest.spyOn(disabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disabilityService.update).toHaveBeenCalled();
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
