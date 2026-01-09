import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { FamilyDiseaseService } from '../service/family-disease.service';
import { IFamilyDisease } from '../family-disease.model';
import { FamilyDiseaseFormService } from './family-disease-form.service';

import { FamilyDiseaseUpdateComponent } from './family-disease-update.component';

describe('FamilyDisease Management Update Component', () => {
  let comp: FamilyDiseaseUpdateComponent;
  let fixture: ComponentFixture<FamilyDiseaseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familyDiseaseFormService: FamilyDiseaseFormService;
  let familyDiseaseService: FamilyDiseaseService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FamilyDiseaseUpdateComponent],
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
      .overrideTemplate(FamilyDiseaseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilyDiseaseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familyDiseaseFormService = TestBed.inject(FamilyDiseaseFormService);
    familyDiseaseService = TestBed.inject(FamilyDiseaseService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Patient query and add missing value', () => {
      const familyDisease: IFamilyDisease = { id: 31816 };
      const patient: IPatient = { id: 16668 };
      familyDisease.patient = patient;

      const patientCollection: IPatient[] = [{ id: 16668 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ familyDisease });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const familyDisease: IFamilyDisease = { id: 31816 };
      const patient: IPatient = { id: 16668 };
      familyDisease.patient = patient;

      activatedRoute.data = of({ familyDisease });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContainEqual(patient);
      expect(comp.familyDisease).toEqual(familyDisease);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyDisease>>();
      const familyDisease = { id: 2762 };
      jest.spyOn(familyDiseaseFormService, 'getFamilyDisease').mockReturnValue(familyDisease);
      jest.spyOn(familyDiseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyDisease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyDisease }));
      saveSubject.complete();

      // THEN
      expect(familyDiseaseFormService.getFamilyDisease).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(familyDiseaseService.update).toHaveBeenCalledWith(expect.objectContaining(familyDisease));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyDisease>>();
      const familyDisease = { id: 2762 };
      jest.spyOn(familyDiseaseFormService, 'getFamilyDisease').mockReturnValue({ id: null });
      jest.spyOn(familyDiseaseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyDisease: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyDisease }));
      saveSubject.complete();

      // THEN
      expect(familyDiseaseFormService.getFamilyDisease).toHaveBeenCalled();
      expect(familyDiseaseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyDisease>>();
      const familyDisease = { id: 2762 };
      jest.spyOn(familyDiseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyDisease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familyDiseaseService.update).toHaveBeenCalled();
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
