import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganExamination, NewOrganExamination } from '../organ-examination.model';

export type PartialUpdateOrganExamination = Partial<IOrganExamination> & Pick<IOrganExamination, 'id'>;

export type EntityResponseType = HttpResponse<IOrganExamination>;
export type EntityArrayResponseType = HttpResponse<IOrganExamination[]>;

@Injectable({ providedIn: 'root' })
export class OrganExaminationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organ-examinations', 'microservice');

  create(organExamination: NewOrganExamination): Observable<EntityResponseType> {
    return this.http.post<IOrganExamination>(this.resourceUrl, organExamination, { observe: 'response' });
  }

  update(organExamination: IOrganExamination): Observable<EntityResponseType> {
    return this.http.put<IOrganExamination>(
      `${this.resourceUrl}/${this.getOrganExaminationIdentifier(organExamination)}`,
      organExamination,
      { observe: 'response' },
    );
  }

  partialUpdate(organExamination: PartialUpdateOrganExamination): Observable<EntityResponseType> {
    return this.http.patch<IOrganExamination>(
      `${this.resourceUrl}/${this.getOrganExaminationIdentifier(organExamination)}`,
      organExamination,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganExamination>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganExamination[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganExaminationIdentifier(organExamination: Pick<IOrganExamination, 'id'>): number {
    return organExamination.id;
  }

  compareOrganExamination(o1: Pick<IOrganExamination, 'id'> | null, o2: Pick<IOrganExamination, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganExaminationIdentifier(o1) === this.getOrganExaminationIdentifier(o2) : o1 === o2;
  }

  addOrganExaminationToCollectionIfMissing<Type extends Pick<IOrganExamination, 'id'>>(
    organExaminationCollection: Type[],
    ...organExaminationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organExaminations: Type[] = organExaminationsToCheck.filter(isPresent);
    if (organExaminations.length > 0) {
      const organExaminationCollectionIdentifiers = organExaminationCollection.map(organExaminationItem =>
        this.getOrganExaminationIdentifier(organExaminationItem),
      );
      const organExaminationsToAdd = organExaminations.filter(organExaminationItem => {
        const organExaminationIdentifier = this.getOrganExaminationIdentifier(organExaminationItem);
        if (organExaminationCollectionIdentifiers.includes(organExaminationIdentifier)) {
          return false;
        }
        organExaminationCollectionIdentifiers.push(organExaminationIdentifier);
        return true;
      });
      return [...organExaminationsToAdd, ...organExaminationCollection];
    }
    return organExaminationCollection;
  }
}
