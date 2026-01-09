import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAllergy } from '../allergy.model';
import { AllergyService } from '../service/allergy.service';

@Component({
  templateUrl: './allergy-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AllergyDeleteDialogComponent {
  allergy?: IAllergy;

  protected allergyService = inject(AllergyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.allergyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
