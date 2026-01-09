import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPregnancyTetanus, NewPregnancyTetanus } from '../pregnancy-tetanus.model';

export type PartialUpdatePregnancyTetanus = Partial<IPregnancyTetanus> & Pick<IPregnancyTetanus, 'id'>;

type RestOf<T extends IPregnancyTetanus | NewPregnancyTetanus> = Omit<T, 'injectionDate' | 'nextAppointment'> & {
  injectionDate?: string | null;
  nextAppointment?: string | null;
};

export type RestPregnancyTetanus = RestOf<IPregnancyTetanus>;

export type NewRestPregnancyTetanus = RestOf<NewPregnancyTetanus>;

export type PartialUpdateRestPregnancyTetanus = RestOf<PartialUpdatePregnancyTetanus>;

export type EntityResponseType = HttpResponse<IPregnancyTetanus>;
export type EntityArrayResponseType = HttpResponse<IPregnancyTetanus[]>;

@Injectable({ providedIn: 'root' })
export class PregnancyTetanusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pregnancy-tetanuses', 'microservice');

  create(pregnancyTetanus: NewPregnancyTetanus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pregnancyTetanus);
    return this.http
      .post<RestPregnancyTetanus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pregnancyTetanus: IPregnancyTetanus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pregnancyTetanus);
    return this.http
      .put<RestPregnancyTetanus>(`${this.resourceUrl}/${this.getPregnancyTetanusIdentifier(pregnancyTetanus)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pregnancyTetanus: PartialUpdatePregnancyTetanus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pregnancyTetanus);
    return this.http
      .patch<RestPregnancyTetanus>(`${this.resourceUrl}/${this.getPregnancyTetanusIdentifier(pregnancyTetanus)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPregnancyTetanus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPregnancyTetanus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPregnancyTetanusIdentifier(pregnancyTetanus: Pick<IPregnancyTetanus, 'id'>): number {
    return pregnancyTetanus.id;
  }

  comparePregnancyTetanus(o1: Pick<IPregnancyTetanus, 'id'> | null, o2: Pick<IPregnancyTetanus, 'id'> | null): boolean {
    return o1 && o2 ? this.getPregnancyTetanusIdentifier(o1) === this.getPregnancyTetanusIdentifier(o2) : o1 === o2;
  }

  addPregnancyTetanusToCollectionIfMissing<Type extends Pick<IPregnancyTetanus, 'id'>>(
    pregnancyTetanusCollection: Type[],
    ...pregnancyTetanusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pregnancyTetanuses: Type[] = pregnancyTetanusesToCheck.filter(isPresent);
    if (pregnancyTetanuses.length > 0) {
      const pregnancyTetanusCollectionIdentifiers = pregnancyTetanusCollection.map(pregnancyTetanusItem =>
        this.getPregnancyTetanusIdentifier(pregnancyTetanusItem),
      );
      const pregnancyTetanusesToAdd = pregnancyTetanuses.filter(pregnancyTetanusItem => {
        const pregnancyTetanusIdentifier = this.getPregnancyTetanusIdentifier(pregnancyTetanusItem);
        if (pregnancyTetanusCollectionIdentifiers.includes(pregnancyTetanusIdentifier)) {
          return false;
        }
        pregnancyTetanusCollectionIdentifiers.push(pregnancyTetanusIdentifier);
        return true;
      });
      return [...pregnancyTetanusesToAdd, ...pregnancyTetanusCollection];
    }
    return pregnancyTetanusCollection;
  }

  protected convertDateFromClient<T extends IPregnancyTetanus | NewPregnancyTetanus | PartialUpdatePregnancyTetanus>(
    pregnancyTetanus: T,
  ): RestOf<T> {
    return {
      ...pregnancyTetanus,
      injectionDate: pregnancyTetanus.injectionDate?.format(DATE_FORMAT) ?? null,
      nextAppointment: pregnancyTetanus.nextAppointment?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPregnancyTetanus: RestPregnancyTetanus): IPregnancyTetanus {
    return {
      ...restPregnancyTetanus,
      injectionDate: restPregnancyTetanus.injectionDate ? dayjs(restPregnancyTetanus.injectionDate) : undefined,
      nextAppointment: restPregnancyTetanus.nextAppointment ? dayjs(restPregnancyTetanus.nextAppointment) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPregnancyTetanus>): HttpResponse<IPregnancyTetanus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPregnancyTetanus[]>): HttpResponse<IPregnancyTetanus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
