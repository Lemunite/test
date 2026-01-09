import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { AllergyService } from '../service/allergy.service';
import { IAllergy } from '../allergy.model';
import { AllergyFormService } from './allergy-form.service';

import { AllergyUpdateComponent } from './allergy-update.component';

describe('Allergy Management Update Component', () => {
  let comp: AllergyUpdateComponent;
  let fixture: ComponentFixture<AllergyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let allergyFormService: AllergyFormService;
  let allergyService: AllergyService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AllergyUpdateComponent],
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
      .overrideTemplate(AllergyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllergyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    allergyFormService = TestBed.inject(AllergyFormService);
    allergyService = TestBed.inject(AllergyService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const allergy: IAllergy = { id: 133 };
      const patient: IPatient = { id: 16668 };
      allergy.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const allergy: IAllergy = { id: 133 };
      const patient: IPatient = { id: 16668 };
      allergy.patient = patient;

      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.allergy).toEqual(allergy);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllergy>>();
      const allergy = { id: 12270 };
      jest.spyOn(allergyFormService, 'getAllergy').mockReturnValue(allergy);
      jest.spyOn(allergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allergy }));
      saveSubject.complete();

      // THEN
      expect(allergyFormService.getAllergy).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(allergyService.update).toHaveBeenCalledWith(expect.objectContaining(allergy));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllergy>>();
      const allergy = { id: 12270 };
      jest.spyOn(allergyFormService, 'getAllergy').mockReturnValue({ id: null });
      jest.spyOn(allergyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allergy }));
      saveSubject.complete();

      // THEN
      expect(allergyFormService.getAllergy).toHaveBeenCalled();
      expect(allergyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllergy>>();
      const allergy = { id: 12270 };
      jest.spyOn(allergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(allergyService.update).toHaveBeenCalled();
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
