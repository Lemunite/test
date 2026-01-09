import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFamilyAllergy } from '../family-allergy.model';
import { FamilyAllergyService } from '../service/family-allergy.service';

@Component({
  templateUrl: './family-allergy-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FamilyAllergyDeleteDialogComponent {
  familyAllergy?: IFamilyAllergy;

  protected familyAllergyService = inject(FamilyAllergyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familyAllergyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
