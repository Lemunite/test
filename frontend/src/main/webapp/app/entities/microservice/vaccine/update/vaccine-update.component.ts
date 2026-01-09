import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVaccinationForBaby } from 'app/entities/microservice/vaccination-for-baby/vaccination-for-baby.model';
import { VaccinationForBabyService } from 'app/entities/microservice/vaccination-for-baby/service/vaccination-for-baby.service';
import { VaccineType } from 'app/entities/enumerations/vaccine-type.model';
import { VaccineService } from '../service/vaccine.service';
import { IVaccine } from '../vaccine.model';
import { VaccineFormGroup, VaccineFormService } from './vaccine-form.service';

@Component({
  selector: 'jhi-vaccine-update',
  templateUrl: './vaccine-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VaccineUpdateComponent implements OnInit {
  isSaving = false;
  vaccine: IVaccine | null = null;
  vaccineTypeValues = Object.keys(VaccineType);

  vaccinationForBabiesSharedCollection: IVaccinationForBaby[] = [];

  protected vaccineService = inject(VaccineService);
  protected vaccineFormService = inject(VaccineFormService);
  protected vaccinationForBabyService = inject(VaccinationForBabyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VaccineFormGroup = this.vaccineFormService.createVaccineFormGroup();

  compareVaccinationForBaby = (o1: IVaccinationForBaby | null, o2: IVaccinationForBaby | null): boolean =>
    this.vaccinationForBabyService.compareVaccinationForBaby(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaccine }) => {
      this.vaccine = vaccine;
      if (vaccine) {
        this.updateForm(vaccine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaccine = this.vaccineFormService.getVaccine(this.editForm);
    if (vaccine.id !== null) {
      this.subscribeToSaveResponse(this.vaccineService.update(vaccine));
    } else {
      this.subscribeToSaveResponse(this.vaccineService.create(vaccine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaccine>>): void {
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

  protected updateForm(vaccine: IVaccine): void {
    this.vaccine = vaccine;
    this.vaccineFormService.resetForm(this.editForm, vaccine);

    this.vaccinationForBabiesSharedCollection =
      this.vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing<IVaccinationForBaby>(
        this.vaccinationForBabiesSharedCollection,
        vaccine.vaccinationForBaby,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.vaccinationForBabyService
      .query()
      .pipe(map((res: HttpResponse<IVaccinationForBaby[]>) => res.body ?? []))
      .pipe(
        map((vaccinationForBabies: IVaccinationForBaby[]) =>
          this.vaccinationForBabyService.addVaccinationForBabyToCollectionIfMissing<IVaccinationForBaby>(
            vaccinationForBabies,
            this.vaccine?.vaccinationForBaby,
          ),
        ),
      )
      .subscribe((vaccinationForBabies: IVaccinationForBaby[]) => (this.vaccinationForBabiesSharedCollection = vaccinationForBabies));
  }
}
