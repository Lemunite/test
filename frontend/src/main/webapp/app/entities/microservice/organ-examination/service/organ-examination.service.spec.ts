import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOrganExamination } from '../organ-examination.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../organ-examination.test-samples';

import { OrganExaminationService } from './organ-examination.service';

const requireRestSample: IOrganExamination = {
  ...sampleWithRequiredData,
};

describe('OrganExamination Service', () => {
  let service: OrganExaminationService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganExamination | IOrganExamination[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrganExaminationService);
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

    it('should create a OrganExamination', () => {
      const organExamination = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organExamination).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganExamination', () => {
      const organExamination = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organExamination).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganExamination', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganExamination', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganExamination', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganExaminationToCollectionIfMissing', () => {
      it('should add a OrganExamination to an empty array', () => {
        const organExamination: IOrganExamination = sampleWithRequiredData;
        expectedResult = service.addOrganExaminationToCollectionIfMissing([], organExamination);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organExamination);
      });

      it('should not add a OrganExamination to an array that contains it', () => {
        const organExamination: IOrganExamination = sampleWithRequiredData;
        const organExaminationCollection: IOrganExamination[] = [
          {
            ...organExamination,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganExaminationToCollectionIfMissing(organExaminationCollection, organExamination);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganExamination to an array that doesn't contain it", () => {
        const organExamination: IOrganExamination = sampleWithRequiredData;
        const organExaminationCollection: IOrganExamination[] = [sampleWithPartialData];
        expectedResult = service.addOrganExaminationToCollectionIfMissing(organExaminationCollection, organExamination);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organExamination);
      });

      it('should add only unique OrganExamination to an array', () => {
        const organExaminationArray: IOrganExamination[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organExaminationCollection: IOrganExamination[] = [sampleWithRequiredData];
        expectedResult = service.addOrganExaminationToCollectionIfMissing(organExaminationCollection, ...organExaminationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organExamination: IOrganExamination = sampleWithRequiredData;
        const organExamination2: IOrganExamination = sampleWithPartialData;
        expectedResult = service.addOrganExaminationToCollectionIfMissing([], organExamination, organExamination2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organExamination);
        expect(expectedResult).toContain(organExamination2);
      });

      it('should accept null and undefined values', () => {
        const organExamination: IOrganExamination = sampleWithRequiredData;
        expectedResult = service.addOrganExaminationToCollectionIfMissing([], null, organExamination, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organExamination);
      });

      it('should return initial array if no OrganExamination is added', () => {
        const organExaminationCollection: IOrganExamination[] = [sampleWithRequiredData];
        expectedResult = service.addOrganExaminationToCollectionIfMissing(organExaminationCollection, undefined, null);
        expect(expectedResult).toEqual(organExaminationCollection);
      });
    });

    describe('compareOrganExamination', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganExamination(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8565 };
        const entity2 = null;

        const compareResult1 = service.compareOrganExamination(entity1, entity2);
        const compareResult2 = service.compareOrganExamination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8565 };
        const entity2 = { id: 28596 };

        const compareResult1 = service.compareOrganExamination(entity1, entity2);
        const compareResult2 = service.compareOrganExamination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8565 };
        const entity2 = { id: 8565 };

        const compareResult1 = service.compareOrganExamination(entity1, entity2);
        const compareResult2 = service.compareOrganExamination(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
