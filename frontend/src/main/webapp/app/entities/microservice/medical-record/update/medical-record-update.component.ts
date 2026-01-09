import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOrganExamination } from 'app/entities/microservice/organ-examination/organ-examination.model';
import { OrganExaminationService } from 'app/entities/microservice/organ-examination/service/organ-examination.service';
import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { MedicalRecordService } from '../service/medical-record.service';
import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordFormGroup, MedicalRecordFormService } from './medical-record-form.service';

@Component({
  selector: 'jhi-medical-record-update',
  templateUrl: './medical-record-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalRecordUpdateComponent implements OnInit {
  isSaving = false;
  medicalRecord: IMedicalRecord | null = null;

  organExaminationsCollection: IOrganExamination[] = [];
  patientsSharedCollection: IPatient[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected medicalRecordService = inject(MedicalRecordService);
  protected medicalRecordFormService = inject(MedicalRecordFormService);
  protected organExaminationService = inject(OrganExaminationService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalRecordFormGroup = this.medicalRecordFormService.createMedicalRecordFormGroup();

  compareOrganExamination = (o1: IOrganExamination | null, o2: IOrganExamination | null): boolean =>
    this.organExaminationService.compareOrganExamination(o1, o2);

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalRecord }) => {
      this.medicalRecord = medicalRecord;
      if (medicalRecord) {
        this.updateForm(medicalRecord);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalRecord = this.medicalRecordFormService.getMedicalRecord(this.editForm);
    if (medicalRecord.id !== null) {
      this.subscribeToSaveResponse(this.medicalRecordService.update(medicalRecord));
    } else {
      this.subscribeToSaveResponse(this.medicalRecordService.create(medicalRecord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalRecord>>): void {
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

  protected updateForm(medicalRecord: IMedicalRecord): void {
    this.medicalRecord = medicalRecord;
    this.medicalRecordFormService.resetForm(this.editForm, medicalRecord);

    this.organExaminationsCollection = this.organExaminationService.addOrganExaminationToCollectionIfMissing<IOrganExamination>(
      this.organExaminationsCollection,
      medicalRecord.organExamination,
    );
    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      medicalRecord.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organExaminationService
      .query({ filter: 'medicalrecord-is-null' })
      .pipe(map((res: HttpResponse<IOrganExamination[]>) => res.body ?? []))
      .pipe(
        map((organExaminations: IOrganExamination[]) =>
          this.organExaminationService.addOrganExaminationToCollectionIfMissing<IOrganExamination>(
            organExaminations,
            this.medicalRecord?.organExamination,
          ),
        ),
      )
      .subscribe((organExaminations: IOrganExamination[]) => (this.organExaminationsCollection = organExaminations));

    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.medicalRecord?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
