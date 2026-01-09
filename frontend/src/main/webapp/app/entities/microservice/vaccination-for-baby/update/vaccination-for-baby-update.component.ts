import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { VaccineType } from 'app/entities/enumerations/vaccine-type.model';
import { IVaccinationForBaby } from '../vaccination-for-baby.model';
import { VaccinationForBabyService } from '../service/vaccination-for-baby.service';
import { VaccinationForBabyFormGroup, VaccinationForBabyFormService } from './vaccination-for-baby-form.service';

@Component({
  selector: 'jhi-vaccination-for-baby-update',
  templateUrl: './vaccination-for-baby-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VaccinationForBabyUpdateComponent implements OnInit {
  isSaving = false;
  vaccinationForBaby: IVaccinationForBaby | null = null;
  vaccineTypeValues = Object.keys(VaccineType);

  protected vaccinationForBabyService = inject(VaccinationForBabyService);
  protected vaccinationForBabyFormService = inject(VaccinationForBabyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VaccinationForBabyFormGroup = this.vaccinationForBabyFormService.createVaccinationForBabyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaccinationForBaby }) => {
      this.vaccinationForBaby = vaccinationForBaby;
      if (vaccinationForBaby) {
        this.updateForm(vaccinationForBaby);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaccinationForBaby = this.vaccinationForBabyFormService.getVaccinationForBaby(this.editForm);
    if (vaccinationForBaby.id !== null) {
      this.subscribeToSaveResponse(this.vaccinationForBabyService.update(vaccinationForBaby));
    } else {
      this.subscribeToSaveResponse(this.vaccinationForBabyService.create(vaccinationForBaby));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaccinationForBaby>>): void {
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

  protected updateForm(vaccinationForBaby: IVaccinationForBaby): void {
    this.vaccinationForBaby = vaccinationForBaby;
    this.vaccinationForBabyFormService.resetForm(this.editForm, vaccinationForBaby);
  }
}
