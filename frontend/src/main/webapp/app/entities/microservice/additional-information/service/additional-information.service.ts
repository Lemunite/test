import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdditionalInformation, NewAdditionalInformation } from '../additional-information.model';

export type PartialUpdateAdditionalInformation = Partial<IAdditionalInformation> & Pick<IAdditionalInformation, 'id'>;

type RestOf<T extends IAdditionalInformation | NewAdditionalInformation> = Omit<T, 'exposureDate'> & {
  exposureDate?: string | null;
};

export type RestAdditionalInformation = RestOf<IAdditionalInformation>;

export type NewRestAdditionalInformation = RestOf<NewAdditionalInformation>;

export type PartialUpdateRestAdditionalInformation = RestOf<PartialUpdateAdditionalInformation>;

export type EntityResponseType = HttpResponse<IAdditionalInformation>;
export type EntityArrayResponseType = HttpResponse<IAdditionalInformation[]>;

@Injectable({ providedIn: 'root' })
export class AdditionalInformationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/additional-informations', 'microservice');

  create(additionalInformation: NewAdditionalInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalInformation);
    return this.http
      .post<RestAdditionalInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(additionalInformation: IAdditionalInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalInformation);
    return this.http
      .put<RestAdditionalInformation>(`${this.resourceUrl}/${this.getAdditionalInformationIdentifier(additionalInformation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(additionalInformation: PartialUpdateAdditionalInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalInformation);
    return this.http
      .patch<RestAdditionalInformation>(`${this.resourceUrl}/${this.getAdditionalInformationIdentifier(additionalInformation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAdditionalInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAdditionalInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdditionalInformationIdentifier(additionalInformation: Pick<IAdditionalInformation, 'id'>): number {
    return additionalInformation.id;
  }

  compareAdditionalInformation(o1: Pick<IAdditionalInformation, 'id'> | null, o2: Pick<IAdditionalInformation, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdditionalInformationIdentifier(o1) === this.getAdditionalInformationIdentifier(o2) : o1 === o2;
  }

  addAdditionalInformationToCollectionIfMissing<Type extends Pick<IAdditionalInformation, 'id'>>(
    additionalInformationCollection: Type[],
    ...additionalInformationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const additionalInformations: Type[] = additionalInformationsToCheck.filter(isPresent);
    if (additionalInformations.length > 0) {
      const additionalInformationCollectionIdentifiers = additionalInformationCollection.map(additionalInformationItem =>
        this.getAdditionalInformationIdentifier(additionalInformationItem),
      );
      const additionalInformationsToAdd = additionalInformations.filter(additionalInformationItem => {
        const additionalInformationIdentifier = this.getAdditionalInformationIdentifier(additionalInformationItem);
        if (additionalInformationCollectionIdentifiers.includes(additionalInformationIdentifier)) {
          return false;
        }
        additionalInformationCollectionIdentifiers.push(additionalInformationIdentifier);
        return true;
      });
      return [...additionalInformationsToAdd, ...additionalInformationCollection];
    }
    return additionalInformationCollection;
  }

  protected convertDateFromClient<T extends IAdditionalInformation | NewAdditionalInformation | PartialUpdateAdditionalInformation>(
    additionalInformation: T,
  ): RestOf<T> {
    return {
      ...additionalInformation,
      exposureDate: additionalInformation.exposureDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAdditionalInformation: RestAdditionalInformation): IAdditionalInformation {
    return {
      ...restAdditionalInformation,
      exposureDate: restAdditionalInformation.exposureDate ? dayjs(restAdditionalInformation.exposureDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAdditionalInformation>): HttpResponse<IAdditionalInformation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAdditionalInformation[]>): HttpResponse<IAdditionalInformation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
