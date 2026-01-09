import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAllergy } from '../allergy.model';

@Component({
  selector: 'jhi-allergy-detail',
  templateUrl: './allergy-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AllergyDetailComponent {
  allergy = input<IAllergy | null>(null);

  previousState(): void {
    window.history.back();
  }
}
