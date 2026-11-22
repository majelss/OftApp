# OftApp — Plataforma Móvil y Microservicios para Gestión de Exámenes Oftalmológicos (v2.4.0)

<p align="center">
  <img src="docs/diseno/logo.png" alt="Logo OftApp" width="280px"/>
</p>

## 1. Descripción General, Problemática y Alcance Ético

**OftApp** es un MVP académico integral desarrollado en **Kotlin + Jetpack Compose (Material 3)** con arquitectura **MVVM + Clean Repository Offline-First (Room SQLite)** integrado mediante **Retrofit** a un microservicio propio en **Spring Boot 3 (JPA/H2 + Swagger/OpenAPI)** y a la API REST pública estándar **HL7 HAPI FHIR R4**.

### Problemática Oftalmológica que Resuelve
En operativos y clínicas oftalmológicas, los resultados diagnósticos (campimetrías computarizadas Humphrey, tomografías OCT, topografías corneales Pentacam y tonometrías) suelen encontrarse dispersos en múltiples formatos (informes PDF, capturas de fondo de ojo y tablas numéricas), dificultando su trazabilidad histórica y el acceso accesible para **pacientes con dificultades visuales** o baja visión posdilatación.

**OftApp** centraliza el ciclo clínico completo separando los flujos por rol (**Paciente**, **Médico/Tecnólogo** y **Administrador SIS**), garantizando operación continua incluso sin conexión a internet (**«Modo offline»** con **«Sincronización pendiente»** en Room SQLite), visualización integrada de documentos PDF (`PdfRenderer`) e imágenes retinianas con zoom táctil (`Coil`), y cumplimiento de criterios de accesibilidad **WCAG 2.2 Nivel AA / TalkBack**.

> **Declaración Ética y de Privacidad (Anexo Técnico F):**  
> Todos los pacientes, profesionales, RUTs, firmas electrónicas, documentos PDF e imágenes retinianas contenidos en este repositorio y en la base de datos son **100% ficticios y sintéticos**. La aplicación es un prototipo académico para la asignatura *DSY1105 Desarrollo de Aplicaciones Móviles* (Duoc UC) y no almacena datos personales reales ni claves privadas (`.jks`).

---

## 2. Integrantes del Equipo

* **Nikki Alvarado** — *nikalvarado-debug* (Registro y Detalle de Exámenes, Visor PDF/Coil, Integración de Dispositivo)
* **Álvaro Oyarzún** — *Alvarooyar*, *majelss* (Autenticación por Rol, Keystore, Menú Principal y Módulo Administrador)
* **Kevin Mansilla** — *Sneiker02* (Dashboard Clínico, Catálogo Reactivo con Filtros, Sincronización Offline/Online y API Externa FHIR)
* **Benjamín Almonacid** — *benjamin-almonacid* (Historial Clínico del Paciente, Persistencia Room/Spring Boot y Suite de Pruebas >90%)

> **Asignatura:** DSY1105 Desarrollo de Aplicaciones Móviles — **Duoc UC Sede Puerto Montt**  
> **Docente:** Marcelo Eduardo Crisóstomo Carrasco  
> **Evaluaciones Cubiertas:** Evaluación Parcial 4 (Entrega de Encargo) y Evaluación Parcial 5 (Defensa Individual)

---

## 3. Matriz de Cumplimiento de Rúbrica (EP4 y EP5)

| Requisito / Indicador | Implementación Verificable en `OftApp` |
| :--- | :--- |
| **1. Interfaz accesible y funcional por rol (IE 3.1.1)** | Flujos aislados en `navigation/navi.kt` para **Paciente** (`PACIENTE_HOME`, `PACIENTE_EXAMENES`, `PACIENTE_HISTORIAL`, `PACIENTE_COMPARTIR`, `PACIENTE_PERFIL`), **Médico/Tecnólogo** (`MAIN_MENU`, `DASHBOARD`, `LISTA_ATENCIONES`, `REGISTRAR_EXAMEN`, `HISTORIAL`, `DETALLE_EXAMEN`) y **Administrador** (`ADMIN_USUARIOS`, `ADMIN_SISTEMA`, `ADMIN_AUDITORIA`). Accesibilidad **TalkBack** (`semantics { heading(); liveRegion = LiveRegionMode.Polite; contentDescription }`), áreas táctiles `≥ 48.dp` y alto contraste. |
| **2. Microservicios Spring Boot + Swagger UI (IE 3.1.2)** | Proyecto completo en carpeta `backend/` (Spring Boot 3.3.5 + JPA + H2 + Springdoc OpenAPI 2.6.0) con endpoints CRUD para pacientes, exámenes, usuarios y auditoría, control de rol por cabecera `X-User-Role` y **Swagger UI** en `http://localhost:8080/swagger-ui/index.html`. |
| **3. Integración Retrofit y API REST Externa (IE 3.1.3 / 3.1.4)** | Cliente `OftAppApiService` (`data/remote/`) para microservicios propios y `FhirExternalApiService` que consume la API pública **HAPI FHIR R4** (`https://hapi.fhir.org/baseR4/DiagnosticReport`) mostrando estudios sintéticos LOINC en `DashboardScreen` y `ExamListScreen`. |
| **4. Persistencia Offline Robusta y Android Keystore** | Base local Room v3 (`OftAppDatabase`, `ExamenEntity` con `pendienteSincronizacion`). Ante caída del backend, activa **«Modo offline»** y **«Sincronización pendiente»** con botón de resincronización. Token de sesión cifrado con `EncryptedSharedPreferences` (`AES256_GCM` en **Android Keystore**) en `SessionTokenManager.kt`. |
| **5. Visor PDF (`PdfRenderer`) e Imágenes `Coil` con Zoom** | `PdfViewerComponent.kt` genera y renderiza informes clínicos sintéticos multipágina con `android.graphics.pdf.PdfRenderer` y visualiza retinografías/topografías con `AsyncImage` (**Coil**) y gesto de zoom multitáctil (`detectTransformGestures`). |
| **6. Pruebas Unitarias (>90%) y Compose UI Test (IE 3.2.1)** | Suite JUnit 4 + **MockK** + `kotlinx-coroutines-test` en `app/src/test/` cubriendo todos los ViewModels y Repositorios con reporte **JaCoCo >90%** (`jacocoTestReport`), más prueba instrumentada **Compose UI Test** en `OftAppComposeUiTest.kt`. |
| **7. APK Release Firmada y Versionada (IE 3.3.1)** | `versionCode = 2`, `versionName = "2.4.0"` y bloque `signingConfigs.create("release")` en `app/build.gradle.kts` parametrizado mediante variables seguras (`local.properties` / entorno) y plantilla `keystore.properties.example`. |

---

## 4. Arquitectura y Separación de Paquetes

El proyecto Android implementa una separación estricta de responsabilidades bajo el patrón **MVVM (Model - View - ViewModel) + Repository**:

```text
app/src/main/java/com/example/oftapp/
├── data/
│   ├── local/                  # Persistencia Room (OftAppDatabase, ExamenDao, AdminDao, Entidades)
│   │   └── SessionTokenManager.kt  # Cifrado AES256_GCM respaldado por Android Keystore
│   ├── remote/                 # Retrofit2 + OkHttp (OftAppApiService, FhirExternalApiService, RetrofitClient)
│   ├── model/                  # Modelos de datos de eventos clínicos
│   ├── repository/             # HistorialRepository
│   ├── ExamRepository.kt       # Repositorio Offline-First (Spring Boot <-> Room + HAPI FHIR)
│   └── AdminRepository.kt      # Repositorio de Usuarios, Configuración y Auditoría SHA-256
├── model/                      # Modelos de dominio transversales (ExamenModels, UsuarioModel, Roles)
├── viewmodel/                  # Capa de Lógica de Presentación separada de la UI (StateFlow)
│   ├── ExamRegisterViewModel.kt
│   ├── ExamListViewModel.kt
│   ├── ExamDetailViewModel.kt
│   ├── DashboardViewModel.kt
│   ├── PatientHistoryViewModel.kt
│   ├── AdminUserViewModel.kt
│   ├── AdminAuditViewModel.kt
│   └── AdminSystemConfigViewModel.kt
├── ui/
│   ├── components/             # Componentes reutilizables (PdfViewerComponent, OfflineSyncAndFhirComponents, FechaExamenPicker)
│   ├── screens/                # Vistas Jetpack Compose organizadas por módulo (auth, dashboard, examenes, pacientes, admin)
│   └── theme/                  # Sistema de diseño Material 3 y paleta Clinical Clarity
└── navigation/
    └── navi.kt                 # NavHost con control de sesión y aislamiento estricto por rol
```

---

## 5. Esquema de Entidades (Room SQLite y JPA Backend)

1. **`ExamenEntity` (`examenes`)**:
   - `id` (PK, String, ej. `EXAM-2026-001`), `pacienteRut`, `pacienteNombre`, `pacienteEdad`, `tipoExamen`, `protocolo`, `sucursalBox`, `ojoEvaluado` (`OD`, `OI`, `AO`), `documentoNombre`, `documentoTamano`, `documentoFormato`, `documentoUri`, `observaciones`, `estado` (`Validado`, `Pendiente`, `Observado`, `Entregado`), `fecha`, `firmaElectronica`, `pendienteSincronizacion` (Boolean).
2. **`PacienteEntity` (`pacientes`)**:
   - `id` (PK, Long), `rut`, `nombreCompleto`, `edad`, `prevision`, `activo`, `codigoFicha`.
3. **`UsuarioAdminEntity` (`usuarios_admin`)**:
   - `id` (PK, String, ej. `USR-84920`), `rol`, `nombreCompleto`, `rut`, `email`, `telefono`, `exigirCambioClave`, `registroSis`, `especialidad`, `boxHabitual`, `cuentaActiva`, `permisoHistoricos`, `permisoExportacionMasiva`, `permisoAccesoRemoto`, `ultimaModificacion`.
4. **`AuditLogEntity` (`audit_logs`)**:
   - `id` (PK, String, ej. `#LOG-882190`), `titulo`, `categoria`, `estado`, `operador`, `afectadoEtiqueta`, `afectadoValor`, `detalleTitulo`, `detalleTexto`, `valorPrevio`, `valorNuevo`, `fechaGrupo`, `hora`, `hashSha256`, `timestampMillis`.
5. **`SystemConfigEntity` (`system_config`)**:
   - `id` (PK = 1), `vigenciaCodigos` (`24h`, `48h`, `7d`), `umbralPio` (`21 mmHg`), `compresionDicom`, `sucursalesRaw`, `twoFaObligatorio`, `bloqueoInactividad`, `cifradoExtremo`, `ultimoRespaldoFecha`, `ultimoRespaldoTamano`.

---

## 6. Endpoints Propios (Spring Boot) y API REST Externa (HAPI FHIR)

### A. Microservicio Propio Spring Boot (`http://localhost:8080`)
- **Swagger UI Interactivo:** `http://localhost:8080/swagger-ui/index.html`
- **Esquema OpenAPI 3 JSON:** `http://localhost:8080/v3/api-docs`
- **Consola H2 Database:** `http://localhost:8080/h2-console` (`jdbc:h2:mem:oftappdb`)

| Método | Endpoint | Cabecera `X-User-Role` | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Todos | Autentica credenciales simuladas y emite token sintético + rol |
| `GET` | `/api/v1/auth/roles` | Todos | Matriz de roles y permisos del sistema |
| `GET` | `/api/v1/pacientes` | Todos (`PACIENTE` filtra su RUT) | Lista fichas de pacientes sintéticos |
| `POST` | `/api/v1/pacientes` | `MEDICO`, `TECNICO`, `ADMINISTRADOR` | Crea paciente sintético |
| `GET` | `/api/v1/examenes` | Todos (`?pacienteRut=&estado=`) | Lista exámenes oftalmológicos con filtros |
| `POST` | `/api/v1/examenes` | `MEDICO`, `TECNICO`, `ADMINISTRADOR` | Registra examen y genera evento en bitácora de auditoría |
| `PUT` | `/api/v1/examenes/{id}` | `MEDICO`, `ADMINISTRADOR` | Actualiza datos completos de un examen |
| `PATCH` | `/api/v1/examenes/{id}/estado` | `MEDICO`, `ADMINISTRADOR` | Actualiza estado clínico (`Validado`, `Pendiente`, `Observado`) |
| `DELETE` | `/api/v1/examenes/{id}` | `ADMINISTRADOR` | Elimina un examen oftalmológico |
| `GET` | `/api/v1/admin/usuarios` | `ADMINISTRADOR` | Lista usuarios clínicos gestionados |
| `POST` | `/api/v1/admin/usuarios` | `ADMINISTRADOR` | Crea o actualiza usuario clínico |
| `GET` | `/api/v1/admin/auditoria` | `ADMINISTRADOR` | Consulta la Bitácora Criptográfica Inmutable SHA-256 |

### B. API REST Externa Pública (HL7 HAPI FHIR R4)
- **Base URL:** `https://hapi.fhir.org/baseR4/`
- **Recurso Consumido:** `GET https://hapi.fhir.org/baseR4/DiagnosticReport?_count=4&_format=json`
- **Integración Visual:** Tarjeta **"Estándar HL7 FHIR R4 (API REST Externa)"** en `DashboardScreen` y `ExamListScreen`, con caché sintético de respaldo en caso de operar sin internet.

---

## 7. Instrucciones Reproducibles de Ejecución, Pruebas y Firma de APK

### Paso 1: Levantar el Microservicio Backend Spring Boot
```powershell
cd backend
..\gradlew.bat bootRun
# O con Maven:
mvn spring-boot:run
```
Abre en tu navegador: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

> **Nota para Teléfono Físico:** En emulador Android la app apunta por defecto a `http://10.0.2.2:8080/`. Si conectas un teléfono físico por USB, puedes redirigir el puerto del computador al teléfono con un solo comando ADB sin cambiar código:
> ```powershell
> adb reverse tcp:8080 tcp:8080
> ```
> Con `adb reverse tcp:8080 tcp:8080` y cambiando `RetrofitClient.backendBaseUrl = "http://localhost:8080/"` (o usando la IP LAN de tu PC), el teléfono físico consumirá tu Spring Boot local directamente por el cable USB.

### Paso 2: Ejecutar Pruebas Unitarias y Generar Reporte de Cobertura (>90%)
```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport
```
- **Salida de pruebas JUnit + MockK:** `app/build/reports/tests/testDebugUnitTest/index.html`
- **Reporte de Cobertura JaCoCo (>90%):** `app/build/reports/jacoco/jacocoTestReport/html/index.html`

### Paso 3: Ejecutar Prueba Instrumentada (Compose UI Test)
Con un emulador o teléfono físico conectado:
```powershell
.\gradlew.bat connectedDebugAndroidTest
```
Ejecuta `OftAppComposeUiTest`, validando ingreso de credenciales inválidas (mensaje de error visible) y credenciales válidas (navegación efectiva).

### Paso 4: Generar e Instalar la APK Release Firmada
1. Genera tu llave `.jks` fuera del repositorio (o en una ruta ignorada por Git):
   ```powershell
   keytool -genkeypair -v -keystore C:/Users/nikki/oftapp-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias oftapp_key
   ```
2. Agrega las variables en tu archivo local `local.properties` (guiándote por `keystore.properties.example`):
   ```properties
   RELEASE_STORE_FILE=C:/Users/nikki/oftapp-release-key.jks
   RELEASE_STORE_PASSWORD=tu_password_seguro
   RELEASE_KEY_ALIAS=oftapp_key
   RELEASE_KEY_PASSWORD=tu_password_seguro
   ```
3. Compila e instala el APK `release` firmado (`versionCode = 2`, `versionName = "2.4.0"`):
   ```powershell
   .\gradlew.bat assembleRelease
   adb install -r app/build/outputs/apk/release/app-release.apk
   ```

---

## 8. Guía Rápida para el Cambio Transversal en Vivo (<5 Minutos — Defensa EP5)

Durante el **Paso 9 de la Defensa Individual (40% de la nota de EP5)**, el docente pedirá agregar un campo ficticio (por ejemplo `observacionBreve` o `prioridadClinica`) a un examen. Sigue estos 5 puntos exactos:

1. **Backend Spring Boot (`backend/.../model/ExamenEntity.java`)**:
   - Agrega `private String prioridadClinica = "Normal";` con su getter y setter. Reinicia `bootRun` y verifica que aparece en `/swagger-ui/index.html`.
2. **DTO Retrofit (`app/.../data/remote/OftAppApiService.kt`)**:
   - En `ExamenRemoteDto`, añade `val prioridadClinica: String? = "Normal"`.
3. **Entidad Room y Modelo (`app/.../data/local/ExamenEntity.kt` y `model/ExamenModels.kt`)**:
   - Añade `val prioridadClinica: String = "Normal"` en `ExamenEntity` y `ExamenOftalmologico` (sube `version = 4` en `OftAppDatabase.kt`).
4. **Repositorio y ViewModel (`ExamRepository.kt` y `ExamListViewModel.kt`)**:
   - Pasa `prioridadClinica` en los mappers `toDomain()`, `toEntity()` y `toRemoteDto()`.
5. **Pantalla Compose (`ExamListScreen.kt` o `ExamRegisterScreen.kt`)**:
   - Muestra `Text("Prioridad: ${atencion.prioridadClinica}")` en la tarjeta del examen.
