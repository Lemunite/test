import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { FamilyAllergyService } from '../service/family-allergy.service';
import { IFamilyAllergy } from '../family-allergy.model';
import { FamilyAllergyFormService } from './family-allergy-form.service';

import { FamilyAllergyUpdateComponent } from './family-allergy-update.component';

describe('FamilyAllergy Management Update Component', () => {
  let comp: FamilyAllergyUpdateComponent;
  let fixture: ComponentFixture<FamilyAllergyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familyAllergyFormService: FamilyAllergyFormService;
  let familyAllergyService: FamilyAllergyService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FamilyAllergyUpdateComponent],
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
      .overrideTemplate(FamilyAllergyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilyAllergyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familyAllergyFormService = TestBed.inject(FamilyAllergyFormService);
    familyAllergyService = TestBed.inject(FamilyAllergyService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const familyAllergy: IFamilyAllergy = { id: 11017 };
      const patient: IPatient = { id: 16668 };
      familyAllergy.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ familyAllergy });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const familyAllergy: IFamilyAllergy = { id: 11017 };
      const patient: IPatient = { id: 16668 };
      familyAllergy.patient = patient;

      activatedRoute.data = of({ familyAllergy });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.familyAllergy).toEqual(familyAllergy);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyAllergy>>();
      const familyAllergy = { id: 5048 };
      jest.spyOn(familyAllergyFormService, 'getFamilyAllergy').mockReturnValue(familyAllergy);
      jest.spyOn(familyAllergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyAllergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyAllergy }));
      saveSubject.complete();

      // THEN
      expect(familyAllergyFormService.getFamilyAllergy).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(familyAllergyService.update).toHaveBeenCalledWith(expect.objectContaining(familyAllergy));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyAllergy>>();
      const familyAllergy = { id: 5048 };
      jest.spyOn(familyAllergyFormService, 'getFamilyAllergy').mockReturnValue({ id: null });
      jest.spyOn(familyAllergyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyAllergy: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyAllergy }));
      saveSubject.complete();

      // THEN
      expect(familyAllergyFormService.getFamilyAllergy).toHaveBeenCalled();
      expect(familyAllergyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyAllergy>>();
      const familyAllergy = { id: 5048 };
      jest.spyOn(familyAllergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyAllergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familyAllergyService.update).toHaveBeenCalled();
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
