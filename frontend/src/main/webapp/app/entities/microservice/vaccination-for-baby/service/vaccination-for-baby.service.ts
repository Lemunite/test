import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaccinationForBaby, NewVaccinationForBaby } from '../vaccination-for-baby.model';

export type PartialUpdateVaccinationForBaby = Partial<IVaccinationForBaby> & Pick<IVaccinationForBaby, 'id'>;

export type EntityResponseType = HttpResponse<IVaccinationForBaby>;
export type EntityArrayResponseType = HttpResponse<IVaccinationForBaby[]>;

@Injectable({ providedIn: 'root' })
export class VaccinationForBabyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaccination-for-babies', 'microservice');

  create(vaccinationForBaby: NewVaccinationForBaby): Observable<EntityResponseType> {
    return this.http.post<IVaccinationForBaby>(this.resourceUrl, vaccinationForBaby, { observe: 'response' });
  }

  update(vaccinationForBaby: IVaccinationForBaby): Observable<EntityResponseType> {
    return this.http.put<IVaccinationForBaby>(
      `${this.resourceUrl}/${this.getVaccinationForBabyIdentifier(vaccinationForBaby)}`,
      vaccinationForBaby,
      { observe: 'response' },
    );
  }

  partialUpdate(vaccinationForBaby: PartialUpdateVaccinationForBaby): Observable<EntityResponseType> {
    return this.http.patch<IVaccinationForBaby>(
      `${this.resourceUrl}/${this.getVaccinationForBabyIdentifier(vaccinationForBaby)}`,
      vaccinationForBaby,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVaccinationForBaby>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVaccinationForBaby[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVaccinationForBabyIdentifier(vaccinationForBaby: Pick<IVaccinationForBaby, 'id'>): number {
    return vaccinationForBaby.id;
  }

  compareVaccinationForBaby(o1: Pick<IVaccinationForBaby, 'id'> | null, o2: Pick<IVaccinationForBaby, 'id'> | null): boolean {
    return o1 && o2 ? this.getVaccinationForBabyIdentifier(o1) === this.getVaccinationForBabyIdentifier(o2) : o1 === o2;
  }

  addVaccinationForBabyToCollectionIfMissing<Type extends Pick<IVaccinationForBaby, 'id'>>(
    vaccinationForBabyCollection: Type[],
    ...vaccinationForBabiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vaccinationForBabies: Type[] = vaccinationForBabiesToCheck.filter(isPresent);
    if (vaccinationForBabies.length > 0) {
      const vaccinationForBabyCollectionIdentifiers = vaccinationForBabyCollection.map(vaccinationForBabyItem =>
        this.getVaccinationForBabyIdentifier(vaccinationForBabyItem),
      );
      const vaccinationForBabiesToAdd = vaccinationForBabies.filter(vaccinationForBabyItem => {
        const vaccinationForBabyIdentifier = this.getVaccinationForBabyIdentifier(vaccinationForBabyItem);
        if (vaccinationForBabyCollectionIdentifiers.includes(vaccinationForBabyIdentifier)) {
          return false;
        }
        vaccinationForBabyCollectionIdentifiers.push(vaccinationForBabyIdentifier);
        return true;
      });
      return [...vaccinationForBabiesToAdd, ...vaccinationForBabyCollection];
    }
    return vaccinationForBabyCollection;
  }
}
