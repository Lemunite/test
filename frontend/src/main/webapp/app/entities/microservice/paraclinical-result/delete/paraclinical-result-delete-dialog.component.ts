import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IParaclinicalResult } from '../paraclinical-result.model';
import { ParaclinicalResultService } from '../service/paraclinical-result.service';

@Component({
  templateUrl: './paraclinical-result-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ParaclinicalResultDeleteDialogComponent {
  paraclinicalResult?: IParaclinicalResult;

  protected paraclinicalResultService = inject(ParaclinicalResultService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paraclinicalResultService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
