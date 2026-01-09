import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedicalRecord } from 'app/entities/microservice/medical-record/medical-record.model';
import { MedicalRecordService } from 'app/entities/microservice/medical-record/service/medical-record.service';
import { ParaclinicalResultService } from '../service/paraclinical-result.service';
import { IParaclinicalResult } from '../paraclinical-result.model';
import { ParaclinicalResultFormService } from './paraclinical-result-form.service';

import { ParaclinicalResultUpdateComponent } from './paraclinical-result-update.component';

describe('ParaclinicalResult Management Update Component', () => {
  let comp: ParaclinicalResultUpdateComponent;
  let fixture: ComponentFixture<ParaclinicalResultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paraclinicalResultFormService: ParaclinicalResultFormService;
  let paraclinicalResultService: ParaclinicalResultService;
  let medicalRecordService: MedicalRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ParaclinicalResultUpdateComponent],
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
      .overrideTemplate(ParaclinicalResultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParaclinicalResultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paraclinicalResultFormService = TestBed.inject(ParaclinicalResultFormService);
    paraclinicalResultService = TestBed.inject(ParaclinicalResultService);
    medicalRecordService = TestBed.inject(MedicalRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MedicalRecord query and add missing value', () => {
      const paraclinicalResult: IParaclinicalResult = { id: 3394 };
      const medicalRecord: IMedicalRecord = { id: 17381 };
      paraclinicalResult.medicalRecord = medicalRecord;

      const medicalRecordCollection: IMedicalRecord[] = [{ id: 17381 }];
      jest.spyOn(medicalRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: medicalRecordCollection })));
      const additionalMedicalRecords = [medicalRecord];
      const expectedCollection: IMedicalRecord[] = [...additionalMedicalRecords, ...medicalRecordCollection];
      jest.spyOn(medicalRecordService, 'addMedicalRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paraclinicalResult });
      comp.ngOnInit();

      expect(medicalRecordService.query).toHaveBeenCalled();
      expect(medicalRecordService.addMedicalRecordToCollectionIfMissing).toHaveBeenCalledWith(
        medicalRecordCollection,
        ...additionalMedicalRecords.map(expect.objectContaining),
      );
      expect(comp.medicalRecordsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paraclinicalResult: IParaclinicalResult = { id: 3394 };
      const medicalRecord: IMedicalRecord = { id: 17381 };
      paraclinicalResult.medicalRecord = medicalRecord;

      activatedRoute.data = of({ paraclinicalResult });
      comp.ngOnInit();

      expect(comp.medicalRecordsSharedCollection).toContainEqual(medicalRecord);
      expect(comp.paraclinicalResult).toEqual(paraclinicalResult);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParaclinicalResult>>();
      const paraclinicalResult = { id: 25600 };
      jest.spyOn(paraclinicalResultFormService, 'getParaclinicalResult').mockReturnValue(paraclinicalResult);
      jest.spyOn(paraclinicalResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paraclinicalResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paraclinicalResult }));
      saveSubject.complete();

      // THEN
      expect(paraclinicalResultFormService.getParaclinicalResult).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paraclinicalResultService.update).toHaveBeenCalledWith(expect.objectContaining(paraclinicalResult));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParaclinicalResult>>();
      const paraclinicalResult = { id: 25600 };
      jest.spyOn(paraclinicalResultFormService, 'getParaclinicalResult').mockReturnValue({ id: null });
      jest.spyOn(paraclinicalResultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paraclinicalResult: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paraclinicalResult }));
      saveSubject.complete();

      // THEN
      expect(paraclinicalResultFormService.getParaclinicalResult).toHaveBeenCalled();
      expect(paraclinicalResultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParaclinicalResult>>();
      const paraclinicalResult = { id: 25600 };
      jest.spyOn(paraclinicalResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paraclinicalResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paraclinicalResultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicalRecord', () => {
      it('should forward to medicalRecordService', () => {
        const entity = { id: 17381 };
        const entity2 = { id: 9576 };
        jest.spyOn(medicalRecordService, 'compareMedicalRecord');
        comp.compareMedicalRecord(entity, entity2);
        expect(medicalRecordService.compareMedicalRecord).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
