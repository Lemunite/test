import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisease, NewDisease } from '../disease.model';

export type PartialUpdateDisease = Partial<IDisease> & Pick<IDisease, 'id'>;

export type EntityResponseType = HttpResponse<IDisease>;
export type EntityArrayResponseType = HttpResponse<IDisease[]>;

@Injectable({ providedIn: 'root' })
export class DiseaseService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/diseases', 'microservice');

  create(disease: NewDisease): Observable<EntityResponseType> {
    return this.http.post<IDisease>(this.resourceUrl, disease, { observe: 'response' });
  }

  update(disease: IDisease): Observable<EntityResponseType> {
    return this.http.put<IDisease>(`${this.resourceUrl}/${this.getDiseaseIdentifier(disease)}`, disease, { observe: 'response' });
  }

  partialUpdate(disease: PartialUpdateDisease): Observable<EntityResponseType> {
    return this.http.patch<IDisease>(`${this.resourceUrl}/${this.getDiseaseIdentifier(disease)}`, disease, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisease>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisease[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDiseaseIdentifier(disease: Pick<IDisease, 'id'>): number {
    return disease.id;
  }

  compareDisease(o1: Pick<IDisease, 'id'> | null, o2: Pick<IDisease, 'id'> | null): boolean {
    return o1 && o2 ? this.getDiseaseIdentifier(o1) === this.getDiseaseIdentifier(o2) : o1 === o2;
  }

  addDiseaseToCollectionIfMissing<Type extends Pick<IDisease, 'id'>>(
    diseaseCollection: Type[],
    ...diseasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const diseases: Type[] = diseasesToCheck.filter(isPresent);
    if (diseases.length > 0) {
      const diseaseCollectionIdentifiers = diseaseCollection.map(diseaseItem => this.getDiseaseIdentifier(diseaseItem));
      const diseasesToAdd = diseases.filter(diseaseItem => {
        const diseaseIdentifier = this.getDiseaseIdentifier(diseaseItem);
        if (diseaseCollectionIdentifiers.includes(diseaseIdentifier)) {
          return false;
        }
        diseaseCollectionIdentifiers.push(diseaseIdentifier);
        return true;
      });
      return [...diseasesToAdd, ...diseaseCollection];
    }
    return diseaseCollection;
  }
}
