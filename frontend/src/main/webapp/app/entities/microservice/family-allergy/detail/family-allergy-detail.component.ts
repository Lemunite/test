import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFamilyAllergy } from '../family-allergy.model';

@Component({
  selector: 'jhi-family-allergy-detail',
  templateUrl: './family-allergy-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FamilyAllergyDetailComponent {
  familyAllergy = input<IFamilyAllergy | null>(null);

  previousState(): void {
    window.history.back();
  }
}
