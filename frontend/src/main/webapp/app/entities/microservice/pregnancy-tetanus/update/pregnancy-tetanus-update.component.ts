import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { TetanusDose } from 'app/entities/enumerations/tetanus-dose.model';
import { PregnancyTetanusService } from '../service/pregnancy-tetanus.service';
import { IPregnancyTetanus } from '../pregnancy-tetanus.model';
import { PregnancyTetanusFormGroup, PregnancyTetanusFormService } from './pregnancy-tetanus-form.service';

@Component({
  selector: 'jhi-pregnancy-tetanus-update',
  templateUrl: './pregnancy-tetanus-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PregnancyTetanusUpdateComponent implements OnInit {
  isSaving = false;
  pregnancyTetanus: IPregnancyTetanus | null = null;
  tetanusDoseValues = Object.keys(TetanusDose);

  patientsSharedCollection: IPatient[] = [];

  protected pregnancyTetanusService = inject(PregnancyTetanusService);
  protected pregnancyTetanusFormService = inject(PregnancyTetanusFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PregnancyTetanusFormGroup = this.pregnancyTetanusFormService.createPregnancyTetanusFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pregnancyTetanus }) => {
      this.pregnancyTetanus = pregnancyTetanus;
      if (pregnancyTetanus) {
        this.updateForm(pregnancyTetanus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pregnancyTetanus = this.pregnancyTetanusFormService.getPregnancyTetanus(this.editForm);
    if (pregnancyTetanus.id !== null) {
      this.subscribeToSaveResponse(this.pregnancyTetanusService.update(pregnancyTetanus));
    } else {
      this.subscribeToSaveResponse(this.pregnancyTetanusService.create(pregnancyTetanus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPregnancyTetanus>>): void {
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

  protected updateForm(pregnancyTetanus: IPregnancyTetanus): void {
    this.pregnancyTetanus = pregnancyTetanus;
    this.pregnancyTetanusFormService.resetForm(this.editForm, pregnancyTetanus);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      pregnancyTetanus.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) =>
          this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.pregnancyTetanus?.patient),
        ),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
