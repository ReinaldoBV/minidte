package com.azurian.minidte.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.azurian.minidte.domain.Documento} entity.
 */
public class DocumentoDTO implements Serializable {

    private Long id;

    private Integer rutEmpresa;

    private Integer idAmbiente;

    private Integer tipoDocumento;

    private Integer montoTotal;

    private String correoReceptor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRutEmpresa() {
        return rutEmpresa;
    }

    public void setRutEmpresa(Integer rutEmpresa) {
        this.rutEmpresa = rutEmpresa;
    }

    public Integer getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(Integer idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getCorreoReceptor() {
        return correoReceptor;
    }

    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoDTO)) {
            return false;
        }

        DocumentoDTO documentoDTO = (DocumentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoDTO{" +
            "id=" + getId() +
            ", rutEmpresa=" + getRutEmpresa() +
            ", idAmbiente=" + getIdAmbiente() +
            ", tipoDocumento=" + getTipoDocumento() +
            ", montoTotal=" + getMontoTotal() +
            ", correoReceptor='" + getCorreoReceptor() + "'" +
            "}";
    }
}
