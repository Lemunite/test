import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISurgeryHistory, NewSurgeryHistory } from '../surgery-history.model';

export type PartialUpdateSurgeryHistory = Partial<ISurgeryHistory> & Pick<ISurgeryHistory, 'id'>;

export type EntityResponseType = HttpResponse<ISurgeryHistory>;
export type EntityArrayResponseType = HttpResponse<ISurgeryHistory[]>;

@Injectable({ providedIn: 'root' })
export class SurgeryHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surgery-histories', 'microservice');

  create(surgeryHistory: NewSurgeryHistory): Observable<EntityResponseType> {
    return this.http.post<ISurgeryHistory>(this.resourceUrl, surgeryHistory, { observe: 'response' });
  }

  update(surgeryHistory: ISurgeryHistory): Observable<EntityResponseType> {
    return this.http.put<ISurgeryHistory>(`${this.resourceUrl}/${this.getSurgeryHistoryIdentifier(surgeryHistory)}`, surgeryHistory, {
      observe: 'response',
    });
  }

  partialUpdate(surgeryHistory: PartialUpdateSurgeryHistory): Observable<EntityResponseType> {
    return this.http.patch<ISurgeryHistory>(`${this.resourceUrl}/${this.getSurgeryHistoryIdentifier(surgeryHistory)}`, surgeryHistory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISurgeryHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISurgeryHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSurgeryHistoryIdentifier(surgeryHistory: Pick<ISurgeryHistory, 'id'>): number {
    return surgeryHistory.id;
  }

  compareSurgeryHistory(o1: Pick<ISurgeryHistory, 'id'> | null, o2: Pick<ISurgeryHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getSurgeryHistoryIdentifier(o1) === this.getSurgeryHistoryIdentifier(o2) : o1 === o2;
  }

  addSurgeryHistoryToCollectionIfMissing<Type extends Pick<ISurgeryHistory, 'id'>>(
    surgeryHistoryCollection: Type[],
    ...surgeryHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const surgeryHistories: Type[] = surgeryHistoriesToCheck.filter(isPresent);
    if (surgeryHistories.length > 0) {
      const surgeryHistoryCollectionIdentifiers = surgeryHistoryCollection.map(surgeryHistoryItem =>
        this.getSurgeryHistoryIdentifier(surgeryHistoryItem),
      );
      const surgeryHistoriesToAdd = surgeryHistories.filter(surgeryHistoryItem => {
        const surgeryHistoryIdentifier = this.getSurgeryHistoryIdentifier(surgeryHistoryItem);
        if (surgeryHistoryCollectionIdentifiers.includes(surgeryHistoryIdentifier)) {
          return false;
        }
        surgeryHistoryCollectionIdentifiers.push(surgeryHistoryIdentifier);
        return true;
      });
      return [...surgeryHistoriesToAdd, ...surgeryHistoryCollection];
    }
    return surgeryHistoryCollection;
  }
}
