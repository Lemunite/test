import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IVaccine } from '../vaccine.model';

@Component({
  selector: 'jhi-vaccine-detail',
  templateUrl: './vaccine-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class VaccineDetailComponent {
  vaccine = input<IVaccine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
