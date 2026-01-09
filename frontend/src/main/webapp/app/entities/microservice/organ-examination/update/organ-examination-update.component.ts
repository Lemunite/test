import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrganExamination } from '../organ-examination.model';
import { OrganExaminationService } from '../service/organ-examination.service';
import { OrganExaminationFormGroup, OrganExaminationFormService } from './organ-examination-form.service';

@Component({
  selector: 'jhi-organ-examination-update',
  templateUrl: './organ-examination-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganExaminationUpdateComponent implements OnInit {
  isSaving = false;
  organExamination: IOrganExamination | null = null;

  protected organExaminationService = inject(OrganExaminationService);
  protected organExaminationFormService = inject(OrganExaminationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganExaminationFormGroup = this.organExaminationFormService.createOrganExaminationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organExamination }) => {
      this.organExamination = organExamination;
      if (organExamination) {
        this.updateForm(organExamination);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organExamination = this.organExaminationFormService.getOrganExamination(this.editForm);
    if (organExamination.id !== null) {
      this.subscribeToSaveResponse(this.organExaminationService.update(organExamination));
    } else {
      this.subscribeToSaveResponse(this.organExaminationService.create(organExamination));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganExamination>>): void {
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

  protected updateForm(organExamination: IOrganExamination): void {
    this.organExamination = organExamination;
    this.organExaminationFormService.resetForm(this.editForm, organExamination);
  }
}
