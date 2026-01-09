import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDisability } from '../disability.model';
import { DisabilityService } from '../service/disability.service';

@Component({
  templateUrl: './disability-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DisabilityDeleteDialogComponent {
  disability?: IDisability;

  protected disabilityService = inject(DisabilityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disabilityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
