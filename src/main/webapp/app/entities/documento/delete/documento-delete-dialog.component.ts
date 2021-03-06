import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';

@Component({
  templateUrl: './documento-delete-dialog.component.html',
})
export class DocumentoDeleteDialogComponent {
  documento?: IDocumento;

  constructor(protected documentoService: DocumentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
