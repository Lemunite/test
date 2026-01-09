import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { IFamilyDisease } from '../family-disease.model';
import { FamilyDiseaseService } from '../service/family-disease.service';
import { FamilyDiseaseFormGroup, FamilyDiseaseFormService } from './family-disease-form.service';

@Component({
  selector: 'jhi-family-disease-update',
  templateUrl: './family-disease-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FamilyDiseaseUpdateComponent implements OnInit {
  isSaving = false;
  familyDisease: IFamilyDisease | null = null;

  patientsSharedCollection: IPatient[] = [];

  protected familyDiseaseService = inject(FamilyDiseaseService);
  protected familyDiseaseFormService = inject(FamilyDiseaseFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FamilyDiseaseFormGroup = this.familyDiseaseFormService.createFamilyDiseaseFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familyDisease }) => {
      this.familyDisease = familyDisease;
      if (familyDisease) {
        this.updateForm(familyDisease);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familyDisease = this.familyDiseaseFormService.getFamilyDisease(this.editForm);
    if (familyDisease.id !== null) {
      this.subscribeToSaveResponse(this.familyDiseaseService.update(familyDisease));
    } else {
      this.subscribeToSaveResponse(this.familyDiseaseService.create(familyDisease));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilyDisease>>): void {
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

  protected updateForm(familyDisease: IFamilyDisease): void {
    this.familyDisease = familyDisease;
    this.familyDiseaseFormService.resetForm(this.editForm, familyDisease);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      familyDisease.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.familyDisease?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
