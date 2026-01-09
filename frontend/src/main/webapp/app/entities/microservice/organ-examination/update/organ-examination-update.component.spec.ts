import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { OrganExaminationService } from '../service/organ-examination.service';
import { IOrganExamination } from '../organ-examination.model';
import { OrganExaminationFormService } from './organ-examination-form.service';

import { OrganExaminationUpdateComponent } from './organ-examination-update.component';

describe('OrganExamination Management Update Component', () => {
  let comp: OrganExaminationUpdateComponent;
  let fixture: ComponentFixture<OrganExaminationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organExaminationFormService: OrganExaminationFormService;
  let organExaminationService: OrganExaminationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrganExaminationUpdateComponent],
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
      .overrideTemplate(OrganExaminationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganExaminationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organExaminationFormService = TestBed.inject(OrganExaminationFormService);
    organExaminationService = TestBed.inject(OrganExaminationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const organExamination: IOrganExamination = { id: 28596 };

      activatedRoute.data = of({ organExamination });
      comp.ngOnInit();

      expect(comp.organExamination).toEqual(organExamination);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganExamination>>();
      const organExamination = { id: 8565 };
      jest.spyOn(organExaminationFormService, 'getOrganExamination').mockReturnValue(organExamination);
      jest.spyOn(organExaminationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organExamination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organExamination }));
      saveSubject.complete();

      // THEN
      expect(organExaminationFormService.getOrganExamination).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organExaminationService.update).toHaveBeenCalledWith(expect.objectContaining(organExamination));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganExamination>>();
      const organExamination = { id: 8565 };
      jest.spyOn(organExaminationFormService, 'getOrganExamination').mockReturnValue({ id: null });
      jest.spyOn(organExaminationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organExamination: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organExamination }));
      saveSubject.complete();

      // THEN
      expect(organExaminationFormService.getOrganExamination).toHaveBeenCalled();
      expect(organExaminationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganExamination>>();
      const organExamination = { id: 8565 };
      jest.spyOn(organExaminationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organExamination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organExaminationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
