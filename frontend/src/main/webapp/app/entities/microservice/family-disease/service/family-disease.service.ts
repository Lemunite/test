import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilyDisease, NewFamilyDisease } from '../family-disease.model';

export type PartialUpdateFamilyDisease = Partial<IFamilyDisease> & Pick<IFamilyDisease, 'id'>;

export type EntityResponseType = HttpResponse<IFamilyDisease>;
export type EntityArrayResponseType = HttpResponse<IFamilyDisease[]>;

@Injectable({ providedIn: 'root' })
export class FamilyDiseaseService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/family-diseases', 'microservice');

  create(familyDisease: NewFamilyDisease): Observable<EntityResponseType> {
    return this.http.post<IFamilyDisease>(this.resourceUrl, familyDisease, { observe: 'response' });
  }

  update(familyDisease: IFamilyDisease): Observable<EntityResponseType> {
    return this.http.put<IFamilyDisease>(`${this.resourceUrl}/${this.getFamilyDiseaseIdentifier(familyDisease)}`, familyDisease, {
      observe: 'response',
    });
  }

  partialUpdate(familyDisease: PartialUpdateFamilyDisease): Observable<EntityResponseType> {
    return this.http.patch<IFamilyDisease>(`${this.resourceUrl}/${this.getFamilyDiseaseIdentifier(familyDisease)}`, familyDisease, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamilyDisease>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamilyDisease[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFamilyDiseaseIdentifier(familyDisease: Pick<IFamilyDisease, 'id'>): number {
    return familyDisease.id;
  }

  compareFamilyDisease(o1: Pick<IFamilyDisease, 'id'> | null, o2: Pick<IFamilyDisease, 'id'> | null): boolean {
    return o1 && o2 ? this.getFamilyDiseaseIdentifier(o1) === this.getFamilyDiseaseIdentifier(o2) : o1 === o2;
  }

  addFamilyDiseaseToCollectionIfMissing<Type extends Pick<IFamilyDisease, 'id'>>(
    familyDiseaseCollection: Type[],
    ...familyDiseasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const familyDiseases: Type[] = familyDiseasesToCheck.filter(isPresent);
    if (familyDiseases.length > 0) {
      const familyDiseaseCollectionIdentifiers = familyDiseaseCollection.map(familyDiseaseItem =>
        this.getFamilyDiseaseIdentifier(familyDiseaseItem),
      );
      const familyDiseasesToAdd = familyDiseases.filter(familyDiseaseItem => {
        const familyDiseaseIdentifier = this.getFamilyDiseaseIdentifier(familyDiseaseItem);
        if (familyDiseaseCollectionIdentifiers.includes(familyDiseaseIdentifier)) {
          return false;
        }
        familyDiseaseCollectionIdentifiers.push(familyDiseaseIdentifier);
        return true;
      });
      return [...familyDiseasesToAdd, ...familyDiseaseCollection];
    }
    return familyDiseaseCollection;
  }
}
