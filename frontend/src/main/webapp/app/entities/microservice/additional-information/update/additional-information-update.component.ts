import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { RiskLevel } from 'app/entities/enumerations/risk-level.model';
import { RiskLevelAlcohol } from 'app/entities/enumerations/risk-level-alcohol.model';
import { TypeToilet } from 'app/entities/enumerations/type-toilet.model';
import { BirthStatusType } from 'app/entities/enumerations/birth-status-type.model';
import { AdditionalInformationService } from '../service/additional-information.service';
import { IAdditionalInformation } from '../additional-information.model';
import { AdditionalInformationFormGroup, AdditionalInformationFormService } from './additional-information-form.service';

@Component({
  selector: 'jhi-additional-information-update',
  templateUrl: './additional-information-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdditionalInformationUpdateComponent implements OnInit {
  isSaving = false;
  additionalInformation: IAdditionalInformation | null = null;
  riskLevelValues = Object.keys(RiskLevel);
  riskLevelAlcoholValues = Object.keys(RiskLevelAlcohol);
  typeToiletValues = Object.keys(TypeToilet);
  birthStatusTypeValues = Object.keys(BirthStatusType);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected additionalInformationService = inject(AdditionalInformationService);
  protected additionalInformationFormService = inject(AdditionalInformationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdditionalInformationFormGroup = this.additionalInformationFormService.createAdditionalInformationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ additionalInformation }) => {
      this.additionalInformation = additionalInformation;
      if (additionalInformation) {
        this.updateForm(additionalInformation);
      }
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
    const additionalInformation = this.additionalInformationFormService.getAdditionalInformation(this.editForm);
    if (additionalInformation.id !== null) {
      this.subscribeToSaveResponse(this.additionalInformationService.update(additionalInformation));
    } else {
      this.subscribeToSaveResponse(this.additionalInformationService.create(additionalInformation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdditionalInformation>>): void {
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

  protected updateForm(additionalInformation: IAdditionalInformation): void {
    this.additionalInformation = additionalInformation;
    this.additionalInformationFormService.resetForm(this.editForm, additionalInformation);
  }
}
