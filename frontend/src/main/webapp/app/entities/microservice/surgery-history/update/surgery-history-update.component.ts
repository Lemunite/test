import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { ISurgeryHistory } from '../surgery-history.model';
import { SurgeryHistoryService } from '../service/surgery-history.service';
import { SurgeryHistoryFormGroup, SurgeryHistoryFormService } from './surgery-history-form.service';

@Component({
  selector: 'jhi-surgery-history-update',
  templateUrl: './surgery-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SurgeryHistoryUpdateComponent implements OnInit {
  isSaving = false;
  surgeryHistory: ISurgeryHistory | null = null;

  patientsSharedCollection: IPatient[] = [];

  protected surgeryHistoryService = inject(SurgeryHistoryService);
  protected surgeryHistoryFormService = inject(SurgeryHistoryFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SurgeryHistoryFormGroup = this.surgeryHistoryFormService.createSurgeryHistoryFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surgeryHistory }) => {
      this.surgeryHistory = surgeryHistory;
      if (surgeryHistory) {
        this.updateForm(surgeryHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const surgeryHistory = this.surgeryHistoryFormService.getSurgeryHistory(this.editForm);
    if (surgeryHistory.id !== null) {
      this.subscribeToSaveResponse(this.surgeryHistoryService.update(surgeryHistory));
    } else {
      this.subscribeToSaveResponse(this.surgeryHistoryService.create(surgeryHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISurgeryHistory>>): void {
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

  protected updateForm(surgeryHistory: ISurgeryHistory): void {
    this.surgeryHistory = surgeryHistory;
    this.surgeryHistoryFormService.resetForm(this.editForm, surgeryHistory);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      surgeryHistory.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) =>
          this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.surgeryHistory?.patient),
        ),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
