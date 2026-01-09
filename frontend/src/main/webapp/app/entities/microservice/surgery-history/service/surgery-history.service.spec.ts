import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISurgeryHistory } from '../surgery-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../surgery-history.test-samples';

import { SurgeryHistoryService } from './surgery-history.service';

const requireRestSample: ISurgeryHistory = {
  ...sampleWithRequiredData,
};

describe('SurgeryHistory Service', () => {
  let service: SurgeryHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ISurgeryHistory | ISurgeryHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SurgeryHistoryService);
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

    it('should create a SurgeryHistory', () => {
      const surgeryHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(surgeryHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SurgeryHistory', () => {
      const surgeryHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(surgeryHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SurgeryHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SurgeryHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SurgeryHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSurgeryHistoryToCollectionIfMissing', () => {
      it('should add a SurgeryHistory to an empty array', () => {
        const surgeryHistory: ISurgeryHistory = sampleWithRequiredData;
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing([], surgeryHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surgeryHistory);
      });

      it('should not add a SurgeryHistory to an array that contains it', () => {
        const surgeryHistory: ISurgeryHistory = sampleWithRequiredData;
        const surgeryHistoryCollection: ISurgeryHistory[] = [
          {
            ...surgeryHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing(surgeryHistoryCollection, surgeryHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SurgeryHistory to an array that doesn't contain it", () => {
        const surgeryHistory: ISurgeryHistory = sampleWithRequiredData;
        const surgeryHistoryCollection: ISurgeryHistory[] = [sampleWithPartialData];
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing(surgeryHistoryCollection, surgeryHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surgeryHistory);
      });

      it('should add only unique SurgeryHistory to an array', () => {
        const surgeryHistoryArray: ISurgeryHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const surgeryHistoryCollection: ISurgeryHistory[] = [sampleWithRequiredData];
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing(surgeryHistoryCollection, ...surgeryHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const surgeryHistory: ISurgeryHistory = sampleWithRequiredData;
        const surgeryHistory2: ISurgeryHistory = sampleWithPartialData;
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing([], surgeryHistory, surgeryHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surgeryHistory);
        expect(expectedResult).toContain(surgeryHistory2);
      });

      it('should accept null and undefined values', () => {
        const surgeryHistory: ISurgeryHistory = sampleWithRequiredData;
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing([], null, surgeryHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surgeryHistory);
      });

      it('should return initial array if no SurgeryHistory is added', () => {
        const surgeryHistoryCollection: ISurgeryHistory[] = [sampleWithRequiredData];
        expectedResult = service.addSurgeryHistoryToCollectionIfMissing(surgeryHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(surgeryHistoryCollection);
      });
    });

    describe('compareSurgeryHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSurgeryHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2530 };
        const entity2 = null;

        const compareResult1 = service.compareSurgeryHistory(entity1, entity2);
        const compareResult2 = service.compareSurgeryHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2530 };
        const entity2 = { id: 18610 };

        const compareResult1 = service.compareSurgeryHistory(entity1, entity2);
        const compareResult2 = service.compareSurgeryHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2530 };
        const entity2 = { id: 2530 };

        const compareResult1 = service.compareSurgeryHistory(entity1, entity2);
        const compareResult2 = service.compareSurgeryHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
