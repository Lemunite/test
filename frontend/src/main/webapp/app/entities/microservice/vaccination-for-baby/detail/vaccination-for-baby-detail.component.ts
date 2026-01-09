import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVaccinationForBaby } from '../vaccination-for-baby.model';

@Component({
  selector: 'jhi-vaccination-for-baby-detail',
  templateUrl: './vaccination-for-baby-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VaccinationForBabyDetailComponent {
  vaccinationForBaby = input<IVaccinationForBaby | null>(null);

  previousState(): void {
    window.history.back();
  }
}
