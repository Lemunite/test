import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IAdditionalInformation } from '../additional-information.model';

@Component({
  selector: 'jhi-additional-information-detail',
  templateUrl: './additional-information-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class AdditionalInformationDetailComponent {
  additionalInformation = input<IAdditionalInformation | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
