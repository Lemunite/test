import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAdditionalInformation } from 'app/entities/microservice/additional-information/additional-information.model';
import { AdditionalInformationService } from 'app/entities/microservice/additional-information/service/additional-information.service';
import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { VaccinationForBabyService } from 'app/entities/microservice/vaccination-for-baby/service/vaccination-for-baby.service';
import { IPatient } from '../patient.model';
import { PatientService } from '../service/patient.service';
import { PatientFormService } from './patient-form.service';

import { PatientUpdateComponent } from './patient-update.component';

describe('Patient Management Update Component', () => {
  let comp: PatientUpdateComponent;
  let fixture: ComponentFixture<PatientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientFormService: PatientFormService;
  let patientService: PatientService;
  let additionalInformationService: AdditionalInformationService;
  let vaccinationForBabyService: VaccinationForBabyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PatientUpdateComponent],
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
      .overrideTemplate(PatientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientFormService = TestBed.inject(PatientFormService);
    patientService = TestBed.inject(PatientService);
    additionalInformationService = TestBed.inject(AdditionalInformationService);
    vaccinationForBabyService = TestBed.inject(VaccinationForBabyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call additionalInfo query and add missing value', () => {
      const patient: IPatient = { id: 16914 };
      const additionalInfo: IAdditionalInformation = { id: 11732 };
      patient.additionalInfo = additionalInfo;

      const additionalInfoCollection: IAdditionalInformation[] = [{ id: 11732 }];
      jest.spyOn(additionalInformationService, 'query').mockReturnValue(of(new HttpResponse({ body: additionalInfoCollection })));
      const expectedCollection: IAdditionalInformation[] = [additionalInfo, ...additionalInfoCollection];
      jest.spyOn(additionalInformationService, 'addAdditionalInformationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(additionalInformationService.query).toHaveBeenCalled();
      expect(additionalInformationService.addAdditionalInformationToCollectionIfMissing).toHaveBeenCalledWith(
        additionalInfoCollection,
        additionalInfo,
      );
      expect(comp.additionalInfosCollection).toEqual(expectedCollection);
    });

    it('should call vaccinationsForBaby query and add missing value', () => {
      const patient: IPatient = { id: 16914 };
      const vaccinationsForBaby: IVaccinationForBaby = { id: 8159 };
      patient.vaccinationsForBaby = vaccinationsForBaby;

      const vaccinationsForBabyCollection: IVaccinationForBaby[] = [{ id: 8159 }];
      jest.spyOn(vaccinationForBabyService, 'query').mockReturnValue(of(new HttpResponse({ body: vaccinationsForBabyCollection })));
      const expectedCollection: IVaccinationForBaby[] = [vaccinationsForBaby, ...vaccinationsForBabyCollection];
      jest.spyOn(vaccinationForBabyService, 'addVaccinationForBabyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(vaccinationForBabyService.query).toHaveBeenCalled();
      expect(vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing).toHaveBeenCalledWith(
        vaccinationsForBabyCollection,
        vaccinationsForBaby,
      );
      expect(comp.vaccinationsForBabiesCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const patient: IPatient = { id: 16914 };
      const additionalInfo: IAdditionalInformation = { id: 11732 };
      patient.additionalInfo = additionalInfo;
      const vaccinationsForBaby: IVaccinationForBaby = { id: 8159 };
      patient.vaccinationsForBaby = vaccinationsForBaby;

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(comp.additionalInfosCollection).toContainEqual(additionalInfo);
      expect(comp.vaccinationsForBabiesCollection).toContainEqual(vaccinationsForBaby);
      expect(comp.patient).toEqual(patient);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 16668 };
      jest.spyOn(patientFormService, 'getPatient').mockReturnValue(patient);
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(patientFormService.getPatient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientService.update).toHaveBeenCalledWith(expect.objectContaining(patient));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 16668 };
      jest.spyOn(patientFormService, 'getPatient').mockReturnValue({ id: null });
      jest.spyOn(patientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(patientFormService.getPatient).toHaveBeenCalled();
      expect(patientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 16668 };
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAdditionalInformation', () => {
      it('should forward to additionalInformationService', () => {
        const entity = { id: 11732 };
        const entity2 = { id: 21718 };
        jest.spyOn(additionalInformationService, 'compareAdditionalInformation');
        comp.compareAdditionalInformation(entity, entity2);
        expect(additionalInformationService.compareAdditionalInformation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVaccinationForBaby', () => {
      it('should forward to vaccinationForBabyService', () => {
        const entity = { id: 8159 };
        const entity2 = { id: 31096 };
        jest.spyOn(vaccinationForBabyService, 'compareVaccinationForBaby');
        comp.compareVaccinationForBaby(entity, entity2);
        expect(vaccinationForBabyService.compareVaccinationForBaby).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
