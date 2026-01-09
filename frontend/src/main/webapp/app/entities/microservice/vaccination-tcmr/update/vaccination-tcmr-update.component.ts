import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { VaccineTCMRType } from 'app/entities/enumerations/vaccine-tcmr-type.model';
import { VaccinationTCMRService } from '../service/vaccination-tcmr.service';
import { IVaccinationTCMR } from '../vaccination-tcmr.model';
import { VaccinationTCMRFormGroup, VaccinationTCMRFormService } from './vaccination-tcmr-form.service';

@Component({
  selector: 'jhi-vaccination-tcmr-update',
  templateUrl: './vaccination-tcmr-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VaccinationTCMRUpdateComponent implements OnInit {
  isSaving = false;
  vaccinationTCMR: IVaccinationTCMR | null = null;
  vaccineTCMRTypeValues = Object.keys(VaccineTCMRType);

  patientsSharedCollection: IPatient[] = [];

  protected vaccinationTCMRService = inject(VaccinationTCMRService);
  protected vaccinationTCMRFormService = inject(VaccinationTCMRFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VaccinationTCMRFormGroup = this.vaccinationTCMRFormService.createVaccinationTCMRFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaccinationTCMR }) => {
      this.vaccinationTCMR = vaccinationTCMR;
      if (vaccinationTCMR) {
        this.updateForm(vaccinationTCMR);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaccinationTCMR = this.vaccinationTCMRFormService.getVaccinationTCMR(this.editForm);
    if (vaccinationTCMR.id !== null) {
      this.subscribeToSaveResponse(this.vaccinationTCMRService.update(vaccinationTCMR));
    } else {
      this.subscribeToSaveResponse(this.vaccinationTCMRService.create(vaccinationTCMR));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaccinationTCMR>>): void {
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

  protected updateForm(vaccinationTCMR: IVaccinationTCMR): void {
    this.vaccinationTCMR = vaccinationTCMR;
    this.vaccinationTCMRFormService.resetForm(this.editForm, vaccinationTCMR);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      vaccinationTCMR.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) =>
          this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.vaccinationTCMR?.patient),
        ),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
