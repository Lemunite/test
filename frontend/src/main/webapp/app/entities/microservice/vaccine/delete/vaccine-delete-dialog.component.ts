import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVaccine } from '../vaccine.model';
import { VaccineService } from '../service/vaccine.service';

@Component({
  templateUrl: './vaccine-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VaccineDeleteDialogComponent {
  vaccine?: IVaccine;

  protected vaccineService = inject(VaccineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vaccineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
