package com.example.oftapp.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para la Bitácora Criptográfica Inmutable de Auditoría del Sistema.
 */
@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String categoria; // "usuarios", "parametros", "examenes", "fichas"

    @Column(nullable = false)
    private String estado; // "Autorizado", "Creado", "Configuración", "Desactivado"

    private String operador;
    private String afectadoEtiqueta;
    private String afectadoValor;
    private String detalleTitulo;

    @Column(length = 1000)
    private String detalleTexto;

    private String valorPrevio;
    private String valorNuevo;
    private String fechaGrupo;
    private String hora;
    private String hashSha256;
    private long timestampMillis;

    public AuditLogEntity() {
    }

    public AuditLogEntity(
            String id,
            String titulo,
            String categoria,
            String estado,
            String operador,
            String afectadoEtiqueta,
            String afectadoValor,
            String detalleTitulo,
            String detalleTexto,
            String valorPrevio,
            String valorNuevo,
            String fechaGrupo,
            String hora,
            String hashSha256,
            long timestampMillis
    ) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.estado = estado;
        this.operador = operador;
        this.afectadoEtiqueta = afectadoEtiqueta;
        this.afectadoValor = afectadoValor;
        this.detalleTitulo = detalleTitulo;
        this.detalleTexto = detalleTexto;
        this.valorPrevio = valorPrevio;
        this.valorNuevo = valorNuevo;
        this.fechaGrupo = fechaGrupo;
        this.hora = hora;
        this.hashSha256 = hashSha256;
        this.timestampMillis = timestampMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getAfectadoEtiqueta() {
        return afectadoEtiqueta;
    }

    public void setAfectadoEtiqueta(String afectadoEtiqueta) {
        this.afectadoEtiqueta = afectadoEtiqueta;
    }

    public String getAfectadoValor() {
        return afectadoValor;
    }

    public void setAfectadoValor(String afectadoValor) {
        this.afectadoValor = afectadoValor;
    }

    public String getDetalleTitulo() {
        return detalleTitulo;
    }

    public void setDetalleTitulo(String detalleTitulo) {
        this.detalleTitulo = detalleTitulo;
    }

    public String getDetalleTexto() {
        return detalleTexto;
    }

    public void setDetalleTexto(String detalleTexto) {
        this.detalleTexto = detalleTexto;
    }

    public String getValorPrevio() {
        return valorPrevio;
    }

    public void setValorPrevio(String valorPrevio) {
        this.valorPrevio = valorPrevio;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public String getFechaGrupo() {
        return fechaGrupo;
    }

    public void setFechaGrupo(String fechaGrupo) {
        this.fechaGrupo = fechaGrupo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }
}
