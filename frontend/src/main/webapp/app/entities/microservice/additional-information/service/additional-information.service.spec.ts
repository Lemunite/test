import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAdditionalInformation } from '../additional-information.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../additional-information.test-samples';

import { AdditionalInformationService, RestAdditionalInformation } from './additional-information.service';

const requireRestSample: RestAdditionalInformation = {
  ...sampleWithRequiredData,
  exposureDate: sampleWithRequiredData.exposureDate?.format(DATE_FORMAT),
};

describe('AdditionalInformation Service', () => {
  let service: AdditionalInformationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdditionalInformation | IAdditionalInformation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdditionalInformationService);
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

    it('should create a AdditionalInformation', () => {
      const additionalInformation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(additionalInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AdditionalInformation', () => {
      const additionalInformation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(additionalInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AdditionalInformation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AdditionalInformation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AdditionalInformation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdditionalInformationToCollectionIfMissing', () => {
      it('should add a AdditionalInformation to an empty array', () => {
        const additionalInformation: IAdditionalInformation = sampleWithRequiredData;
        expectedResult = service.addAdditionalInformationToCollectionIfMissing([], additionalInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalInformation);
      });

      it('should not add a AdditionalInformation to an array that contains it', () => {
        const additionalInformation: IAdditionalInformation = sampleWithRequiredData;
        const additionalInformationCollection: IAdditionalInformation[] = [
          {
            ...additionalInformation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdditionalInformationToCollectionIfMissing(additionalInformationCollection, additionalInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AdditionalInformation to an array that doesn't contain it", () => {
        const additionalInformation: IAdditionalInformation = sampleWithRequiredData;
        const additionalInformationCollection: IAdditionalInformation[] = [sampleWithPartialData];
        expectedResult = service.addAdditionalInformationToCollectionIfMissing(additionalInformationCollection, additionalInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalInformation);
      });

      it('should add only unique AdditionalInformation to an array', () => {
        const additionalInformationArray: IAdditionalInformation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const additionalInformationCollection: IAdditionalInformation[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalInformationToCollectionIfMissing(
          additionalInformationCollection,
          ...additionalInformationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const additionalInformation: IAdditionalInformation = sampleWithRequiredData;
        const additionalInformation2: IAdditionalInformation = sampleWithPartialData;
        expectedResult = service.addAdditionalInformationToCollectionIfMissing([], additionalInformation, additionalInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalInformation);
        expect(expectedResult).toContain(additionalInformation2);
      });

      it('should accept null and undefined values', () => {
        const additionalInformation: IAdditionalInformation = sampleWithRequiredData;
        expectedResult = service.addAdditionalInformationToCollectionIfMissing([], null, additionalInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalInformation);
      });

      it('should return initial array if no AdditionalInformation is added', () => {
        const additionalInformationCollection: IAdditionalInformation[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalInformationToCollectionIfMissing(additionalInformationCollection, undefined, null);
        expect(expectedResult).toEqual(additionalInformationCollection);
      });
    });

    describe('compareAdditionalInformation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdditionalInformation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11732 };
        const entity2 = null;

        const compareResult1 = service.compareAdditionalInformation(entity1, entity2);
        const compareResult2 = service.compareAdditionalInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11732 };
        const entity2 = { id: 21718 };

        const compareResult1 = service.compareAdditionalInformation(entity1, entity2);
        const compareResult2 = service.compareAdditionalInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11732 };
        const entity2 = { id: 11732 };

        const compareResult1 = service.compareAdditionalInformation(entity1, entity2);
        const compareResult2 = service.compareAdditionalInformation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
