import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IPregnancyTetanus } from '../pregnancy-tetanus.model';

@Component({
  selector: 'jhi-pregnancy-tetanus-detail',
  templateUrl: './pregnancy-tetanus-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class PregnancyTetanusDetailComponent {
  pregnancyTetanus = input<IPregnancyTetanus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
