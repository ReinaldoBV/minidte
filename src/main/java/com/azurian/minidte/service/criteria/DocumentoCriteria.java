package com.azurian.minidte.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.azurian.minidte.domain.Documento} entity. This class is used
 * in {@link com.azurian.minidte.web.rest.DocumentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rutEmpresa;

    private IntegerFilter idAmbiente;

    private IntegerFilter tipoDocumento;

    private IntegerFilter montoTotal;

    private StringFilter correoReceptor;

    private Boolean distinct;

    public DocumentoCriteria() {}

    public DocumentoCriteria(DocumentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rutEmpresa = other.rutEmpresa == null ? null : other.rutEmpresa.copy();
        this.idAmbiente = other.idAmbiente == null ? null : other.idAmbiente.copy();
        this.tipoDocumento = other.tipoDocumento == null ? null : other.tipoDocumento.copy();
        this.montoTotal = other.montoTotal == null ? null : other.montoTotal.copy();
        this.correoReceptor = other.correoReceptor == null ? null : other.correoReceptor.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocumentoCriteria copy() {
        return new DocumentoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRutEmpresa() {
        return rutEmpresa;
    }

    public IntegerFilter rutEmpresa() {
        if (rutEmpresa == null) {
            rutEmpresa = new IntegerFilter();
        }
        return rutEmpresa;
    }

    public void setRutEmpresa(IntegerFilter rutEmpresa) {
        this.rutEmpresa = rutEmpresa;
    }

    public IntegerFilter getIdAmbiente() {
        return idAmbiente;
    }

    public IntegerFilter idAmbiente() {
        if (idAmbiente == null) {
            idAmbiente = new IntegerFilter();
        }
        return idAmbiente;
    }

    public void setIdAmbiente(IntegerFilter idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public IntegerFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public IntegerFilter tipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = new IntegerFilter();
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(IntegerFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public IntegerFilter getMontoTotal() {
        return montoTotal;
    }

    public IntegerFilter montoTotal() {
        if (montoTotal == null) {
            montoTotal = new IntegerFilter();
        }
        return montoTotal;
    }

    public void setMontoTotal(IntegerFilter montoTotal) {
        this.montoTotal = montoTotal;
    }

    public StringFilter getCorreoReceptor() {
        return correoReceptor;
    }

    public StringFilter correoReceptor() {
        if (correoReceptor == null) {
            correoReceptor = new StringFilter();
        }
        return correoReceptor;
    }

    public void setCorreoReceptor(StringFilter correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentoCriteria that = (DocumentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rutEmpresa, that.rutEmpresa) &&
            Objects.equals(idAmbiente, that.idAmbiente) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(montoTotal, that.montoTotal) &&
            Objects.equals(correoReceptor, that.correoReceptor) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rutEmpresa, idAmbiente, tipoDocumento, montoTotal, correoReceptor, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rutEmpresa != null ? "rutEmpresa=" + rutEmpresa + ", " : "") +
            (idAmbiente != null ? "idAmbiente=" + idAmbiente + ", " : "") +
            (tipoDocumento != null ? "tipoDocumento=" + tipoDocumento + ", " : "") +
            (montoTotal != null ? "montoTotal=" + montoTotal + ", " : "") +
            (correoReceptor != null ? "correoReceptor=" + correoReceptor + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
