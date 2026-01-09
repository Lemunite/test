import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAdditionalInformation } from 'app/entities/microservice/additional-information/additional-information.model';
import { AdditionalInformationService } from 'app/entities/microservice/additional-information/service/additional-information.service';
import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { VaccinationForBabyService } from 'app/entities/microservice/vaccination-for-baby/service/vaccination-for-baby.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { PatientService } from '../service/patient.service';
import { IPatient } from '../patient.model';
import { PatientFormGroup, PatientFormService } from './patient-form.service';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;
  patient: IPatient | null = null;
  genderValues = Object.keys(Gender);

  additionalInfosCollection: IAdditionalInformation[] = [];
  vaccinationsForBabiesCollection: IVaccinationForBaby[] = [];

  protected patientService = inject(PatientService);
  protected patientFormService = inject(PatientFormService);
  protected additionalInformationService = inject(AdditionalInformationService);
  protected vaccinationForBabyService = inject(VaccinationForBabyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PatientFormGroup = this.patientFormService.createPatientFormGroup();

  compareAdditionalInformation = (o1: IAdditionalInformation | null, o2: IAdditionalInformation | null): boolean =>
    this.additionalInformationService.compareAdditionalInformation(o1, o2);

  compareVaccinationForBaby = (o1: IVaccinationForBaby | null, o2: IVaccinationForBaby | null): boolean =>
    this.vaccinationForBabyService.compareVaccinationForBaby(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.patient = patient;
      if (patient) {
        this.updateForm(patient);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patient = this.patientFormService.getPatient(this.editForm);
    if (patient.id !== null) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  protected updateForm(patient: IPatient): void {
    this.patient = patient;
    this.patientFormService.resetForm(this.editForm, patient);

    this.additionalInfosCollection =
      this.additionalInformationService.addAdditionalInformationToCollectionIfMissing<IAdditionalInformation>(
        this.additionalInfosCollection,
        patient.additionalInfo,
      );
    this.vaccinationsForBabiesCollection = this.vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing<IVaccinationForBaby>(
      this.vaccinationsForBabiesCollection,
      patient.vaccinationsForBaby,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.additionalInformationService
      .query({ filter: 'patient-is-null' })
      .pipe(map((res: HttpResponse<IAdditionalInformation[]>) => res.body ?? []))
      .pipe(
        map((additionalInformations: IAdditionalInformation[]) =>
          this.additionalInformationService.addAdditionalInformationToCollectionIfMissing<IAdditionalInformation>(
            additionalInformations,
            this.patient?.additionalInfo,
          ),
        ),
      )
      .subscribe((additionalInformations: IAdditionalInformation[]) => (this.additionalInfosCollection = additionalInformations));

    this.vaccinationForBabyService
      .query({ filter: 'patient-is-null' })
      .pipe(map((res: HttpResponse<IVaccinationForBaby[]>) => res.body ?? []))
      .pipe(
        map((vaccinationForBabies: IVaccinationForBaby[]) =>
          this.vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing<IVaccinationForBaby>(
            vaccinationForBabies,
            this.patient?.vaccinationsForBaby,
          ),
        ),
      )
      .subscribe((vaccinationForBabies: IVaccinationForBaby[]) => (this.vaccinationsForBabiesCollection = vaccinationForBabies));
  }
}
