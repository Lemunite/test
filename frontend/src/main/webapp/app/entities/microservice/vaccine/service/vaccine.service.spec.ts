import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVaccine } from '../vaccine.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vaccine.test-samples';

import { RestVaccine, VaccineService } from './vaccine.service';

const requireRestSample: RestVaccine = {
  ...sampleWithRequiredData,
  injectionDate: sampleWithRequiredData.injectionDate?.format(DATE_FORMAT),
  nextAppointment: sampleWithRequiredData.nextAppointment?.format(DATE_FORMAT),
};

describe('Vaccine Service', () => {
  let service: VaccineService;
  let httpMock: HttpTestingController;
  let expectedResult: IVaccine | IVaccine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VaccineService);
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

    it('should create a Vaccine', () => {
      const vaccine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vaccine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vaccine', () => {
      const vaccine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vaccine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vaccine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vaccine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vaccine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVaccineToCollectionIfMissing', () => {
      it('should add a Vaccine to an empty array', () => {
        const vaccine: IVaccine = sampleWithRequiredData;
        expectedResult = service.addVaccineToCollectionIfMissing([], vaccine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccine);
      });

      it('should not add a Vaccine to an array that contains it', () => {
        const vaccine: IVaccine = sampleWithRequiredData;
        const vaccineCollection: IVaccine[] = [
          {
            ...vaccine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVaccineToCollectionIfMissing(vaccineCollection, vaccine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vaccine to an array that doesn't contain it", () => {
        const vaccine: IVaccine = sampleWithRequiredData;
        const vaccineCollection: IVaccine[] = [sampleWithPartialData];
        expectedResult = service.addVaccineToCollectionIfMissing(vaccineCollection, vaccine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccine);
      });

      it('should add only unique Vaccine to an array', () => {
        const vaccineArray: IVaccine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vaccineCollection: IVaccine[] = [sampleWithRequiredData];
        expectedResult = service.addVaccineToCollectionIfMissing(vaccineCollection, ...vaccineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vaccine: IVaccine = sampleWithRequiredData;
        const vaccine2: IVaccine = sampleWithPartialData;
        expectedResult = service.addVaccineToCollectionIfMissing([], vaccine, vaccine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaccine);
        expect(expectedResult).toContain(vaccine2);
      });

      it('should accept null and undefined values', () => {
        const vaccine: IVaccine = sampleWithRequiredData;
        expectedResult = service.addVaccineToCollectionIfMissing([], null, vaccine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaccine);
      });

      it('should return initial array if no Vaccine is added', () => {
        const vaccineCollection: IVaccine[] = [sampleWithRequiredData];
        expectedResult = service.addVaccineToCollectionIfMissing(vaccineCollection, undefined, null);
        expect(expectedResult).toEqual(vaccineCollection);
      });
    });

    describe('compareVaccine', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVaccine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27715 };
        const entity2 = null;

        const compareResult1 = service.compareVaccine(entity1, entity2);
        const compareResult2 = service.compareVaccine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27715 };
        const entity2 = { id: 10849 };

        const compareResult1 = service.compareVaccine(entity1, entity2);
        const compareResult2 = service.compareVaccine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27715 };
        const entity2 = { id: 27715 };

        const compareResult1 = service.compareVaccine(entity1, entity2);
        const compareResult2 = service.compareVaccine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
