import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPregnancyTetanus } from '../pregnancy-tetanus.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pregnancy-tetanus.test-samples';

import { PregnancyTetanusService, RestPregnancyTetanus } from './pregnancy-tetanus.service';

const requireRestSample: RestPregnancyTetanus = {
  ...sampleWithRequiredData,
  injectionDate: sampleWithRequiredData.injectionDate?.format(DATE_FORMAT),
  nextAppointment: sampleWithRequiredData.nextAppointment?.format(DATE_FORMAT),
};

describe('PregnancyTetanus Service', () => {
  let service: PregnancyTetanusService;
  let httpMock: HttpTestingController;
  let expectedResult: IPregnancyTetanus | IPregnancyTetanus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PregnancyTetanusService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PregnancyTetanus', () => {
      const pregnancyTetanus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pregnancyTetanus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PregnancyTetanus', () => {
      const pregnancyTetanus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pregnancyTetanus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PregnancyTetanus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PregnancyTetanus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PregnancyTetanus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPregnancyTetanusToCollectionIfMissing', () => {
      it('should add a PregnancyTetanus to an empty array', () => {
        const pregnancyTetanus: IPregnancyTetanus = sampleWithRequiredData;
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing([], pregnancyTetanus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pregnancyTetanus);
      });

      it('should not add a PregnancyTetanus to an array that contains it', () => {
        const pregnancyTetanus: IPregnancyTetanus = sampleWithRequiredData;
        const pregnancyTetanusCollection: IPregnancyTetanus[] = [
          {
            ...pregnancyTetanus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing(pregnancyTetanusCollection, pregnancyTetanus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PregnancyTetanus to an array that doesn't contain it", () => {
        const pregnancyTetanus: IPregnancyTetanus = sampleWithRequiredData;
        const pregnancyTetanusCollection: IPregnancyTetanus[] = [sampleWithPartialData];
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing(pregnancyTetanusCollection, pregnancyTetanus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pregnancyTetanus);
      });

      it('should add only unique PregnancyTetanus to an array', () => {
        const pregnancyTetanusArray: IPregnancyTetanus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pregnancyTetanusCollection: IPregnancyTetanus[] = [sampleWithRequiredData];
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing(pregnancyTetanusCollection, ...pregnancyTetanusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pregnancyTetanus: IPregnancyTetanus = sampleWithRequiredData;
        const pregnancyTetanus2: IPregnancyTetanus = sampleWithPartialData;
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing([], pregnancyTetanus, pregnancyTetanus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pregnancyTetanus);
        expect(expectedResult).toContain(pregnancyTetanus2);
      });

      it('should accept null and undefined values', () => {
        const pregnancyTetanus: IPregnancyTetanus = sampleWithRequiredData;
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing([], null, pregnancyTetanus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pregnancyTetanus);
      });

      it('should return initial array if no PregnancyTetanus is added', () => {
        const pregnancyTetanusCollection: IPregnancyTetanus[] = [sampleWithRequiredData];
        expectedResult = service.addPregnancyTetanusToCollectionIfMissing(pregnancyTetanusCollection, undefined, null);
        expect(expectedResult).toEqual(pregnancyTetanusCollection);
      });
    });

    describe('comparePregnancyTetanus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePregnancyTetanus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5738 };
        const entity2 = null;

        const compareResult1 = service.comparePregnancyTetanus(entity1, entity2);
        const compareResult2 = service.comparePregnancyTetanus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5738 };
        const entity2 = { id: 27406 };

        const compareResult1 = service.comparePregnancyTetanus(entity1, entity2);
        const compareResult2 = service.comparePregnancyTetanus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5738 };
        const entity2 = { id: 5738 };

        const compareResult1 = service.comparePregnancyTetanus(entity1, entity2);
        const compareResult2 = service.comparePregnancyTetanus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
