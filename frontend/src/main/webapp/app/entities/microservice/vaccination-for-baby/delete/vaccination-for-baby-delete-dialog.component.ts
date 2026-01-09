import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVaccinationForBaby } from '../vaccination-for-baby.model';
import { VaccinationForBabyService } from '../service/vaccination-for-baby.service';

@Component({
  templateUrl: './vaccination-for-baby-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VaccinationForBabyDeleteDialogComponent {
  vaccinationForBaby?: IVaccinationForBaby;

  protected vaccinationForBabyService = inject(VaccinationForBabyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vaccinationForBabyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
