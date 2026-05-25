# SafeZone Backend

Backend de la plataforma web **SafeZone**, orientada al registro y seguimiento de casos de violencia familiar.  
El sistema centraliza la gestion de denuncias, casos, citas, asignaciones, evidencias, notificaciones y trazabilidad operativa para los distintos roles de atencion.

## Objetivo del backend

Proveer una API REST segura y modular para:

- Registrar y administrar denuncias y casos.
- Dar seguimiento al ciclo de vida de cada caso.
- Gestionar usuarios y control de acceso por autenticacion.
- Mantener trazabilidad y configuracion operativa del sistema.

## Stack y dependencias principales

- **Java 21**
- **Spring Boot 3.5.13**
- **Spring Web** (`spring-boot-starter-web`)
- **Spring Data JPA** (`spring-boot-starter-data-jpa`)
- **Spring Security** (`spring-boot-starter-security`)
- **Bean Validation** (`spring-boot-starter-validation`)
- **MySQL Connector/J** (`mysql-connector-j`)
- **Flyway** (`flyway-core`)
- **OpenAPI/Swagger UI** (`springdoc-openapi-starter-webmvc-ui`)
- **Lombok**
- **JUnit/Spring Test** (`spring-boot-starter-test`, `spring-security-test`)

## Arquitectura y estructura del proyecto

El proyecto esta organizado en capas para separar responsabilidades:

- `domain`: logica de aplicacion (servicios, contratos de repositorio, DTOs, mapeos, validaciones, enums).
- `persistance`: entidades JPA que representan el modelo persistente.
- `web`: capa de entrada HTTP (controladores REST).
- `shared`: componentes transversales (configuracion, seguridad, excepciones, auditoria, utilidades y respuestas comunes).

### Estructura base actual

```text
src/main/java/com/utp/safezonebackend
├── domain
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── enums
│   ├── mapper
│   ├── repository
│   ├── service
│   └── validator
├── persistance
│   └── entity
├── shared
│   ├── audit
│   ├── config
│   ├── exception
│   ├── response
│   ├── security
│   └── util
└── web
    └── controller
```

## Modulos funcionales implementados

El backend contempla los siguientes modulos de negocio:

- **Auth**
- **Usuarios**
- **Denuncias**
- **Casos**
- **Seguimientos de caso**
- **Asignaciones de caso**
- **Citas**
- **Evidencias**
- **Notificaciones**
- **Reportes**
- **Configuracion del sistema**
- **Auditoria**
- **Victima Alias**
- **Refresh Token**

## Modelo persistente (entidades)

Entidades principales registradas en `persistance/entity`:

- `Usuario`
- `Denuncia`
- `Caso`
- `SeguimientoCaso`
- `AsignacionCaso`
- `Cita`
- `Evidencia`
- `Notificacion`
- `ConfiguracionSistema`
- `Auditoria`
- `VictimaAlias`
- `RefreshToken`

## Documentacion tecnica complementaria

- Carpeta `docs/`:
  - `LOGICA_NEGOCIO.md`: definicion funcional del sistema, reglas por modulo, permisos por rol y flujo de atencion.
  - `safezonedb_export_20260525_061915.sql`: export completo de la base de datos (`safezonedb`) para importacion en entornos locales.
- Carpeta `informe/`: documentos de soporte funcional y tecnico del proyecto.
- Carpeta `src/main/resources/bd/`: referencia de estructura de base de datos.
