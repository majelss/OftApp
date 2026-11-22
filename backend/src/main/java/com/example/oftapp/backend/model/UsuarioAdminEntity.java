package com.example.oftapp.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para Usuarios gestionados por el rol Administrador.
 */
@Entity
@Table(name = "usuarios_admin")
public class UsuarioAdminEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String rol; // "medico", "paciente", "tecnologo", "administrativo", "administrador"

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String rut;

    @Column(nullable = false)
    private String email;

    private String telefono;
    private boolean exigirCambioClave;
    private String registroSis;
    private String especialidad;
    private String boxHabitual;
    private boolean cuentaActiva;
    private boolean permisoHistoricos;
    private boolean permisoExportacionMasiva;
    private boolean permisoAccesoRemoto;
    private String ultimaModificacion;

    public UsuarioAdminEntity() {
    }

    public UsuarioAdminEntity(
            String id,
            String rol,
            String nombreCompleto,
            String rut,
            String email,
            String telefono,
            boolean exigirCambioClave,
            String registroSis,
            String especialidad,
            String boxHabitual,
            boolean cuentaActiva,
            boolean permisoHistoricos,
            boolean permisoExportacionMasiva,
            boolean permisoAccesoRemoto,
            String ultimaModificacion
    ) {
        this.id = id;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.rut = rut;
        this.email = email;
        this.telefono = telefono;
        this.exigirCambioClave = exigirCambioClave;
        this.registroSis = registroSis;
        this.especialidad = especialidad;
        this.boxHabitual = boxHabitual;
        this.cuentaActiva = cuentaActiva;
        this.permisoHistoricos = permisoHistoricos;
        this.permisoExportacionMasiva = permisoExportacionMasiva;
        this.permisoAccesoRemoto = permisoAccesoRemoto;
        this.ultimaModificacion = ultimaModificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isExigirCambioClave() {
        return exigirCambioClave;
    }

    public void setExigirCambioClave(boolean exigirCambioClave) {
        this.exigirCambioClave = exigirCambioClave;
    }

    public String getRegistroSis() {
        return registroSis;
    }

    public void setRegistroSis(String registroSis) {
        this.registroSis = registroSis;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getBoxHabitual() {
        return boxHabitual;
    }

    public void setBoxHabitual(String boxHabitual) {
        this.boxHabitual = boxHabitual;
    }

    public boolean isCuentaActiva() {
        return cuentaActiva;
    }

    public void setCuentaActiva(boolean cuentaActiva) {
        this.cuentaActiva = cuentaActiva;
    }

    public boolean isPermisoHistoricos() {
        return permisoHistoricos;
    }

    public void setPermisoHistoricos(boolean permisoHistoricos) {
        this.permisoHistoricos = permisoHistoricos;
    }

    public boolean isPermisoExportacionMasiva() {
        return permisoExportacionMasiva;
    }

    public void setPermisoExportacionMasiva(boolean permisoExportacionMasiva) {
        this.permisoExportacionMasiva = permisoExportacionMasiva;
    }

    public boolean isPermisoAccesoRemoto() {
        return permisoAccesoRemoto;
    }

    public void setPermisoAccesoRemoto(boolean permisoAccesoRemoto) {
        this.permisoAccesoRemoto = permisoAccesoRemoto;
    }

    public String getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(String ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }
}
