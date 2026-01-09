import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVaccinationForBaby } from '../vaccination-for-baby.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vaccination-for-baby.test-samples';

import { VaccinationForBabyService } from './vaccination-for-baby.service';

const requireRestSample: IVaccinationForBaby = {
  ...sampleWithRequiredData,
};

describe('VaccinationForBaby Service', () => {
  let service: VaccinationForBabyService;
  let httpMock: HttpTestingController;
  let expectedResult: IVaccinationForBaby | IVaccinationForBaby[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VaccinationForBabyService);
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

    it('should create a VaccinationForBaby', () => {
      const vaccinationForBaby = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vaccinationForBaby).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VaccinationForBaby', () => {
      const vaccinationForBaby = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vaccinationForBaby).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VaccinationForBaby', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VaccinationForBaby', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VaccinationForBaby', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVaccinationForBabyToCollectionIfMissing', () => {
      it('should add a VaccinationForBaby to an empty array', () => {
        const vaccinationForBaby: IVaccinationForBaby = sampleWithRequiredData;
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing([], vaccinationForBaby);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccinationForBaby);
      });

      it('should not add a VaccinationForBaby to an array that contains it', () => {
        const vaccinationForBaby: IVaccinationForBaby = sampleWithRequiredData;
        const vaccinationForBabyCollection: IVaccinationForBaby[] = [
          {
            ...vaccinationForBaby,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing(vaccinationForBabyCollection, vaccinationForBaby);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VaccinationForBaby to an array that doesn't contain it", () => {
        const vaccinationForBaby: IVaccinationForBaby = sampleWithRequiredData;
        const vaccinationForBabyCollection: IVaccinationForBaby[] = [sampleWithPartialData];
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing(vaccinationForBabyCollection, vaccinationForBaby);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccinationForBaby);
      });

      it('should add only unique VaccinationForBaby to an array', () => {
        const vaccinationForBabyArray: IVaccinationForBaby[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vaccinationForBabyCollection: IVaccinationForBaby[] = [sampleWithRequiredData];
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing(vaccinationForBabyCollection, ...vaccinationForBabyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vaccinationForBaby: IVaccinationForBaby = sampleWithRequiredData;
        const vaccinationForBaby2: IVaccinationForBaby = sampleWithPartialData;
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing([], vaccinationForBaby, vaccinationForBaby2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccinationForBaby);
        expect(expectedResult).toContain(vaccinationForBaby2);
      });

      it('should accept null and undefined values', () => {
        const vaccinationForBaby: IVaccinationForBaby = sampleWithRequiredData;
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing([], null, vaccinationForBaby, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccinationForBaby);
      });

      it('should return initial array if no VaccinationForBaby is added', () => {
        const vaccinationForBabyCollection: IVaccinationForBaby[] = [sampleWithRequiredData];
        expectedResult = service.addVaccinationForBabyToCollectionIfMissing(vaccinationForBabyCollection, undefined, null);
        expect(expectedResult).toEqual(vaccinationForBabyCollection);
      });
    });

    describe('compareVaccinationForBaby', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVaccinationForBaby(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8159 };
        const entity2 = null;

        const compareResult1 = service.compareVaccinationForBaby(entity1, entity2);
        const compareResult2 = service.compareVaccinationForBaby(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8159 };
        const entity2 = { id: 31096 };

        const compareResult1 = service.compareVaccinationForBaby(entity1, entity2);
        const compareResult2 = service.compareVaccinationForBaby(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8159 };
        const entity2 = { id: 8159 };

        const compareResult1 = service.compareVaccinationForBaby(entity1, entity2);
        const compareResult2 = service.compareVaccinationForBaby(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
