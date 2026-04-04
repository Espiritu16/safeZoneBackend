# BD - Esquema Relacional (MySQL)

Base de datos: `safezonedb`

## 1) Tabla: `usuarios`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico del usuario (UUID). Clave primaria. |
| rol | Rol del usuario: `VICTIMA`, `PSICOLOGO`, `DEFENSOR`, `ADMIN`. |
| correo | Correo para inicio de sesion y contacto (unico). |
| contrasena_hash | Contrasena cifrada (hash), nunca en texto plano. |
| nombres | Nombres del usuario. |
| apellidos | Apellidos del usuario. |
| dni | Documento de identidad (unico). |
| telefono | Telefono de contacto. |
| distrito | Distrito de residencia o referencia. |
| activo | Indica si la cuenta esta habilitada (`1`/`0`). |
| eliminado | Marcador de borrado logico (`1`/`0`). |
| fecha_creacion | Fecha de registro del usuario. |
| fecha_actualizacion | Fecha de ultima actualizacion del usuario. |

## 2) Tabla: `casos`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico del caso (UUID). Clave primaria. |
| victima_id | ID de la victima propietaria del caso (FK a `usuarios.id`). |
| estado | Estado del caso: `ABIERTO`, `EN_SEGUIMIENTO`, `CERRADO`. |
| prioridad | Prioridad operativa: `BAJA`, `MEDIA`, `ALTA`, `CRITICA`. |
| resumen | Descripcion corta del caso. |
| distrito | Distrito asociado al caso. |
| eliminado | Marcador de borrado logico (`1`/`0`). |
| fecha_creacion | Fecha de apertura del caso. |
| fecha_actualizacion | Fecha de ultima actualizacion del caso. |
| fecha_cierre | Fecha de cierre del caso (si aplica). |

Regla: `fecha_cierre` debe ser mayor o igual que `fecha_creacion`.

## 3) Tabla: `denuncias`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico de la denuncia (UUID). Clave primaria. |
| caso_id | ID del caso al que pertenece (FK a `casos.id`). |
| victima_id | ID de la victima que reporta (FK a `usuarios.id`). |
| descripcion | Detalle principal de la denuncia. |
| tipo_violencia | Tipo de violencia reportada. |
| fecha_incidente | Fecha en la que ocurrio el incidente. |
| distrito | Distrito donde ocurrio el hecho. |
| direccion_referencia | Referencia adicional de ubicacion (opcional). |
| nivel_riesgo | Nivel de riesgo: `BAJO`, `MEDIO`, `ALTO`, `CRITICO`. |
| es_anonima | Indica si la denuncia se maneja como anonima (`1`/`0`). |
| adjuntos | Evidencias en formato JSON (imagenes, documentos o enlaces). |
| eliminado | Marcador de borrado logico (`1`/`0`). |
| fecha_creacion | Fecha de registro de la denuncia. |
| fecha_actualizacion | Fecha de ultima actualizacion de la denuncia. |

## 4) Tabla: `asignaciones_caso`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico de la asignacion (UUID). Clave primaria. |
| caso_id | ID del caso asociado (FK a `casos.id`). |
| profesional_id | ID del profesional asignado (FK a `usuarios.id`). |
| rol_profesional | Rol asignado: `PSICOLOGO` o `DEFENSOR`. |
| activo | Indica si la asignacion esta vigente (`1`/`0`). |
| eliminado | Marcador de borrado logico (`1`/`0`). |
| fecha_asignacion | Fecha en que se realizo la asignacion. |
| fecha_actualizacion | Fecha de ultima actualizacion de la asignacion. |
| fecha_fin | Fecha de finalizacion de la asignacion (si aplica). |
| asignado_por | ID del administrador que hizo la asignacion (FK a `usuarios.id`). |

Regla: `fecha_fin` debe ser mayor o igual que `fecha_asignacion`.

## 5) Tabla: `seguimientos_caso`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico del seguimiento (UUID). Clave primaria. |
| caso_id | ID del caso asociado (FK a `casos.id`). |
| autor_id | ID del usuario que registra el seguimiento (FK a `usuarios.id`). |
| rol_autor | Rol del autor: `PSICOLOGO`, `DEFENSOR` o `ADMIN`. |
| tipo_seguimiento | Tipo de actividad de seguimiento. |
| contenido | Detalle de la nota de seguimiento. |
| proxima_accion | Siguiente accion planificada para el caso. |
| fecha_proxima_accion | Fecha estimada de la proxima accion. |
| eliminado | Marcador de borrado logico (`1`/`0`). |
| fecha_creacion | Fecha en que se registro el seguimiento. |
| fecha_actualizacion | Fecha de ultima actualizacion del seguimiento. |

## 6) Tabla: `auditoria`

| ATRIBUTO | DESCRIPCION |
|---|---|
| id | Identificador unico del evento de auditoria (UUID). Clave primaria. |
| fecha_evento | Fecha y hora del evento auditado. |
| actor_id | ID del usuario que ejecuto la accion (FK a `usuarios.id`, opcional). |
| rol_actor | Rol del actor: `VICTIMA`, `PSICOLOGO`, `DEFENSOR`, `ADMIN`. |
| accion | Accion realizada: `CREAR`, `ACTUALIZAR`, `ELIMINAR`, etc. |
| entidad_tipo | Tipo de entidad afectada. |
| entidad_id | ID del registro afectado. |
| resultado | Resultado de la accion: `OK` o `ERROR`. |
| detalle | Descripcion resumida del cambio realizado. |
| antes | Estado previo en formato JSON (si aplica). |
| despues | Estado posterior en formato JSON (si aplica). |
| ip | Direccion IP de origen (si aplica). |
| agente_usuario | Cliente o navegador desde donde se ejecuto la accion. |
| codigo_solicitud | ID de correlacion para seguimiento en logs. |
| fecha_creacion | Fecha de registro en auditoria. |

## Indices y restricciones principales

- Unicos: `usuarios.correo`, `usuarios.dni`.
- Foraneas en todas las relaciones entre `usuarios`, `casos`, `denuncias`, `asignaciones_caso`, `seguimientos_caso` y `auditoria`.
- Indices operativos por estado, prioridad, fechas y relaciones de caso.
- Restricciones de fechas para consistencia en `casos` y `asignaciones_caso`.
