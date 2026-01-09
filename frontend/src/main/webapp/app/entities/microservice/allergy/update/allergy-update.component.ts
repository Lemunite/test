import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/microservice/patient/patient.model';
import { PatientService } from 'app/entities/microservice/patient/service/patient.service';
import { AllergyType } from 'app/entities/enumerations/allergy-type.model';
import { AllergyService } from '../service/allergy.service';
import { IAllergy } from '../allergy.model';
import { AllergyFormGroup, AllergyFormService } from './allergy-form.service';

@Component({
  selector: 'jhi-allergy-update',
  templateUrl: './allergy-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AllergyUpdateComponent implements OnInit {
  isSaving = false;
  allergy: IAllergy | null = null;
  allergyTypeValues = Object.keys(AllergyType);

  patientsSharedCollection: IPatient[] = [];

  protected allergyService = inject(AllergyService);
  protected allergyFormService = inject(AllergyFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AllergyFormGroup = this.allergyFormService.createAllergyFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allergy }) => {
      this.allergy = allergy;
      if (allergy) {
        this.updateForm(allergy);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const allergy = this.allergyFormService.getAllergy(this.editForm);
    if (allergy.id !== null) {
      this.subscribeToSaveResponse(this.allergyService.update(allergy));
    } else {
      this.subscribeToSaveResponse(this.allergyService.create(allergy));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllergy>>): void {
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

  protected updateForm(allergy: IAllergy): void {
    this.allergy = allergy;
    this.allergyFormService.resetForm(this.editForm, allergy);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      allergy.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.allergy?.patient)))
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
