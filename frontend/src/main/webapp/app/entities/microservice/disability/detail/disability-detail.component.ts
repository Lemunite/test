import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDisability } from '../disability.model';

@Component({
  selector: 'jhi-disability-detail',
  templateUrl: './disability-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DisabilityDetailComponent {
  disability = input<IDisability | null>(null);

  previousState(): void {
    window.history.back();
  }
}
