import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IVaccinationTCMR } from '../vaccination-tcmr.model';

@Component({
  selector: 'jhi-vaccination-tcmr-detail',
  templateUrl: './vaccination-tcmr-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class VaccinationTCMRDetailComponent {
  vaccinationTCMR = input<IVaccinationTCMR | null>(null);

  previousState(): void {
    window.history.back();
  }
}
