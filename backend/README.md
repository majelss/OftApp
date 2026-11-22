# OftApp Backend Microservice (Spring Boot 3 + JPA/H2 + Swagger/OpenAPI)

Microservicio clínico para **OftApp** desarrollado con **Spring Boot 3.3.5**, **Spring Data JPA**, base de datos **H2** y documentación interactiva **Springdoc OpenAPI / Swagger UI**.

## 1. Ejecución del Microservicio

Puedes abrir la carpeta `backend/` directamente en **IntelliJ IDEA**, **Eclipse**, **VS Code** o desde terminal:

### Con Maven:
```bash
cd backend
mvn spring-boot:run
```

### Con Gradle (usando el wrapper de la raíz o Gradle local):
```powershell
cd backend
..\gradlew.bat bootRun
```

El servidor se iniciará en el puerto **`8080`**.

---

## 2. URLs de Documentación y Base de Datos

- **Swagger UI (Documentación Interactiva):**  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Esquema OpenAPI 3 (JSON):**  
  [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Consola H2 Database:**  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  *(JDBC URL: `jdbc:h2:mem:oftappdb` | User: `sa` | Password: vacío)*

---

## 3. Endpoints Principales (`/api/v1`)

| Método | Ruta | Rol (`X-User-Role`) | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Todos | Autentica credenciales simuladas y retorna token sintético + rol |
| `GET` | `/api/v1/auth/roles` | Todos | Lista los roles y permisos del sistema |
| `GET` | `/api/v1/pacientes` | Todos (`PACIENTE` solo ve su ficha) | Lista pacientes sintéticos |
| `GET` | `/api/v1/pacientes/{id}` | Todos | Obtiene paciente por ID |
| `GET` | `/api/v1/pacientes/rut/{rut}` | Todos | Obtiene paciente por RUT chileno |
| `POST` | `/api/v1/pacientes` | `MEDICO`, `TECNICO`, `ADMINISTRADOR` | Registra nuevo paciente sintético |
| `PUT` | `/api/v1/pacientes/{id}` | `MEDICO`, `ADMINISTRADOR` | Actualiza ficha de paciente |
| `DELETE` | `/api/v1/pacientes/{id}` | `ADMINISTRADOR` | Elimina paciente sintético |
| `GET` | `/api/v1/examenes` | Todos (`?pacienteRut=...&estado=...`) | Lista y filtra exámenes oftalmológicos |
| `GET` | `/api/v1/examenes/{id}` | Todos | Detalle de un examen oftalmológico |
| `POST` | `/api/v1/examenes` | `MEDICO`, `TECNICO`, `ADMINISTRADOR` | Registra examen y añade traza en Auditoría |
| `PUT` | `/api/v1/examenes/{id}` | `MEDICO`, `ADMINISTRADOR` | Actualiza examen oftalmológico |
| `PATCH` | `/api/v1/examenes/{id}/estado` | `MEDICO`, `ADMINISTRADOR` | Actualiza estado clínico (`Validado`, `Pendiente`, `Observado`) |
| `DELETE` | `/api/v1/examenes/{id}` | `ADMINISTRADOR` | Elimina examen oftalmológico |
| `GET` | `/api/v1/admin/usuarios` | `ADMINISTRADOR` | Lista usuarios clínicos gestionados |
| `POST` | `/api/v1/admin/usuarios` | `ADMINISTRADOR` | Crea/guarda usuario clínico |
| `PUT` | `/api/v1/admin/usuarios/{id}` | `ADMINISTRADOR` | Modifica permisos/datos de usuario |
| `PATCH` | `/api/v1/admin/usuarios/{id}/suspender` | `ADMINISTRADOR` | Suspende preventivamente una cuenta |
| `GET` | `/api/v1/admin/auditoria` | `ADMINISTRADOR` | Consulta la Bitácora Criptográfica Inmutable |
| `POST` | `/api/v1/admin/auditoria` | `ADMINISTRADOR` | Registra nuevo evento forense con SHA-256 |
