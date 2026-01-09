import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFamilyDisease } from 'app/entities/microservice/family-disease/family-disease.model';
import { FamilyDiseaseService } from 'app/entities/microservice/family-disease/service/family-disease.service';
import { DiseaseName } from 'app/entities/enumerations/disease-name.model';
import { DiseaseService } from '../service/disease.service';
import { IDisease } from '../disease.model';
import { DiseaseFormGroup, DiseaseFormService } from './disease-form.service';

@Component({
  selector: 'jhi-disease-update',
  templateUrl: './disease-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DiseaseUpdateComponent implements OnInit {
  isSaving = false;
  disease: IDisease | null = null;
  diseaseNameValues = Object.keys(DiseaseName);

  familyDiseasesSharedCollection: IFamilyDisease[] = [];

  protected diseaseService = inject(DiseaseService);
  protected diseaseFormService = inject(DiseaseFormService);
  protected familyDiseaseService = inject(FamilyDiseaseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DiseaseFormGroup = this.diseaseFormService.createDiseaseFormGroup();

  compareFamilyDisease = (o1: IFamilyDisease | null, o2: IFamilyDisease | null): boolean =>
    this.familyDiseaseService.compareFamilyDisease(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disease }) => {
      this.disease = disease;
      if (disease) {
        this.updateForm(disease);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disease = this.diseaseFormService.getDisease(this.editForm);
    if (disease.id !== null) {
      this.subscribeToSaveResponse(this.diseaseService.update(disease));
    } else {
      this.subscribeToSaveResponse(this.diseaseService.create(disease));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisease>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(disease: IDisease): void {
    this.disease = disease;
    this.diseaseFormService.resetForm(this.editForm, disease);

    this.familyDiseasesSharedCollection = this.familyDiseaseService.addFamilyDiseaseToCollectionIfMissing<IFamilyDisease>(
      this.familyDiseasesSharedCollection,
      disease.familyDisease,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.familyDiseaseService
      .query()
      .pipe(map((res: HttpResponse<IFamilyDisease[]>) => res.body ?? []))
      .pipe(
        map((familyDiseases: IFamilyDisease[]) =>
          this.familyDiseaseService.addFamilyDiseaseToCollectionIfMissing<IFamilyDisease>(familyDiseases, this.disease?.familyDisease),
        ),
      )
      .subscribe((familyDiseases: IFamilyDisease[]) => (this.familyDiseasesSharedCollection = familyDiseases));
  }
}
