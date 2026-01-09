import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VaccinationForBabyService } from '../service/vaccination-for-baby.service';
import { IVaccinationForBaby } from '../vaccination-for-baby.model';
import { VaccinationForBabyFormService } from './vaccination-for-baby-form.service';

import { VaccinationForBabyUpdateComponent } from './vaccination-for-baby-update.component';

describe('VaccinationForBaby Management Update Component', () => {
  let comp: VaccinationForBabyUpdateComponent;
  let fixture: ComponentFixture<VaccinationForBabyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaccinationForBabyFormService: VaccinationForBabyFormService;
  let vaccinationForBabyService: VaccinationForBabyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VaccinationForBabyUpdateComponent],
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
      .overrideTemplate(VaccinationForBabyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaccinationForBabyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaccinationForBabyFormService = TestBed.inject(VaccinationForBabyFormService);
    vaccinationForBabyService = TestBed.inject(VaccinationForBabyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const vaccinationForBaby: IVaccinationForBaby = { id: 31096 };

      activatedRoute.data = of({ vaccinationForBaby });
      comp.ngOnInit();

      expect(comp.vaccinationForBaby).toEqual(vaccinationForBaby);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationForBaby>>();
      const vaccinationForBaby = { id: 8159 };
      jest.spyOn(vaccinationForBabyFormService, 'getVaccinationForBaby').mockReturnValue(vaccinationForBaby);
      jest.spyOn(vaccinationForBabyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationForBaby });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccinationForBaby }));
      saveSubject.complete();

      // THEN
      expect(vaccinationForBabyFormService.getVaccinationForBaby).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaccinationForBabyService.update).toHaveBeenCalledWith(expect.objectContaining(vaccinationForBaby));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationForBaby>>();
      const vaccinationForBaby = { id: 8159 };
      jest.spyOn(vaccinationForBabyFormService, 'getVaccinationForBaby').mockReturnValue({ id: null });
      jest.spyOn(vaccinationForBabyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationForBaby: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccinationForBaby }));
      saveSubject.complete();

      // THEN
      expect(vaccinationForBabyFormService.getVaccinationForBaby).toHaveBeenCalled();
      expect(vaccinationForBabyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVaccinationForBaby>>();
      const vaccinationForBaby = { id: 8159 };
      jest.spyOn(vaccinationForBabyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccinationForBaby });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaccinationForBabyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
