import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParaclinicalResult, NewParaclinicalResult } from '../paraclinical-result.model';

export type PartialUpdateParaclinicalResult = Partial<IParaclinicalResult> & Pick<IParaclinicalResult, 'id'>;

type RestOf<T extends IParaclinicalResult | NewParaclinicalResult> = Omit<T, 'resultDate'> & {
  resultDate?: string | null;
};

export type RestParaclinicalResult = RestOf<IParaclinicalResult>;

export type NewRestParaclinicalResult = RestOf<NewParaclinicalResult>;

export type PartialUpdateRestParaclinicalResult = RestOf<PartialUpdateParaclinicalResult>;

export type EntityResponseType = HttpResponse<IParaclinicalResult>;
export type EntityArrayResponseType = HttpResponse<IParaclinicalResult[]>;

@Injectable({ providedIn: 'root' })
export class ParaclinicalResultService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paraclinical-results', 'microservice');

  create(paraclinicalResult: NewParaclinicalResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paraclinicalResult);
    return this.http
      .post<RestParaclinicalResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paraclinicalResult: IParaclinicalResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paraclinicalResult);
    return this.http
      .put<RestParaclinicalResult>(`${this.resourceUrl}/${this.getParaclinicalResultIdentifier(paraclinicalResult)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paraclinicalResult: PartialUpdateParaclinicalResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paraclinicalResult);
    return this.http
      .patch<RestParaclinicalResult>(`${this.resourceUrl}/${this.getParaclinicalResultIdentifier(paraclinicalResult)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestParaclinicalResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParaclinicalResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParaclinicalResultIdentifier(paraclinicalResult: Pick<IParaclinicalResult, 'id'>): number {
    return paraclinicalResult.id;
  }

  compareParaclinicalResult(o1: Pick<IParaclinicalResult, 'id'> | null, o2: Pick<IParaclinicalResult, 'id'> | null): boolean {
    return o1 && o2 ? this.getParaclinicalResultIdentifier(o1) === this.getParaclinicalResultIdentifier(o2) : o1 === o2;
  }

  addParaclinicalResultToCollectionIfMissing<Type extends Pick<IParaclinicalResult, 'id'>>(
    paraclinicalResultCollection: Type[],
    ...paraclinicalResultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paraclinicalResults: Type[] = paraclinicalResultsToCheck.filter(isPresent);
    if (paraclinicalResults.length > 0) {
      const paraclinicalResultCollectionIdentifiers = paraclinicalResultCollection.map(paraclinicalResultItem =>
        this.getParaclinicalResultIdentifier(paraclinicalResultItem),
      );
      const paraclinicalResultsToAdd = paraclinicalResults.filter(paraclinicalResultItem => {
        const paraclinicalResultIdentifier = this.getParaclinicalResultIdentifier(paraclinicalResultItem);
        if (paraclinicalResultCollectionIdentifiers.includes(paraclinicalResultIdentifier)) {
          return false;
        }
        paraclinicalResultCollectionIdentifiers.push(paraclinicalResultIdentifier);
        return true;
      });
      return [...paraclinicalResultsToAdd, ...paraclinicalResultCollection];
    }
    return paraclinicalResultCollection;
  }

  protected convertDateFromClient<T extends IParaclinicalResult | NewParaclinicalResult | PartialUpdateParaclinicalResult>(
    paraclinicalResult: T,
  ): RestOf<T> {
    return {
      ...paraclinicalResult,
      resultDate: paraclinicalResult.resultDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restParaclinicalResult: RestParaclinicalResult): IParaclinicalResult {
    return {
      ...restParaclinicalResult,
      resultDate: restParaclinicalResult.resultDate ? dayjs(restParaclinicalResult.resultDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestParaclinicalResult>): HttpResponse<IParaclinicalResult> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestParaclinicalResult[]>): HttpResponse<IParaclinicalResult[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
