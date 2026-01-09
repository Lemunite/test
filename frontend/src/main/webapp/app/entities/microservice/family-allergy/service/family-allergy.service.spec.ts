import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFamilyAllergy } from '../family-allergy.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../family-allergy.test-samples';

import { FamilyAllergyService } from './family-allergy.service';

const requireRestSample: IFamilyAllergy = {
  ...sampleWithRequiredData,
};

describe('FamilyAllergy Service', () => {
  let service: FamilyAllergyService;
  let httpMock: HttpTestingController;
  let expectedResult: IFamilyAllergy | IFamilyAllergy[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FamilyAllergyService);
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

    it('should create a FamilyAllergy', () => {
      const familyAllergy = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(familyAllergy).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FamilyAllergy', () => {
      const familyAllergy = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(familyAllergy).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FamilyAllergy', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FamilyAllergy', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FamilyAllergy', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFamilyAllergyToCollectionIfMissing', () => {
      it('should add a FamilyAllergy to an empty array', () => {
        const familyAllergy: IFamilyAllergy = sampleWithRequiredData;
        expectedResult = service.addFamilyAllergyToCollectionIfMissing([], familyAllergy);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyAllergy);
      });

      it('should not add a FamilyAllergy to an array that contains it', () => {
        const familyAllergy: IFamilyAllergy = sampleWithRequiredData;
        const familyAllergyCollection: IFamilyAllergy[] = [
          {
            ...familyAllergy,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFamilyAllergyToCollectionIfMissing(familyAllergyCollection, familyAllergy);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FamilyAllergy to an array that doesn't contain it", () => {
        const familyAllergy: IFamilyAllergy = sampleWithRequiredData;
        const familyAllergyCollection: IFamilyAllergy[] = [sampleWithPartialData];
        expectedResult = service.addFamilyAllergyToCollectionIfMissing(familyAllergyCollection, familyAllergy);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyAllergy);
      });

      it('should add only unique FamilyAllergy to an array', () => {
        const familyAllergyArray: IFamilyAllergy[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const familyAllergyCollection: IFamilyAllergy[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyAllergyToCollectionIfMissing(familyAllergyCollection, ...familyAllergyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const familyAllergy: IFamilyAllergy = sampleWithRequiredData;
        const familyAllergy2: IFamilyAllergy = sampleWithPartialData;
        expectedResult = service.addFamilyAllergyToCollectionIfMissing([], familyAllergy, familyAllergy2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyAllergy);
        expect(expectedResult).toContain(familyAllergy2);
      });

      it('should accept null and undefined values', () => {
        const familyAllergy: IFamilyAllergy = sampleWithRequiredData;
        expectedResult = service.addFamilyAllergyToCollectionIfMissing([], null, familyAllergy, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyAllergy);
      });

      it('should return initial array if no FamilyAllergy is added', () => {
        const familyAllergyCollection: IFamilyAllergy[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyAllergyToCollectionIfMissing(familyAllergyCollection, undefined, null);
        expect(expectedResult).toEqual(familyAllergyCollection);
      });
    });

    describe('compareFamilyAllergy', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFamilyAllergy(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5048 };
        const entity2 = null;

        const compareResult1 = service.compareFamilyAllergy(entity1, entity2);
        const compareResult2 = service.compareFamilyAllergy(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5048 };
        const entity2 = { id: 11017 };

        const compareResult1 = service.compareFamilyAllergy(entity1, entity2);
        const compareResult2 = service.compareFamilyAllergy(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5048 };
        const entity2 = { id: 5048 };

        const compareResult1 = service.compareFamilyAllergy(entity1, entity2);
        const compareResult2 = service.compareFamilyAllergy(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
