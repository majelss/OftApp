package com.example.oftapp.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para Exámenes Oftalmológicos Sintéticos.
 * Mapea los mismos campos que la entidad Room de la aplicación móvil Android.
 */
@Entity
@Table(name = "examenes")
public class ExamenEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String pacienteRut;

    @Column(nullable = false)
    private String pacienteNombre;

    private int pacienteEdad;

    @Column(nullable = false)
    private String tipoExamen;

    private String protocolo;

    @Column(nullable = false)
    private String sucursalBox;

    @Column(nullable = false)
    private String ojoEvaluado; // "OD", "OI", "AO"

    private String documentoNombre;

    private String documentoTamano;

    private String documentoFormato;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private String estado; // "Validado", "Pendiente", "Observado", "Entregado"

    @Column(nullable = false)
    private String fecha;

    private String firmaElectronica;

    public ExamenEntity() {
    }

    public ExamenEntity(
            String id,
            String pacienteRut,
            String pacienteNombre,
            int pacienteEdad,
            String tipoExamen,
            String protocolo,
            String sucursalBox,
            String ojoEvaluado,
            String documentoNombre,
            String documentoTamano,
            String documentoFormato,
            String observaciones,
            String estado,
            String fecha,
            String firmaElectronica
    ) {
        this.id = id;
        this.pacienteRut = pacienteRut;
        this.pacienteNombre = pacienteNombre;
        this.pacienteEdad = pacienteEdad;
        this.tipoExamen = tipoExamen;
        this.protocolo = protocolo;
        this.sucursalBox = sucursalBox;
        this.ojoEvaluado = ojoEvaluado;
        this.documentoNombre = documentoNombre;
        this.documentoTamano = documentoTamano;
        this.documentoFormato = documentoFormato;
        this.observaciones = observaciones;
        this.estado = estado;
        this.fecha = fecha;
        this.firmaElectronica = firmaElectronica;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacienteRut() {
        return pacienteRut;
    }

    public void setPacienteRut(String pacienteRut) {
        this.pacienteRut = pacienteRut;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public int getPacienteEdad() {
        return pacienteEdad;
    }

    public void setPacienteEdad(int pacienteEdad) {
        this.pacienteEdad = pacienteEdad;
    }

    public String getTipoExamen() {
        return tipoExamen;
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getSucursalBox() {
        return sucursalBox;
    }

    public void setSucursalBox(String sucursalBox) {
        this.sucursalBox = sucursalBox;
    }

    public String getOjoEvaluado() {
        return ojoEvaluado;
    }

    public void setOjoEvaluado(String ojoEvaluado) {
        this.ojoEvaluado = ojoEvaluado;
    }

    public String getDocumentoNombre() {
        return documentoNombre;
    }

    public void setDocumentoNombre(String documentoNombre) {
        this.documentoNombre = documentoNombre;
    }

    public String getDocumentoTamano() {
        return documentoTamano;
    }

    public void setDocumentoTamano(String documentoTamano) {
        this.documentoTamano = documentoTamano;
    }

    public String getDocumentoFormato() {
        return documentoFormato;
    }

    public void setDocumentoFormato(String documentoFormato) {
        this.documentoFormato = documentoFormato;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(String firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }
}
