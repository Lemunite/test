import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPregnancyTetanus } from '../pregnancy-tetanus.model';
import { PregnancyTetanusService } from '../service/pregnancy-tetanus.service';

@Component({
  templateUrl: './pregnancy-tetanus-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PregnancyTetanusDeleteDialogComponent {
  pregnancyTetanus?: IPregnancyTetanus;

  protected pregnancyTetanusService = inject(PregnancyTetanusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pregnancyTetanusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
