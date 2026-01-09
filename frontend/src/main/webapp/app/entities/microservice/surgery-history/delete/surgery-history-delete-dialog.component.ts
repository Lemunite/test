import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISurgeryHistory } from '../surgery-history.model';
import { SurgeryHistoryService } from '../service/surgery-history.service';

@Component({
  templateUrl: './surgery-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SurgeryHistoryDeleteDialogComponent {
  surgeryHistory?: ISurgeryHistory;

  protected surgeryHistoryService = inject(SurgeryHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.surgeryHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
