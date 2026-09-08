# OftApp — Plataforma Móvil para Gestión de Exámenes Oftalmológicos

## Nombre y Propósito

**OftApp** es una aplicación móvil desarrollada para optimizar la organización, trazabilidad y consulta de exámenes oftalmológicos (como campimetría, topografía y agudeza visual). 

### Problema que resuelve
Actualmente, los resultados de los exámenes se generan en múltiples formatos (PDF, Excel, imágenes) y se almacenan de manera dispersa en correos o carpetas locales sin un registro centralizado. **OftApp** unifica estos antecedentes en una ficha digital por paciente ficticio, permitiendo al personal médico y administrativo consultar historiales, comparar estudios previos y gestionar los estados de entrega en tiempo real.

---

## Identidad Visual

### Logotipo
El isotipo integra la forma de un ojo estilizado con una cruz médica en su comisura exterior, representando la especialización clínica y el uso de tecnología médica.

![Logotipo de OftApp](docs/diseno/logo.png)

### Paleta de Colores

| Rol de Color | Código HEX | Descripción / Aplicación |
| :--- | :--- | :--- |
| **Color Principal** | `#0F80AA` | Azul oceánico para botones primarios, encabezados e íconos principales |
| **Color Secundario** | `#20BAC5` | Cian brillante para estados activos, acentos y la palabra "App" |
| **Fondo** | `#F8FAFC` | Blanco clínico con tinte frío para favorecer la lectura |
| **Texto Principal** | `#1D4E89` / `#334155` | Azul marino y gris oscuro para máxima legibilidad |
| **Adicionales** | `#3FC1C0` / `#04A6C2` | Verde agua y azul medio para estados, alertas y bordes |

---

## Flujo de Usuario

El siguiente diagrama ilustra el flujo de actividades según los roles de la aplicación (Tecnólogo, Administrativo, Médico y Paciente):

![Diagrama de Actividad UML](docs/diseno/diagrama_actividad.png)

---

## Pantallas Principales

Las maquetas y capturas de interfaz se encuentran organizadas en la carpeta `docs/diseno/`:

* **`docs/diseno/01_login.png`**: Pantalla de autenticación con selección de rol ficticio (**RF01**).
* **`docs/diseno/02_menuprincipal.png`**: Menú principal de la aplicación con accesos rápidos según el rol activo.
* **`docs/diseno/03_dashboard.png`**: Panel de control general con métricas, resúmenes y accesos directos del usuario.
* **`docs/diseno/04_lista_examenes.png`**: Catálogo general de exámenes con filtros de búsqueda por fecha, tipo, sucursal y estado (**RF07**).
* **`docs/diseno/05_registro_examen.png`**: Formulario para registrar atenciones y cargar exámenes con validación de campos obligatorios (**RF02, RF04, RF05**).
* **`docs/diseno/06_detalle_examen.png`**: Vista detallada de un examen que muestra tipo, ojo evaluado, estado, observaciones y el documento simulado (**RF03, RF08, RF09**).
* **`docs/diseno/07_historial_paciente.png`**: Trazabilidad y consulta del historial de exámenes anteriores asociados a un paciente ficticio (**RF06**).
---

## Tecnologías

* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** Jetpack Compose
* **Arquitectura:** MVVM (Model - ViewModel - UI - Repository)
* **Persistencia Local:** Room Database / DataStore
* **Navegación:** Jetpack Navigation Compose
* **Entorno de Desarrollo:** Android Studio

---

## Integrantes
**Nikki Alvarado - nikalvarado-debug
**Alvaro Oyarzún - Alvarooyar, majelss
**Benjamin Almonacid - benjamin-almonacid
**Kevin Mansilla

> **Asignatura:** Desarrollo de Aplicaciones Móviles  
> **Institución:** Duoc UC Sede Puerto Montt  
> **Profesor:** Marcelo Eduardo Crisóstomo Carrasco  
> **Cliente / Contexto:** MG Ingeniería Informática SPA  
> **Fecha:** Septiembre de 2026
