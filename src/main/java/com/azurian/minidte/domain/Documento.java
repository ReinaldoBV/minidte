package com.azurian.minidte.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rut_empresa")
    private Integer rutEmpresa;

    @Column(name = "id_ambiente")
    private Integer idAmbiente;

    @Column(name = "tipo_documento")
    private Integer tipoDocumento;

    @Column(name = "monto_total")
    private Integer montoTotal;

    @Column(name = "correo_receptor")
    private String correoReceptor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Documento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRutEmpresa() {
        return this.rutEmpresa;
    }

    public Documento rutEmpresa(Integer rutEmpresa) {
        this.setRutEmpresa(rutEmpresa);
        return this;
    }

    public void setRutEmpresa(Integer rutEmpresa) {
        this.rutEmpresa = rutEmpresa;
    }

    public Integer getIdAmbiente() {
        return this.idAmbiente;
    }

    public Documento idAmbiente(Integer idAmbiente) {
        this.setIdAmbiente(idAmbiente);
        return this;
    }

    public void setIdAmbiente(Integer idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public Integer getTipoDocumento() {
        return this.tipoDocumento;
    }

    public Documento tipoDocumento(Integer tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getMontoTotal() {
        return this.montoTotal;
    }

    public Documento montoTotal(Integer montoTotal) {
        this.setMontoTotal(montoTotal);
        return this;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getCorreoReceptor() {
        return this.correoReceptor;
    }

    public Documento correoReceptor(String correoReceptor) {
        this.setCorreoReceptor(correoReceptor);
        return this;
    }

    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documento)) {
            return false;
        }
        return id != null && id.equals(((Documento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documento{" +
            "id=" + getId() +
            ", rutEmpresa=" + getRutEmpresa() +
            ", idAmbiente=" + getIdAmbiente() +
            ", tipoDocumento=" + getTipoDocumento() +
            ", montoTotal=" + getMontoTotal() +
            ", correoReceptor='" + getCorreoReceptor() + "'" +
            "}";
    }
}
