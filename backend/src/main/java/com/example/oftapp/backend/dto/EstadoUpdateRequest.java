package com.example.oftapp.backend.dto;

public class EstadoUpdateRequest {
    private String nuevoEstado;
    private String observaciones;
    private String firmaElectronica;

    public EstadoUpdateRequest() {
    }

    public EstadoUpdateRequest(String nuevoEstado, String observaciones, String firmaElectronica) {
        this.nuevoEstado = nuevoEstado;
        this.observaciones = observaciones;
        this.firmaElectronica = firmaElectronica;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(String firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }
}
