import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IParaclinicalResult } from '../paraclinical-result.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../paraclinical-result.test-samples';

import { ParaclinicalResultService, RestParaclinicalResult } from './paraclinical-result.service';

const requireRestSample: RestParaclinicalResult = {
  ...sampleWithRequiredData,
  resultDate: sampleWithRequiredData.resultDate?.format(DATE_FORMAT),
};

describe('ParaclinicalResult Service', () => {
  let service: ParaclinicalResultService;
  let httpMock: HttpTestingController;
  let expectedResult: IParaclinicalResult | IParaclinicalResult[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ParaclinicalResultService);
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

    it('should create a ParaclinicalResult', () => {
      const paraclinicalResult = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paraclinicalResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ParaclinicalResult', () => {
      const paraclinicalResult = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paraclinicalResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ParaclinicalResult', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ParaclinicalResult', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ParaclinicalResult', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParaclinicalResultToCollectionIfMissing', () => {
      it('should add a ParaclinicalResult to an empty array', () => {
        const paraclinicalResult: IParaclinicalResult = sampleWithRequiredData;
        expectedResult = service.addParaclinicalResultToCollectionIfMissing([], paraclinicalResult);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paraclinicalResult);
      });

      it('should not add a ParaclinicalResult to an array that contains it', () => {
        const paraclinicalResult: IParaclinicalResult = sampleWithRequiredData;
        const paraclinicalResultCollection: IParaclinicalResult[] = [
          {
            ...paraclinicalResult,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParaclinicalResultToCollectionIfMissing(paraclinicalResultCollection, paraclinicalResult);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ParaclinicalResult to an array that doesn't contain it", () => {
        const paraclinicalResult: IParaclinicalResult = sampleWithRequiredData;
        const paraclinicalResultCollection: IParaclinicalResult[] = [sampleWithPartialData];
        expectedResult = service.addParaclinicalResultToCollectionIfMissing(paraclinicalResultCollection, paraclinicalResult);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paraclinicalResult);
      });

      it('should add only unique ParaclinicalResult to an array', () => {
        const paraclinicalResultArray: IParaclinicalResult[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paraclinicalResultCollection: IParaclinicalResult[] = [sampleWithRequiredData];
        expectedResult = service.addParaclinicalResultToCollectionIfMissing(paraclinicalResultCollection, ...paraclinicalResultArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paraclinicalResult: IParaclinicalResult = sampleWithRequiredData;
        const paraclinicalResult2: IParaclinicalResult = sampleWithPartialData;
        expectedResult = service.addParaclinicalResultToCollectionIfMissing([], paraclinicalResult, paraclinicalResult2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paraclinicalResult);
        expect(expectedResult).toContain(paraclinicalResult2);
      });

      it('should accept null and undefined values', () => {
        const paraclinicalResult: IParaclinicalResult = sampleWithRequiredData;
        expectedResult = service.addParaclinicalResultToCollectionIfMissing([], null, paraclinicalResult, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paraclinicalResult);
      });

      it('should return initial array if no ParaclinicalResult is added', () => {
        const paraclinicalResultCollection: IParaclinicalResult[] = [sampleWithRequiredData];
        expectedResult = service.addParaclinicalResultToCollectionIfMissing(paraclinicalResultCollection, undefined, null);
        expect(expectedResult).toEqual(paraclinicalResultCollection);
      });
    });

    describe('compareParaclinicalResult', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParaclinicalResult(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25600 };
        const entity2 = null;

        const compareResult1 = service.compareParaclinicalResult(entity1, entity2);
        const compareResult2 = service.compareParaclinicalResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25600 };
        const entity2 = { id: 3394 };

        const compareResult1 = service.compareParaclinicalResult(entity1, entity2);
        const compareResult2 = service.compareParaclinicalResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25600 };
        const entity2 = { id: 25600 };

        const compareResult1 = service.compareParaclinicalResult(entity1, entity2);
        const compareResult2 = service.compareParaclinicalResult(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
