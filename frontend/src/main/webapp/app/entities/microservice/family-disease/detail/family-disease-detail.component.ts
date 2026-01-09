import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFamilyDisease } from '../family-disease.model';

@Component({
  selector: 'jhi-family-disease-detail',
  templateUrl: './family-disease-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FamilyDiseaseDetailComponent {
  familyDisease = input<IFamilyDisease | null>(null);

  previousState(): void {
    window.history.back();
  }
}
