import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDisability } from '../disability.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../disability.test-samples';

import { DisabilityService } from './disability.service';

const requireRestSample: IDisability = {
  ...sampleWithRequiredData,
};

describe('Disability Service', () => {
  let service: DisabilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IDisability | IDisability[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DisabilityService);
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

    it('should create a Disability', () => {
      const disability = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(disability).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Disability', () => {
      const disability = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(disability).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Disability', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Disability', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Disability', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDisabilityToCollectionIfMissing', () => {
      it('should add a Disability to an empty array', () => {
        const disability: IDisability = sampleWithRequiredData;
        expectedResult = service.addDisabilityToCollectionIfMissing([], disability);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disability);
      });

      it('should not add a Disability to an array that contains it', () => {
        const disability: IDisability = sampleWithRequiredData;
        const disabilityCollection: IDisability[] = [
          {
            ...disability,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDisabilityToCollectionIfMissing(disabilityCollection, disability);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Disability to an array that doesn't contain it", () => {
        const disability: IDisability = sampleWithRequiredData;
        const disabilityCollection: IDisability[] = [sampleWithPartialData];
        expectedResult = service.addDisabilityToCollectionIfMissing(disabilityCollection, disability);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disability);
      });

      it('should add only unique Disability to an array', () => {
        const disabilityArray: IDisability[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const disabilityCollection: IDisability[] = [sampleWithRequiredData];
        expectedResult = service.addDisabilityToCollectionIfMissing(disabilityCollection, ...disabilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disability: IDisability = sampleWithRequiredData;
        const disability2: IDisability = sampleWithPartialData;
        expectedResult = service.addDisabilityToCollectionIfMissing([], disability, disability2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disability);
        expect(expectedResult).toContain(disability2);
      });

      it('should accept null and undefined values', () => {
        const disability: IDisability = sampleWithRequiredData;
        expectedResult = service.addDisabilityToCollectionIfMissing([], null, disability, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disability);
      });

      it('should return initial array if no Disability is added', () => {
        const disabilityCollection: IDisability[] = [sampleWithRequiredData];
        expectedResult = service.addDisabilityToCollectionIfMissing(disabilityCollection, undefined, null);
        expect(expectedResult).toEqual(disabilityCollection);
      });
    });

    describe('compareDisability', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDisability(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10283 };
        const entity2 = null;

        const compareResult1 = service.compareDisability(entity1, entity2);
        const compareResult2 = service.compareDisability(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10283 };
        const entity2 = { id: 11805 };

        const compareResult1 = service.compareDisability(entity1, entity2);
        const compareResult2 = service.compareDisability(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10283 };
        const entity2 = { id: 10283 };

        const compareResult1 = service.compareDisability(entity1, entity2);
        const compareResult2 = service.compareDisability(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
