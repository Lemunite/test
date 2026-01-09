import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { DisabilityType } from 'app/entities/enumerations/disability-type.model';
import { DisabilityService } from '../service/disability.service';
import { IDisability } from '../disability.model';
import { DisabilityFormGroup, DisabilityFormService } from './disability-form.service';

@Component({
  selector: 'jhi-disability-update',
  templateUrl: './disability-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisabilityUpdateComponent implements OnInit {
  isSaving = false;
  disability: IDisability | null = null;
  disabilityTypeValues = Object.keys(DisabilityType);

  patientsSharedCollection: IPatient[] = [];

  protected disabilityService = inject(DisabilityService);
  protected disabilityFormService = inject(DisabilityFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DisabilityFormGroup = this.disabilityFormService.createDisabilityFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disability }) => {
      this.disability = disability;
      if (disability) {
        this.updateForm(disability);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disability = this.disabilityFormService.getDisability(this.editForm);
    if (disability.id !== null) {
      this.subscribeToSaveResponse(this.disabilityService.update(disability));
    } else {
      this.subscribeToSaveResponse(this.disabilityService.create(disability));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisability>>): void {
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

  protected updateForm(disability: IDisability): void {
    this.disability = disability;
    this.disabilityFormService.resetForm(this.editForm, disability);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      disability.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.disability?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
