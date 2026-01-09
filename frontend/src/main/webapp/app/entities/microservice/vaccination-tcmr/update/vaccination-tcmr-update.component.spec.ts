import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { VaccinationTCMRService } from '../service/vaccination-tcmr.service';
import { IVaccinationTCMR } from '../vaccination-tcmr.model';
import { VaccinationTCMRFormService } from './vaccination-tcmr-form.service';

import { VaccinationTCMRUpdateComponent } from './vaccination-tcmr-update.component';

describe('VaccinationTCMR Management Update Component', () => {
  let comp: VaccinationTCMRUpdateComponent;
  let fixture: ComponentFixture<VaccinationTCMRUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaccinationTCMRFormService: VaccinationTCMRFormService;
  let vaccinationTCMRService: VaccinationTCMRService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VaccinationTCMRUpdateComponent],
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
      .overrideTemplate(VaccinationTCMRUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaccinationTCMRUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaccinationTCMRFormService = TestBed.inject(VaccinationTCMRFormService);
    vaccinationTCMRService = TestBed.inject(VaccinationTCMRService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const vaccinationTCMR: IVaccinationTCMR = { id: 2095 };
      const patient: IPatient = { id: 16668 };
      vaccinationTCMR.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vaccinationTCMR });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const vaccinationTCMR: IVaccinationTCMR = { id: 2095 };
      const patient: IPatient = { id: 16668 };
      vaccinationTCMR.patient = patient;

      activatedRoute.data = of({ vaccinationTCMR });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.vaccinationTCMR).toEqual(vaccinationTCMR);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationTCMR>>();
      const vaccinationTCMR = { id: 32463 };
      jest.spyOn(vaccinationTCMRFormService, 'getVaccinationTCMR').mockReturnValue(vaccinationTCMR);
      jest.spyOn(vaccinationTCMRService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationTCMR });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccinationTCMR }));
      saveSubject.complete();

      // THEN
      expect(vaccinationTCMRFormService.getVaccinationTCMR).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaccinationTCMRService.update).toHaveBeenCalledWith(expect.objectContaining(vaccinationTCMR));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationTCMR>>();
      const vaccinationTCMR = { id: 32463 };
      jest.spyOn(vaccinationTCMRFormService, 'getVaccinationTCMR').mockReturnValue({ id: null });
      jest.spyOn(vaccinationTCMRService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationTCMR: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccinationTCMR }));
      saveSubject.complete();

      // THEN
      expect(vaccinationTCMRFormService.getVaccinationTCMR).toHaveBeenCalled();
      expect(vaccinationTCMRService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationTCMR>>();
      const vaccinationTCMR = { id: 32463 };
      jest.spyOn(vaccinationTCMRService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationTCMR });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaccinationTCMRService.update).toHaveBeenCalled();
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
