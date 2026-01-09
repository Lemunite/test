import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdditionalInformation } from '../additional-information.model';
import { AdditionalInformationService } from '../service/additional-information.service';

@Component({
  templateUrl: './additional-information-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdditionalInformationDeleteDialogComponent {
  additionalInformation?: IAdditionalInformation;

  protected additionalInformationService = inject(AdditionalInformationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.additionalInformationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
