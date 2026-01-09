import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVaccinationTCMR } from '../vaccination-tcmr.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vaccination-tcmr.test-samples';

import { RestVaccinationTCMR, VaccinationTCMRService } from './vaccination-tcmr.service';

const requireRestSample: RestVaccinationTCMR = {
  ...sampleWithRequiredData,
  injectionDate: sampleWithRequiredData.injectionDate?.format(DATE_FORMAT),
  nextAppointment: sampleWithRequiredData.nextAppointment?.format(DATE_FORMAT),
};

describe('VaccinationTCMR Service', () => {
  let service: VaccinationTCMRService;
  let httpMock: HttpTestingController;
  let expectedResult: IVaccinationTCMR | IVaccinationTCMR[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VaccinationTCMRService);
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

    it('should create a VaccinationTCMR', () => {
      const vaccinationTCMR = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vaccinationTCMR).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VaccinationTCMR', () => {
      const vaccinationTCMR = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vaccinationTCMR).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VaccinationTCMR', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VaccinationTCMR', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VaccinationTCMR', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVaccinationTCMRToCollectionIfMissing', () => {
      it('should add a VaccinationTCMR to an empty array', () => {
        const vaccinationTCMR: IVaccinationTCMR = sampleWithRequiredData;
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing([], vaccinationTCMR);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccinationTCMR);
      });

      it('should not add a VaccinationTCMR to an array that contains it', () => {
        const vaccinationTCMR: IVaccinationTCMR = sampleWithRequiredData;
        const vaccinationTCMRCollection: IVaccinationTCMR[] = [
          {
            ...vaccinationTCMR,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing(vaccinationTCMRCollection, vaccinationTCMR);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VaccinationTCMR to an array that doesn't contain it", () => {
        const vaccinationTCMR: IVaccinationTCMR = sampleWithRequiredData;
        const vaccinationTCMRCollection: IVaccinationTCMR[] = [sampleWithPartialData];
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing(vaccinationTCMRCollection, vaccinationTCMR);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccinationTCMR);
      });

      it('should add only unique VaccinationTCMR to an array', () => {
        const vaccinationTCMRArray: IVaccinationTCMR[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vaccinationTCMRCollection: IVaccinationTCMR[] = [sampleWithRequiredData];
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing(vaccinationTCMRCollection, ...vaccinationTCMRArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vaccinationTCMR: IVaccinationTCMR = sampleWithRequiredData;
        const vaccinationTCMR2: IVaccinationTCMR = sampleWithPartialData;
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing([], vaccinationTCMR, vaccinationTCMR2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccinationTCMR);
        expect(expectedResult).toContain(vaccinationTCMR2);
      });

      it('should accept null and undefined values', () => {
        const vaccinationTCMR: IVaccinationTCMR = sampleWithRequiredData;
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing([], null, vaccinationTCMR, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccinationTCMR);
      });

      it('should return initial array if no VaccinationTCMR is added', () => {
        const vaccinationTCMRCollection: IVaccinationTCMR[] = [sampleWithRequiredData];
        expectedResult = service.addVaccinationTCMRToCollectionIfMissing(vaccinationTCMRCollection, undefined, null);
        expect(expectedResult).toEqual(vaccinationTCMRCollection);
      });
    });

    describe('compareVaccinationTCMR', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVaccinationTCMR(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32463 };
        const entity2 = null;

        const compareResult1 = service.compareVaccinationTCMR(entity1, entity2);
        const compareResult2 = service.compareVaccinationTCMR(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32463 };
        const entity2 = { id: 2095 };

        const compareResult1 = service.compareVaccinationTCMR(entity1, entity2);
        const compareResult2 = service.compareVaccinationTCMR(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32463 };
        const entity2 = { id: 32463 };

        const compareResult1 = service.compareVaccinationTCMR(entity1, entity2);
        const compareResult2 = service.compareVaccinationTCMR(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
