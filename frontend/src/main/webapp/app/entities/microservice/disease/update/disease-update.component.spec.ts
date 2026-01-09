import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFamilyDisease } from 'app/entities/microservice/family-disease/family-disease.model';
import { FamilyDiseaseService } from 'app/entities/microservice/family-disease/service/family-disease.service';
import { DiseaseService } from '../service/disease.service';
import { IDisease } from '../disease.model';
import { DiseaseFormService } from './disease-form.service';

import { DiseaseUpdateComponent } from './disease-update.component';

describe('Disease Management Update Component', () => {
  let comp: DiseaseUpdateComponent;
  let fixture: ComponentFixture<DiseaseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let diseaseFormService: DiseaseFormService;
  let diseaseService: DiseaseService;
  let familyDiseaseService: FamilyDiseaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DiseaseUpdateComponent],
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
      .overrideTemplate(DiseaseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DiseaseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    diseaseFormService = TestBed.inject(DiseaseFormService);
    diseaseService = TestBed.inject(DiseaseService);
    familyDiseaseService = TestBed.inject(FamilyDiseaseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FamilyDisease query and add missing value', () => {
      const disease: IDisease = { id: 25050 };
      const familyDisease: IFamilyDisease = { id: 2762 };
      disease.familyDisease = familyDisease;

      const familyDiseaseCollection: IFamilyDisease[] = [{ id: 2762 }];
      jest.spyOn(familyDiseaseService, 'query').mockReturnValue(of(new HttpResponse({ body: familyDiseaseCollection })));
      const additionalFamilyDiseases = [familyDisease];
      const expectedCollection: IFamilyDisease[] = [...additionalFamilyDiseases, ...familyDiseaseCollection];
      jest.spyOn(familyDiseaseService, 'addFamilyDiseaseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disease });
      comp.ngOnInit();

      expect(familyDiseaseService.query).toHaveBeenCalled();
      expect(familyDiseaseService.addFamilyDiseaseToCollectionIfMissing).toHaveBeenCalledWith(
        familyDiseaseCollection,
        ...additionalFamilyDiseases.map(expect.objectContaining),
      );
      expect(comp.familyDiseasesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const disease: IDisease = { id: 25050 };
      const familyDisease: IFamilyDisease = { id: 2762 };
      disease.familyDisease = familyDisease;

      activatedRoute.data = of({ disease });
      comp.ngOnInit();

      expect(comp.familyDiseasesSharedCollection).toContainEqual(familyDisease);
      expect(comp.disease).toEqual(disease);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisease>>();
      const disease = { id: 23904 };
      jest.spyOn(diseaseFormService, 'getDisease').mockReturnValue(disease);
      jest.spyOn(diseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disease }));
      saveSubject.complete();

      // THEN
      expect(diseaseFormService.getDisease).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(diseaseService.update).toHaveBeenCalledWith(expect.objectContaining(disease));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisease>>();
      const disease = { id: 23904 };
      jest.spyOn(diseaseFormService, 'getDisease').mockReturnValue({ id: null });
      jest.spyOn(diseaseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disease: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disease }));
      saveSubject.complete();

      // THEN
      expect(diseaseFormService.getDisease).toHaveBeenCalled();
      expect(diseaseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisease>>();
      const disease = { id: 23904 };
      jest.spyOn(diseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(diseaseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFamilyDisease', () => {
      it('should forward to familyDiseaseService', () => {
        const entity = { id: 2762 };
        const entity2 = { id: 31816 };
        jest.spyOn(familyDiseaseService, 'compareFamilyDisease');
        comp.compareFamilyDisease(entity, entity2);
        expect(familyDiseaseService.compareFamilyDisease).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
