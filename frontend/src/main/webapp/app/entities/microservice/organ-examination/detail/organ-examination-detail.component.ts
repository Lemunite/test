import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IOrganExamination } from '../organ-examination.model';

@Component({
  selector: 'jhi-organ-examination-detail',
  templateUrl: './organ-examination-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class OrganExaminationDetailComponent {
  organExamination = input<IOrganExamination | null>(null);

  previousState(): void {
    window.history.back();
  }
}
