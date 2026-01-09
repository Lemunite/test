import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaccinationTCMR, NewVaccinationTCMR } from '../vaccination-tcmr.model';

export type PartialUpdateVaccinationTCMR = Partial<IVaccinationTCMR> & Pick<IVaccinationTCMR, 'id'>;

type RestOf<T extends IVaccinationTCMR | NewVaccinationTCMR> = Omit<T, 'injectionDate' | 'nextAppointment'> & {
  injectionDate?: string | null;
  nextAppointment?: string | null;
};

export type RestVaccinationTCMR = RestOf<IVaccinationTCMR>;

export type NewRestVaccinationTCMR = RestOf<NewVaccinationTCMR>;

export type PartialUpdateRestVaccinationTCMR = RestOf<PartialUpdateVaccinationTCMR>;

export type EntityResponseType = HttpResponse<IVaccinationTCMR>;
export type EntityArrayResponseType = HttpResponse<IVaccinationTCMR[]>;

@Injectable({ providedIn: 'root' })
export class VaccinationTCMRService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaccination-tcmrs', 'microservice');

  create(vaccinationTCMR: NewVaccinationTCMR): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccinationTCMR);
    return this.http
      .post<RestVaccinationTCMR>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vaccinationTCMR: IVaccinationTCMR): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccinationTCMR);
    return this.http
      .put<RestVaccinationTCMR>(`${this.resourceUrl}/${this.getVaccinationTCMRIdentifier(vaccinationTCMR)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vaccinationTCMR: PartialUpdateVaccinationTCMR): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccinationTCMR);
    return this.http
      .patch<RestVaccinationTCMR>(`${this.resourceUrl}/${this.getVaccinationTCMRIdentifier(vaccinationTCMR)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVaccinationTCMR>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVaccinationTCMR[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVaccinationTCMRIdentifier(vaccinationTCMR: Pick<IVaccinationTCMR, 'id'>): number {
    return vaccinationTCMR.id;
  }

  compareVaccinationTCMR(o1: Pick<IVaccinationTCMR, 'id'> | null, o2: Pick<IVaccinationTCMR, 'id'> | null): boolean {
    return o1 && o2 ? this.getVaccinationTCMRIdentifier(o1) === this.getVaccinationTCMRIdentifier(o2) : o1 === o2;
  }

  addVaccinationTCMRToCollectionIfMissing<Type extends Pick<IVaccinationTCMR, 'id'>>(
    vaccinationTCMRCollection: Type[],
    ...vaccinationTCMRSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vaccinationTCMRS: Type[] = vaccinationTCMRSToCheck.filter(isPresent);
    if (vaccinationTCMRS.length > 0) {
      const vaccinationTCMRCollectionIdentifiers = vaccinationTCMRCollection.map(vaccinationTCMRItem =>
        this.getVaccinationTCMRIdentifier(vaccinationTCMRItem),
      );
      const vaccinationTCMRSToAdd = vaccinationTCMRS.filter(vaccinationTCMRItem => {
        const vaccinationTCMRIdentifier = this.getVaccinationTCMRIdentifier(vaccinationTCMRItem);
        if (vaccinationTCMRCollectionIdentifiers.includes(vaccinationTCMRIdentifier)) {
          return false;
        }
        vaccinationTCMRCollectionIdentifiers.push(vaccinationTCMRIdentifier);
        return true;
      });
      return [...vaccinationTCMRSToAdd, ...vaccinationTCMRCollection];
    }
    return vaccinationTCMRCollection;
  }

  protected convertDateFromClient<T extends IVaccinationTCMR | NewVaccinationTCMR | PartialUpdateVaccinationTCMR>(
    vaccinationTCMR: T,
  ): RestOf<T> {
    return {
      ...vaccinationTCMR,
      injectionDate: vaccinationTCMR.injectionDate?.format(DATE_FORMAT) ?? null,
      nextAppointment: vaccinationTCMR.nextAppointment?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVaccinationTCMR: RestVaccinationTCMR): IVaccinationTCMR {
    return {
      ...restVaccinationTCMR,
      injectionDate: restVaccinationTCMR.injectionDate ? dayjs(restVaccinationTCMR.injectionDate) : undefined,
      nextAppointment: restVaccinationTCMR.nextAppointment ? dayjs(restVaccinationTCMR.nextAppointment) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVaccinationTCMR>): HttpResponse<IVaccinationTCMR> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVaccinationTCMR[]>): HttpResponse<IVaccinationTCMR[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
