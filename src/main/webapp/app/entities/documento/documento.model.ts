export interface IDocumento {
  id?: number;
  rutEmpresa?: number | null;
  idAmbiente?: number | null;
  tipoDocumento?: number | null;
  montoTotal?: number | null;
  correoReceptor?: string | null;
}

export class Documento implements IDocumento {
  constructor(
    public id?: number,
    public rutEmpresa?: number | null,
    public idAmbiente?: number | null,
    public tipoDocumento?: number | null,
    public montoTotal?: number | null,
    public correoReceptor?: string | null
  ) {}
}

export function getDocumentoIdentifier(documento: IDocumento): number | undefined {
  return documento.id;
}
