import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAllergy, NewAllergy } from '../allergy.model';

export type PartialUpdateAllergy = Partial<IAllergy> & Pick<IAllergy, 'id'>;

export type EntityResponseType = HttpResponse<IAllergy>;
export type EntityArrayResponseType = HttpResponse<IAllergy[]>;

@Injectable({ providedIn: 'root' })
export class AllergyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/allergies', 'microservice');

  create(allergy: NewAllergy): Observable<EntityResponseType> {
    return this.http.post<IAllergy>(this.resourceUrl, allergy, { observe: 'response' });
  }

  update(allergy: IAllergy): Observable<EntityResponseType> {
    return this.http.put<IAllergy>(`${this.resourceUrl}/${this.getAllergyIdentifier(allergy)}`, allergy, { observe: 'response' });
  }

  partialUpdate(allergy: PartialUpdateAllergy): Observable<EntityResponseType> {
    return this.http.patch<IAllergy>(`${this.resourceUrl}/${this.getAllergyIdentifier(allergy)}`, allergy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAllergy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAllergy[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllergyIdentifier(allergy: Pick<IAllergy, 'id'>): number {
    return allergy.id;
  }

  compareAllergy(o1: Pick<IAllergy, 'id'> | null, o2: Pick<IAllergy, 'id'> | null): boolean {
    return o1 && o2 ? this.getAllergyIdentifier(o1) === this.getAllergyIdentifier(o2) : o1 === o2;
  }

  addAllergyToCollectionIfMissing<Type extends Pick<IAllergy, 'id'>>(
    allergyCollection: Type[],
    ...allergiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const allergies: Type[] = allergiesToCheck.filter(isPresent);
    if (allergies.length > 0) {
      const allergyCollectionIdentifiers = allergyCollection.map(allergyItem => this.getAllergyIdentifier(allergyItem));
      const allergiesToAdd = allergies.filter(allergyItem => {
        const allergyIdentifier = this.getAllergyIdentifier(allergyItem);
        if (allergyCollectionIdentifiers.includes(allergyIdentifier)) {
          return false;
        }
        allergyCollectionIdentifiers.push(allergyIdentifier);
        return true;
      });
      return [...allergiesToAdd, ...allergyCollection];
    }
    return allergyCollection;
  }
}
