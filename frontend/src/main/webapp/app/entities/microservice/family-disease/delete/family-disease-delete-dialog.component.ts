import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFamilyDisease } from '../family-disease.model';
import { FamilyDiseaseService } from '../service/family-disease.service';

@Component({
  templateUrl: './family-disease-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FamilyDiseaseDeleteDialogComponent {
  familyDisease?: IFamilyDisease;

  protected familyDiseaseService = inject(FamilyDiseaseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familyDiseaseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
