import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilyAllergy, NewFamilyAllergy } from '../family-allergy.model';

export type PartialUpdateFamilyAllergy = Partial<IFamilyAllergy> & Pick<IFamilyAllergy, 'id'>;

export type EntityResponseType = HttpResponse<IFamilyAllergy>;
export type EntityArrayResponseType = HttpResponse<IFamilyAllergy[]>;

@Injectable({ providedIn: 'root' })
export class FamilyAllergyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/family-allergies', 'microservice');

  create(familyAllergy: NewFamilyAllergy): Observable<EntityResponseType> {
    return this.http.post<IFamilyAllergy>(this.resourceUrl, familyAllergy, { observe: 'response' });
  }

  update(familyAllergy: IFamilyAllergy): Observable<EntityResponseType> {
    return this.http.put<IFamilyAllergy>(`${this.resourceUrl}/${this.getFamilyAllergyIdentifier(familyAllergy)}`, familyAllergy, {
      observe: 'response',
    });
  }

  partialUpdate(familyAllergy: PartialUpdateFamilyAllergy): Observable<EntityResponseType> {
    return this.http.patch<IFamilyAllergy>(`${this.resourceUrl}/${this.getFamilyAllergyIdentifier(familyAllergy)}`, familyAllergy, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamilyAllergy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamilyAllergy[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFamilyAllergyIdentifier(familyAllergy: Pick<IFamilyAllergy, 'id'>): number {
    return familyAllergy.id;
  }

  compareFamilyAllergy(o1: Pick<IFamilyAllergy, 'id'> | null, o2: Pick<IFamilyAllergy, 'id'> | null): boolean {
    return o1 && o2 ? this.getFamilyAllergyIdentifier(o1) === this.getFamilyAllergyIdentifier(o2) : o1 === o2;
  }

  addFamilyAllergyToCollectionIfMissing<Type extends Pick<IFamilyAllergy, 'id'>>(
    familyAllergyCollection: Type[],
    ...familyAllergiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const familyAllergies: Type[] = familyAllergiesToCheck.filter(isPresent);
    if (familyAllergies.length > 0) {
      const familyAllergyCollectionIdentifiers = familyAllergyCollection.map(familyAllergyItem =>
        this.getFamilyAllergyIdentifier(familyAllergyItem),
      );
      const familyAllergiesToAdd = familyAllergies.filter(familyAllergyItem => {
        const familyAllergyIdentifier = this.getFamilyAllergyIdentifier(familyAllergyItem);
        if (familyAllergyCollectionIdentifiers.includes(familyAllergyIdentifier)) {
          return false;
        }
        familyAllergyCollectionIdentifiers.push(familyAllergyIdentifier);
        return true;
      });
      return [...familyAllergiesToAdd, ...familyAllergyCollection];
    }
    return familyAllergyCollection;
  }
}
