import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisability, NewDisability } from '../disability.model';

export type PartialUpdateDisability = Partial<IDisability> & Pick<IDisability, 'id'>;

export type EntityResponseType = HttpResponse<IDisability>;
export type EntityArrayResponseType = HttpResponse<IDisability[]>;

@Injectable({ providedIn: 'root' })
export class DisabilityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disabilities', 'microservice');

  create(disability: NewDisability): Observable<EntityResponseType> {
    return this.http.post<IDisability>(this.resourceUrl, disability, { observe: 'response' });
  }

  update(disability: IDisability): Observable<EntityResponseType> {
    return this.http.put<IDisability>(`${this.resourceUrl}/${this.getDisabilityIdentifier(disability)}`, disability, {
      observe: 'response',
    });
  }

  partialUpdate(disability: PartialUpdateDisability): Observable<EntityResponseType> {
    return this.http.patch<IDisability>(`${this.resourceUrl}/${this.getDisabilityIdentifier(disability)}`, disability, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisability>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisability[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDisabilityIdentifier(disability: Pick<IDisability, 'id'>): number {
    return disability.id;
  }

  compareDisability(o1: Pick<IDisability, 'id'> | null, o2: Pick<IDisability, 'id'> | null): boolean {
    return o1 && o2 ? this.getDisabilityIdentifier(o1) === this.getDisabilityIdentifier(o2) : o1 === o2;
  }

  addDisabilityToCollectionIfMissing<Type extends Pick<IDisability, 'id'>>(
    disabilityCollection: Type[],
    ...disabilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const disabilities: Type[] = disabilitiesToCheck.filter(isPresent);
    if (disabilities.length > 0) {
      const disabilityCollectionIdentifiers = disabilityCollection.map(disabilityItem => this.getDisabilityIdentifier(disabilityItem));
      const disabilitiesToAdd = disabilities.filter(disabilityItem => {
        const disabilityIdentifier = this.getDisabilityIdentifier(disabilityItem);
        if (disabilityCollectionIdentifiers.includes(disabilityIdentifier)) {
          return false;
        }
        disabilityCollectionIdentifiers.push(disabilityIdentifier);
        return true;
      });
      return [...disabilitiesToAdd, ...disabilityCollection];
    }
    return disabilityCollection;
  }
}
