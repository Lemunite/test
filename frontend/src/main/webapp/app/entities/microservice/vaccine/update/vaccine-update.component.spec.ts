import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { VaccinationForBabyService } from 'app/entities/microservice/vaccination-for-baby/service/vaccination-for-baby.service';
import { VaccineService } from '../service/vaccine.service';
import { IVaccine } from '../vaccine.model';
import { VaccineFormService } from './vaccine-form.service';

import { VaccineUpdateComponent } from './vaccine-update.component';

describe('Vaccine Management Update Component', () => {
  let comp: VaccineUpdateComponent;
  let fixture: ComponentFixture<VaccineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaccineFormService: VaccineFormService;
  let vaccineService: VaccineService;
  let vaccinationForBabyService: VaccinationForBabyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VaccineUpdateComponent],
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
      .overrideTemplate(VaccineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaccineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaccineFormService = TestBed.inject(VaccineFormService);
    vaccineService = TestBed.inject(VaccineService);
    vaccinationForBabyService = TestBed.inject(VaccinationForBabyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call VaccinationForBaby query and add missing value', () => {
      const vaccine: IVaccine = { id: 10849 };
      const vaccinationForBaby: IVaccinationForBaby = { id: 8159 };
      vaccine.vaccinationForBaby = vaccinationForBaby;

      const vaccinationForBabyCollection: IVaccinationForBaby[] = [{ id: 8159 }];
      jest.spyOn(vaccinationForBabyService, 'query').mockReturnValue(of(new HttpResponse({ body: vaccinationForBabyCollection })));
      const additionalVaccinationForBabies = [vaccinationForBaby];
      const expectedCollection: IVaccinationForBaby[] = [...additionalVaccinationForBabies, ...vaccinationForBabyCollection];
      jest.spyOn(vaccinationForBabyService, 'addVaccinationForBabyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vaccine });
      comp.ngOnInit();

      expect(vaccinationForBabyService.query).toHaveBeenCalled();
      expect(vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing).toHaveBeenCalledWith(
        vaccinationForBabyCollection,
        ...additionalVaccinationForBabies.map(expect.objectContaining),
      );
      expect(comp.vaccinationForBabiesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const vaccine: IVaccine = { id: 10849 };
      const vaccinationForBaby: IVaccinationForBaby = { id: 8159 };
      vaccine.vaccinationForBaby = vaccinationForBaby;

      activatedRoute.data = of({ vaccine });
      comp.ngOnInit();

      expect(comp.vaccinationForBabiesSharedCollection).toContainEqual(vaccinationForBaby);
      expect(comp.vaccine).toEqual(vaccine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccine>>();
      const vaccine = { id: 27715 };
      jest.spyOn(vaccineFormService, 'getVaccine').mockReturnValue(vaccine);
      jest.spyOn(vaccineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccine }));
      saveSubject.complete();

      // THEN
      expect(vaccineFormService.getVaccine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaccineService.update).toHaveBeenCalledWith(expect.objectContaining(vaccine));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccine>>();
      const vaccine = { id: 27715 };
      jest.spyOn(vaccineFormService, 'getVaccine').mockReturnValue({ id: null });
      jest.spyOn(vaccineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccine }));
      saveSubject.complete();

      // THEN
      expect(vaccineFormService.getVaccine).toHaveBeenCalled();
      expect(vaccineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccine>>();
      const vaccine = { id: 27715 };
      jest.spyOn(vaccineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaccineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
