import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVaccinationTCMR } from '../vaccination-tcmr.model';
import { VaccinationTCMRService } from '../service/vaccination-tcmr.service';

@Component({
  templateUrl: './vaccination-tcmr-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VaccinationTCMRDeleteDialogComponent {
  vaccinationTCMR?: IVaccinationTCMR;

  protected vaccinationTCMRService = inject(VaccinationTCMRService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vaccinationTCMRService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
