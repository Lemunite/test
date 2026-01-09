import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISurgeryHistory } from '../surgery-history.model';

@Component({
  selector: 'jhi-surgery-history-detail',
  templateUrl: './surgery-history-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SurgeryHistoryDetailComponent {
  surgeryHistory = input<ISurgeryHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
