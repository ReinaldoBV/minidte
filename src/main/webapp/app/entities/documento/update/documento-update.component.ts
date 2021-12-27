import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumento, Documento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    rutEmpresa: [],
    idAmbiente: [],
    tipoDocumento: [],
    montoTotal: [],
    correoReceptor: [],
  });

  constructor(protected documentoService: DocumentoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => {
      this.updateForm(documento);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documento = this.createFromForm();
    if (documento.id !== undefined) {
      this.subscribeToSaveResponse(this.documentoService.update(documento));
    } else {
      this.subscribeToSaveResponse(this.documentoService.create(documento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumento>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documento: IDocumento): void {
    this.editForm.patchValue({
      id: documento.id,
      rutEmpresa: documento.rutEmpresa,
      idAmbiente: documento.idAmbiente,
      tipoDocumento: documento.tipoDocumento,
      montoTotal: documento.montoTotal,
      correoReceptor: documento.correoReceptor,
    });
  }

  protected createFromForm(): IDocumento {
    return {
      ...new Documento(),
      id: this.editForm.get(['id'])!.value,
      rutEmpresa: this.editForm.get(['rutEmpresa'])!.value,
      idAmbiente: this.editForm.get(['idAmbiente'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      montoTotal: this.editForm.get(['montoTotal'])!.value,
      correoReceptor: this.editForm.get(['correoReceptor'])!.value,
    };
  }
}
