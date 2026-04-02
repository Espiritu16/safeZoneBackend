# Esquema de Base de Datos (Azure Cosmos DB - NoSQL)

Este documento define los contenedores, su clave de particion y los atributos del proyecto SafeZone.

## 1) Contenedor: `usuarios`
- Clave de particion: `/id`
- Uso: almacenar cuentas del sistema (victima, psicologo, defensor, admin).

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico del usuario. |
| `rol` | `string` | Rol del usuario: `VICTIMA`, `PSICOLOGO`, `DEFENSOR`, `ADMIN`. |
| `correo` | `string` | Correo de acceso (login) y contacto. |
| `contrasenaHash` | `string` | Hash seguro de la contrasena (nunca texto plano). |
| `nombres` | `string` | Nombres de la persona. |
| `apellidos` | `string` | Apellidos de la persona. |
| `dni` | `string` | Identificacion personal (DNI). |
| `telefono` | `string` | Numero de contacto. |
| `distrito` | `string` | Distrito en Lima para ubicacion operativa. |
| `activo` | `boolean` | Si la cuenta esta habilitada o bloqueada. |
| `fechaCreacion` | `datetime (ISO-8601)` | Fecha de registro de la cuenta. |
| `fechaActualizacion` | `datetime (ISO-8601)` | Ultima actualizacion del perfil. |

## 2) Contenedor: `casos`
- Clave de particion: `/victimaId`
- Uso: expediente principal de atencion.

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico del caso. |
| `victimaId` | `string` | Referencia al usuario victima propietario del caso. |
| `estado` | `string` | Estado del caso: `ABIERTO`, `EN_SEGUIMIENTO`, `CERRADO`. |
| `prioridad` | `string` | Nivel de prioridad: `BAJA`, `MEDIA`, `ALTA`, `CRITICA`. |
| `resumen` | `string` | Descripcion corta para listados. |
| `distrito` | `string` | Distrito relacionado al caso. |
| `fechaCreacion` | `datetime (ISO-8601)` | Fecha de apertura del caso. |
| `fechaCierre` | `datetime (ISO-8601) \| null` | Fecha de cierre, si el caso ya finalizo. |

## 3) Contenedor: `denuncias`
- Clave de particion: `/casoId`
- Uso: denuncias o reportes detallados del hecho.

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico de la denuncia. |
| `casoId` | `string` | Caso al que pertenece la denuncia. |
| `victimaId` | `string` | Usuario victima que reporta. |
| `descripcion` | `string` | Detalle del hecho denunciado. |
| `tipoViolencia` | `string` | Tipo de violencia (fisica, psicologica, etc.). |
| `fechaIncidente` | `datetime (ISO-8601)` | Fecha y hora del incidente. |
| `distrito` | `string` | Distrito donde ocurrio el hecho. |
| `direccionReferencia` | `string \| null` | Punto de referencia de ubicacion (opcional). |
| `nivelRiesgo` | `string` | Riesgo del caso: `BAJO`, `MEDIO`, `ALTO`, `CRITICO`. |
| `anonima` | `boolean` | Si la denuncia se trata como anonima. |
| `adjuntos` | `array` | Evidencias asociadas (fotos, docs, URLs). |
| `fechaCreacion` | `datetime (ISO-8601)` | Fecha de registro de la denuncia. |

## 4) Contenedor: `asignaciones_caso`
- Clave de particion: `/casoId`
- Uso: vincular profesionales a un caso.

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico de la asignacion. |
| `casoId` | `string` | Caso donde se asigna al profesional. |
| `profesionalId` | `string` | Usuario asignado (psicologo o defensor). |
| `rolProfesional` | `string` | Rol del asignado: `PSICOLOGO` o `DEFENSOR`. |
| `activo` | `boolean` | Indica si la asignacion esta vigente. |
| `fechaAsignacion` | `datetime (ISO-8601)` | Inicio de la asignacion. |
| `fechaFin` | `datetime (ISO-8601) \| null` | Fin de la asignacion (si aplica). |
| `asignadoPor` | `string` | ID del admin que realiza la asignacion. |

## 5) Contenedor: `seguimientos_caso`
- Clave de particion: `/casoId`
- Uso: registrar notas de seguimiento psicologico/legal/administrativo.

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico del seguimiento. |
| `casoId` | `string` | Caso asociado al seguimiento. |
| `autorId` | `string` | Usuario que registra la nota. |
| `rolAutor` | `string` | Rol del autor: `PSICOLOGO`, `DEFENSOR`, `ADMIN`. |
| `tipoSeguimiento` | `string` | Tipo de avance (sesion, llamada, avance legal, etc.). |
| `contenido` | `string` | Detalle del seguimiento realizado. |
| `proximaAccion` | `string \| null` | Siguiente accion acordada para el caso. |
| `fechaProximaAccion` | `datetime (ISO-8601) \| null` | Fecha programada para la siguiente accion. |
| `fechaCreacion` | `datetime (ISO-8601)` | Fecha del registro del seguimiento. |

## 6) Contenedor: `auditoria`
- Clave de particion: `/entidadTipo`
- Uso: trazabilidad y control de todo cambio relevante en el sistema.

| Atributo | Tipo | Para que sirve |
|---|---|---|
| `id` | `string (UUID)` | Identificador unico del evento de auditoria. |
| `fecha` | `datetime (ISO-8601)` | Momento exacto del evento. |
| `actorId` | `string` | Usuario que ejecuto la accion. |
| `actorRol` | `string` | Rol del actor que ejecuto la accion. |
| `accion` | `string` | Operacion ejecutada: `CREAR`, `ACTUALIZAR`, `ELIMINAR`, `CAMBIAR_ESTADO`, etc. |
| `entidadTipo` | `string` | Tipo de entidad afectada: `USUARIO`, `CASO`, `DENUNCIA`, `ASIGNACION`, `SEGUIMIENTO`. |
| `entidadId` | `string` | ID del registro afectado. |
| `resultado` | `string` | Resultado de la accion: `OK` o `ERROR`. |
| `detalle` | `string` | Resumen legible del cambio realizado. |
| `antes` | `object \| null` | Snapshot previo del registro (si aplica). |
| `despues` | `object \| null` | Snapshot posterior del registro (si aplica). |
| `ip` | `string \| null` | IP de origen de la solicitud. |
| `userAgent` | `string \| null` | Cliente/dispositivo desde donde se realizo la accion. |
| `requestId` | `string` | Correlacion con logs del backend para trazabilidad. |

## Convenciones generales
- Fechas: usar formato ISO-8601 UTC (ejemplo: `2026-04-02T15:30:00Z`).
- IDs: usar UUID v4 en formato string.
- Seguridad: no almacenar contrasenas en texto plano, solo hash.
- Integridad: validar `dni` (8 digitos), `correo` y enums en capa backend.
