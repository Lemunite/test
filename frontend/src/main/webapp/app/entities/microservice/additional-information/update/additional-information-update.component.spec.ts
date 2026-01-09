import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AdditionalInformationService } from '../service/additional-information.service';
import { IAdditionalInformation } from '../additional-information.model';
import { AdditionalInformationFormService } from './additional-information-form.service';

import { AdditionalInformationUpdateComponent } from './additional-information-update.component';

describe('AdditionalInformation Management Update Component', () => {
  let comp: AdditionalInformationUpdateComponent;
  let fixture: ComponentFixture<AdditionalInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let additionalInformationFormService: AdditionalInformationFormService;
  let additionalInformationService: AdditionalInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdditionalInformationUpdateComponent],
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
      .overrideTemplate(AdditionalInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdditionalInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    additionalInformationFormService = TestBed.inject(AdditionalInformationFormService);
    additionalInformationService = TestBed.inject(AdditionalInformationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const additionalInformation: IAdditionalInformation = { id: 21718 };

      activatedRoute.data = of({ additionalInformation });
      comp.ngOnInit();

      expect(comp.additionalInformation).toEqual(additionalInformation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalInformation>>();
      const additionalInformation = { id: 11732 };
      jest.spyOn(additionalInformationFormService, 'getAdditionalInformation').mockReturnValue(additionalInformation);
      jest.spyOn(additionalInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalInformation }));
      saveSubject.complete();

      // THEN
      expect(additionalInformationFormService.getAdditionalInformation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(additionalInformationService.update).toHaveBeenCalledWith(expect.objectContaining(additionalInformation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalInformation>>();
      const additionalInformation = { id: 11732 };
      jest.spyOn(additionalInformationFormService, 'getAdditionalInformation').mockReturnValue({ id: null });
      jest.spyOn(additionalInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalInformation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalInformation }));
      saveSubject.complete();

      // THEN
      expect(additionalInformationFormService.getAdditionalInformation).toHaveBeenCalled();
      expect(additionalInformationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalInformation>>();
      const additionalInformation = { id: 11732 };
      jest.spyOn(additionalInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(additionalInformationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
