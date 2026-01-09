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
import { FamilyAllergyService } from '../service/family-allergy.service';
import { IFamilyAllergy } from '../family-allergy.model';
import { FamilyAllergyFormGroup, FamilyAllergyFormService } from './family-allergy-form.service';

@Component({
  selector: 'jhi-family-allergy-update',
  templateUrl: './family-allergy-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FamilyAllergyUpdateComponent implements OnInit {
  isSaving = false;
  familyAllergy: IFamilyAllergy | null = null;
  allergyTypeValues = Object.keys(AllergyType);

  patientsSharedCollection: IPatient[] = [];

  protected familyAllergyService = inject(FamilyAllergyService);
  protected familyAllergyFormService = inject(FamilyAllergyFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FamilyAllergyFormGroup = this.familyAllergyFormService.createFamilyAllergyFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familyAllergy }) => {
      this.familyAllergy = familyAllergy;
      if (familyAllergy) {
        this.updateForm(familyAllergy);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familyAllergy = this.familyAllergyFormService.getFamilyAllergy(this.editForm);
    if (familyAllergy.id !== null) {
      this.subscribeToSaveResponse(this.familyAllergyService.update(familyAllergy));
    } else {
      this.subscribeToSaveResponse(this.familyAllergyService.create(familyAllergy));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilyAllergy>>): void {
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

  protected updateForm(familyAllergy: IFamilyAllergy): void {
    this.familyAllergy = familyAllergy;
    this.familyAllergyFormService.resetForm(this.editForm, familyAllergy);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      familyAllergy.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.familyAllergy?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
