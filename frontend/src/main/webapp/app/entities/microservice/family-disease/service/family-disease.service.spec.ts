import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFamilyDisease } from '../family-disease.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../family-disease.test-samples';

import { FamilyDiseaseService } from './family-disease.service';

const requireRestSample: IFamilyDisease = {
  ...sampleWithRequiredData,
};

describe('FamilyDisease Service', () => {
  let service: FamilyDiseaseService;
  let httpMock: HttpTestingController;
  let expectedResult: IFamilyDisease | IFamilyDisease[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FamilyDiseaseService);
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

    it('should create a FamilyDisease', () => {
      const familyDisease = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(familyDisease).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FamilyDisease', () => {
      const familyDisease = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(familyDisease).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FamilyDisease', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FamilyDisease', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FamilyDisease', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFamilyDiseaseToCollectionIfMissing', () => {
      it('should add a FamilyDisease to an empty array', () => {
        const familyDisease: IFamilyDisease = sampleWithRequiredData;
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing([], familyDisease);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyDisease);
      });

      it('should not add a FamilyDisease to an array that contains it', () => {
        const familyDisease: IFamilyDisease = sampleWithRequiredData;
        const familyDiseaseCollection: IFamilyDisease[] = [
          {
            ...familyDisease,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing(familyDiseaseCollection, familyDisease);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FamilyDisease to an array that doesn't contain it", () => {
        const familyDisease: IFamilyDisease = sampleWithRequiredData;
        const familyDiseaseCollection: IFamilyDisease[] = [sampleWithPartialData];
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing(familyDiseaseCollection, familyDisease);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyDisease);
      });

      it('should add only unique FamilyDisease to an array', () => {
        const familyDiseaseArray: IFamilyDisease[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const familyDiseaseCollection: IFamilyDisease[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing(familyDiseaseCollection, ...familyDiseaseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const familyDisease: IFamilyDisease = sampleWithRequiredData;
        const familyDisease2: IFamilyDisease = sampleWithPartialData;
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing([], familyDisease, familyDisease2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyDisease);
        expect(expectedResult).toContain(familyDisease2);
      });

      it('should accept null and undefined values', () => {
        const familyDisease: IFamilyDisease = sampleWithRequiredData;
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing([], null, familyDisease, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyDisease);
      });

      it('should return initial array if no FamilyDisease is added', () => {
        const familyDiseaseCollection: IFamilyDisease[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyDiseaseToCollectionIfMissing(familyDiseaseCollection, undefined, null);
        expect(expectedResult).toEqual(familyDiseaseCollection);
      });
    });

    describe('compareFamilyDisease', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFamilyDisease(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2762 };
        const entity2 = null;

        const compareResult1 = service.compareFamilyDisease(entity1, entity2);
        const compareResult2 = service.compareFamilyDisease(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2762 };
        const entity2 = { id: 31816 };

        const compareResult1 = service.compareFamilyDisease(entity1, entity2);
        const compareResult2 = service.compareFamilyDisease(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2762 };
        const entity2 = { id: 2762 };

        const compareResult1 = service.compareFamilyDisease(entity1, entity2);
        const compareResult2 = service.compareFamilyDisease(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
