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
import { IMedicalRecord } from 'app/entities/microservice/medical-record/medical-record.model';
import { MedicalRecordService } from 'app/entities/microservice/medical-record/service/medical-record.service';
import { ParaclinicalResultService } from '../service/paraclinical-result.service';
import { IParaclinicalResult } from '../paraclinical-result.model';
import { ParaclinicalResultFormGroup, ParaclinicalResultFormService } from './paraclinical-result-form.service';

@Component({
  selector: 'jhi-paraclinical-result-update',
  templateUrl: './paraclinical-result-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParaclinicalResultUpdateComponent implements OnInit {
  isSaving = false;
  paraclinicalResult: IParaclinicalResult | null = null;

  medicalRecordsSharedCollection: IMedicalRecord[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected paraclinicalResultService = inject(ParaclinicalResultService);
  protected paraclinicalResultFormService = inject(ParaclinicalResultFormService);
  protected medicalRecordService = inject(MedicalRecordService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ParaclinicalResultFormGroup = this.paraclinicalResultFormService.createParaclinicalResultFormGroup();

  compareMedicalRecord = (o1: IMedicalRecord | null, o2: IMedicalRecord | null): boolean =>
    this.medicalRecordService.compareMedicalRecord(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paraclinicalResult }) => {
      this.paraclinicalResult = paraclinicalResult;
      if (paraclinicalResult) {
        this.updateForm(paraclinicalResult);
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
    const paraclinicalResult = this.paraclinicalResultFormService.getParaclinicalResult(this.editForm);
    if (paraclinicalResult.id !== null) {
      this.subscribeToSaveResponse(this.paraclinicalResultService.update(paraclinicalResult));
    } else {
      this.subscribeToSaveResponse(this.paraclinicalResultService.create(paraclinicalResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParaclinicalResult>>): void {
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

  protected updateForm(paraclinicalResult: IParaclinicalResult): void {
    this.paraclinicalResult = paraclinicalResult;
    this.paraclinicalResultFormService.resetForm(this.editForm, paraclinicalResult);

    this.medicalRecordsSharedCollection = this.medicalRecordService.addMedicalRecordToCollectionIfMissing<IMedicalRecord>(
      this.medicalRecordsSharedCollection,
      paraclinicalResult.medicalRecord,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicalRecordService
      .query()
      .pipe(map((res: HttpResponse<IMedicalRecord[]>) => res.body ?? []))
      .pipe(
        map((medicalRecords: IMedicalRecord[]) =>
          this.medicalRecordService.addMedicalRecordToCollectionIfMissing<IMedicalRecord>(
            medicalRecords,
            this.paraclinicalResult?.medicalRecord,
          ),
        ),
      )
      .subscribe((medicalRecords: IMedicalRecord[]) => (this.medicalRecordsSharedCollection = medicalRecords));
  }
}
