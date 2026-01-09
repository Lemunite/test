import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaccine, NewVaccine } from '../vaccine.model';

export type PartialUpdateVaccine = Partial<IVaccine> & Pick<IVaccine, 'id'>;

type RestOf<T extends IVaccine | NewVaccine> = Omit<T, 'injectionDate' | 'nextAppointment'> & {
  injectionDate?: string | null;
  nextAppointment?: string | null;
};

export type RestVaccine = RestOf<IVaccine>;

export type NewRestVaccine = RestOf<NewVaccine>;

export type PartialUpdateRestVaccine = RestOf<PartialUpdateVaccine>;

export type EntityResponseType = HttpResponse<IVaccine>;
export type EntityArrayResponseType = HttpResponse<IVaccine[]>;

@Injectable({ providedIn: 'root' })
export class VaccineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaccines', 'microservice');

  create(vaccine: NewVaccine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccine);
    return this.http
      .post<RestVaccine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vaccine: IVaccine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccine);
    return this.http
      .put<RestVaccine>(`${this.resourceUrl}/${this.getVaccineIdentifier(vaccine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vaccine: PartialUpdateVaccine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccine);
    return this.http
      .patch<RestVaccine>(`${this.resourceUrl}/${this.getVaccineIdentifier(vaccine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVaccine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVaccine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVaccineIdentifier(vaccine: Pick<IVaccine, 'id'>): number {
    return vaccine.id;
  }

  compareVaccine(o1: Pick<IVaccine, 'id'> | null, o2: Pick<IVaccine, 'id'> | null): boolean {
    return o1 && o2 ? this.getVaccineIdentifier(o1) === this.getVaccineIdentifier(o2) : o1 === o2;
  }

  addVaccineToCollectionIfMissing<Type extends Pick<IVaccine, 'id'>>(
    vaccineCollection: Type[],
    ...vaccinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vaccines: Type[] = vaccinesToCheck.filter(isPresent);
    if (vaccines.length > 0) {
      const vaccineCollectionIdentifiers = vaccineCollection.map(vaccineItem => this.getVaccineIdentifier(vaccineItem));
      const vaccinesToAdd = vaccines.filter(vaccineItem => {
        const vaccineIdentifier = this.getVaccineIdentifier(vaccineItem);
        if (vaccineCollectionIdentifiers.includes(vaccineIdentifier)) {
          return false;
        }
        vaccineCollectionIdentifiers.push(vaccineIdentifier);
        return true;
      });
      return [...vaccinesToAdd, ...vaccineCollection];
    }
    return vaccineCollection;
  }

  protected convertDateFromClient<T extends IVaccine | NewVaccine | PartialUpdateVaccine>(vaccine: T): RestOf<T> {
    return {
      ...vaccine,
      injectionDate: vaccine.injectionDate?.format(DATE_FORMAT) ?? null,
      nextAppointment: vaccine.nextAppointment?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVaccine: RestVaccine): IVaccine {
    return {
      ...restVaccine,
      injectionDate: restVaccine.injectionDate ? dayjs(restVaccine.injectionDate) : undefined,
      nextAppointment: restVaccine.nextAppointment ? dayjs(restVaccine.nextAppointment) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVaccine>): HttpResponse<IVaccine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVaccine[]>): HttpResponse<IVaccine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
