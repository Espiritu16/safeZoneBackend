-- MySQL dump 10.13  Distrib 9.5.0, for macos26.1 (arm64)
--
-- Host: viaduct.proxy.rlwy.net    Database: safezonedb
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignacion_caso`
--

DROP TABLE IF EXISTS `asignacion_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignacion_caso` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profesional_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_profesional` enum('PSICOLOGO','DEFENSOR') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_asignacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_fin` datetime(3) DEFAULT NULL,
  `asignado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_asignaciones_caso` (`caso_id`),
  KEY `idx_asignaciones_profesional` (`profesional_id`),
  KEY `idx_asignaciones_caso_activo` (`caso_id`,`activo`),
  KEY `fk_asignaciones_asignado_por` (`asignado_por`),
  KEY `idx_asignaciones_actualizado_por` (`actualizado_por`),
  KEY `idx_asignaciones_inactivado_por` (`inactivado_por`),
  KEY `idx_asignaciones_activo` (`activo`),
  CONSTRAINT `fk_asignaciones_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_asignado_por` FOREIGN KEY (`asignado_por`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_profesional` FOREIGN KEY (`profesional_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_asignaciones_fechas` CHECK (((`fecha_fin` is null) or (`fecha_fin` >= `fecha_asignacion`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion_caso`
--

LOCK TABLES `asignacion_caso` WRITE;
/*!40000 ALTER TABLE `asignacion_caso` DISABLE KEYS */;
/*!40000 ALTER TABLE `asignacion_caso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditoria`
--

DROP TABLE IF EXISTS `auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_evento` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `actor_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rol_actor` enum('VICTIMA','RECEPCIONISTA','PSICOLOGO','DEFENSOR','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accion` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `entidad_tipo` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `entidad_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resultado` enum('OK','ERROR') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `detalle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `antes` json DEFAULT NULL,
  `despues` json DEFAULT NULL,
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `agente_usuario` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `codigo_solicitud` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_auditoria_fecha_evento` (`fecha_evento`),
  KEY `idx_auditoria_actor` (`actor_id`),
  KEY `idx_auditoria_entidad` (`entidad_tipo`,`entidad_id`),
  KEY `idx_auditoria_codigo_solicitud` (`codigo_solicitud`),
  KEY `idx_auditoria_activo` (`activo`),
  CONSTRAINT `fk_auditoria_actor` FOREIGN KEY (`actor_id`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
INSERT INTO `auditoria` VALUES ('130cc0ae-0cfd-4d6d-9ee6-9d8156a2d69e','2026-05-28 03:19:50.403','064d7c77-dac6-4a5a-be67-290644ff9636','VICTIMA','LOGIN_CORRECTO','AUTH','064d7c77-dac6-4a5a-be67-290644ff9636','OK','Inicio de sesion correcto',NULL,'{\"correo\": \"evidencia.rf01.1779938386@safezone.test\"}',NULL,NULL,'09eeece7-13ad-43e4-bf88-5d1861a288e5','2026-05-28 03:19:50.403',1),('164c0ad6-713a-4bd2-bfbc-0fa644283842','2026-05-28 03:19:48.851',NULL,NULL,'CREACION','AUTH','registrar','OK','Operacion ejecutada correctamente','{\"uri\": \"/api/auth/registrar\", \"query\": null, \"metodo\": \"AuthController.registrar\", \"metodoHttp\": \"POST\"}','{\"httpStatus\": 201}','127.0.0.1','curl/8.7.1','026e4497-52fe-4894-af1b-6878ee235eb0','2026-05-28 03:19:48.851',1),('360b5a8f-4f3b-4124-8de2-1fd6af3a340e','2026-05-25 16:54:11.883',NULL,NULL,'INICIO_SESION','AUTH','iniciar-sesion','ERROR','Operacion fallida','{\"uri\": \"/api/auth/iniciar-sesion\", \"query\": null, \"metodo\": \"AuthController.iniciarSesion\", \"metodoHttp\": \"POST\"}','{\"error\": \"RecursoNoEncontradoException\", \"mensaje\": \"El correo no existe\"}','0:0:0:0:0:0:0:1','curl/8.7.1','8f6996e9-6819-4461-97a2-fb26e1d23862','2026-05-25 16:54:11.883',1),('3dfa94ff-362a-4295-86cd-cb8e97b52e15','2026-05-25 16:28:25.210',NULL,NULL,'INICIO_SESION','AUTH','iniciar-sesion','ERROR','Operacion fallida','{\"uri\": \"/api/auth/iniciar-sesion\", \"query\": null, \"metodo\": \"AuthController.iniciarSesion\", \"metodoHttp\": \"POST\"}','{\"error\": \"RecursoNoEncontradoException\", \"mensaje\": \"El correo no existe\"}','0:0:0:0:0:0:0:1','curl/8.7.1','6e56e015-803b-4660-b64f-3be923dfcb44','2026-05-25 16:28:25.210',1),('87562e59-bfb7-489c-bfaa-e59febad5089','2026-05-28 03:19:55.102',NULL,NULL,'INICIO_SESION','AUTH','iniciar-sesion','ERROR','Operacion fallida','{\"uri\": \"/api/auth/iniciar-sesion\", \"query\": null, \"metodo\": \"AuthController.iniciarSesion\", \"metodoHttp\": \"POST\"}','{\"error\": \"ExcepcionNegocio\", \"mensaje\": \"Credenciales invalidas\"}','127.0.0.1','curl/8.7.1','d639f0a2-79fe-49da-834d-4c348a88b5e0','2026-05-28 03:19:55.103',1),('8b66902e-aea9-479a-9fb6-e9d6e780d495','2026-05-28 03:19:51.369',NULL,NULL,'INICIO_SESION','AUTH','iniciar-sesion','OK','Operacion ejecutada correctamente','{\"uri\": \"/api/auth/iniciar-sesion\", \"query\": null, \"metodo\": \"AuthController.iniciarSesion\", \"metodoHttp\": \"POST\"}','{\"httpStatus\": 200}','127.0.0.1','curl/8.7.1','17a04c1d-4f38-4563-aea2-d73792bf52f4','2026-05-28 03:19:51.369',1),('b6ae06dd-1304-44d9-aa67-fdf74b1a3be5','2026-05-25 16:28:26.745',NULL,NULL,'RECUPERACION_CONTRASENA','AUTH','recuperar-contrasena','ERROR','Operacion fallida','{\"uri\": \"/api/auth/recuperar-contrasena\", \"query\": null, \"metodo\": \"AuthController.solicitarCodigo\", \"metodoHttp\": \"POST\"}','{\"error\": \"RecursoNoEncontradoException\", \"mensaje\": \"El correo no existe\"}','0:0:0:0:0:0:0:1','curl/8.7.1','07007083-d5bb-46c6-8d35-c338cece29f1','2026-05-25 16:28:26.745',1);
/*!40000 ALTER TABLE `auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caso`
--

DROP TABLE IF EXISTS `caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caso` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` enum('REGISTRADO','EN_EVALUACION','EN_ATENCION','DERIVADO','CERRADO','ARCHIVADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `prioridad` enum('BAJA','MEDIA','ALTA','CRITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resumen` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `distrito` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_cierre` datetime(3) DEFAULT NULL,
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_casos_victima` (`victima_id`),
  KEY `idx_casos_estado_prioridad` (`estado`,`prioridad`),
  KEY `idx_casos_fecha_creacion` (`fecha_creacion`),
  KEY `idx_casos_creado_por` (`creado_por`),
  KEY `idx_casos_actualizado_por` (`actualizado_por`),
  KEY `idx_casos_estado_activo` (`estado`,`activo`),
  KEY `idx_casos_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_casos_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_casos_fechas` CHECK (((`fecha_cierre` is null) or (`fecha_cierre` >= `fecha_creacion`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caso`
--

LOCK TABLES `caso` WRITE;
/*!40000 ALTER TABLE `caso` DISABLE KEYS */;
/*!40000 ALTER TABLE `caso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogo_tipo_seguimiento`
--

DROP TABLE IF EXISTS `catalogo_tipo_seguimiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_tipo_seguimiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cat_tseguimiento_codigo` (`codigo`),
  KEY `idx_cat_tseguimiento_activo` (`activo`),
  KEY `fk_cat_tseguimiento_creado_por` (`creado_por`),
  CONSTRAINT `fk_cat_tseguimiento_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogo_tipo_seguimiento`
--

LOCK TABLES `catalogo_tipo_seguimiento` WRITE;
/*!40000 ALTER TABLE `catalogo_tipo_seguimiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogo_tipo_seguimiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogo_tipo_violencia`
--

DROP TABLE IF EXISTS `catalogo_tipo_violencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_tipo_violencia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cat_tviolencia_codigo` (`codigo`),
  KEY `idx_cat_tviolencia_activo` (`activo`),
  KEY `fk_cat_tviolencia_creado_por` (`creado_por`),
  CONSTRAINT `fk_cat_tviolencia_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogo_tipo_violencia`
--

LOCK TABLES `catalogo_tipo_violencia` WRITE;
/*!40000 ALTER TABLE `catalogo_tipo_violencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogo_tipo_violencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cita`
--

DROP TABLE IF EXISTS `cita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cita` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `especialista_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_cita` enum('PSICOLOGIA','LEGAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` datetime(3) NOT NULL,
  `fecha_fin` datetime(3) NOT NULL,
  `estado` enum('PROGRAMADA','CONFIRMADA','CANCELADA','ATENDIDA','NO_ASISTIO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROGRAMADA',
  `motivo_cancelacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observaciones` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_citas_caso` (`caso_id`),
  KEY `idx_citas_victima` (`victima_id`),
  KEY `idx_citas_especialista` (`especialista_id`),
  KEY `idx_citas_fechas` (`fecha_inicio`,`fecha_fin`),
  KEY `fk_citas_creado_por` (`creado_por`),
  KEY `fk_citas_actualizado_por` (`actualizado_por`),
  KEY `idx_citas_activo` (`activo`),
  KEY `idx_citas_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_citas_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_especialista` FOREIGN KEY (`especialista_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_citas_rango` CHECK ((`fecha_fin` > `fecha_inicio`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cita`
--

LOCK TABLES `cita` WRITE;
/*!40000 ALTER TABLE `cita` DISABLE KEYS */;
/*!40000 ALTER TABLE `cita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_sistema`
--

DROP TABLE IF EXISTS `configuracion_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_sistema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clave` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_valor` enum('STRING','NUMBER','BOOLEAN','JSON') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRING',
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_configuracion_clave` (`clave`),
  KEY `fk_config_creado_por` (`creado_por`),
  KEY `fk_config_actualizado_por` (`actualizado_por`),
  KEY `fk_config_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_config_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_config_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_config_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_sistema`
--

LOCK TABLES `configuracion_sistema` WRITE;
/*!40000 ALTER TABLE `configuracion_sistema` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracion_sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `denuncia`
--

DROP TABLE IF EXISTS `denuncia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncia` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_violencia` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_incidente` datetime(3) NOT NULL,
  `distrito` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion_referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nivel_riesgo` enum('BAJO','MEDIO','ALTO','CRITICO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `es_anonima` tinyint(1) NOT NULL DEFAULT '0',
  `adjuntos` json DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_denuncias_caso` (`caso_id`),
  KEY `idx_denuncias_victima` (`victima_id`),
  KEY `idx_denuncias_nivel_riesgo` (`nivel_riesgo`),
  KEY `idx_denuncias_fecha_incidente` (`fecha_incidente`),
  KEY `idx_denuncias_caso_fecha` (`caso_id`,`fecha_creacion`),
  KEY `idx_denuncias_creado_por` (`creado_por`),
  KEY `idx_denuncias_actualizado_por` (`actualizado_por`),
  KEY `idx_denuncias_activo` (`activo`),
  KEY `idx_denuncias_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_denuncias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denuncia`
--

LOCK TABLES `denuncia` WRITE;
/*!40000 ALTER TABLE `denuncia` DISABLE KEYS */;
/*!40000 ALTER TABLE `denuncia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `denuncia_adjunto`
--

DROP TABLE IF EXISTS `denuncia_adjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncia_adjunto` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_archivo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_mime` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `url_almacenamiento` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hash_sha256` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_dadj_denuncia` (`denuncia_id`),
  KEY `idx_dadj_activo` (`activo`),
  KEY `fk_dadj_creado_por` (`creado_por`),
  CONSTRAINT `fk_dadj_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_dadj_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denuncia_adjunto`
--

LOCK TABLES `denuncia_adjunto` WRITE;
/*!40000 ALTER TABLE `denuncia_adjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `denuncia_adjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacion_riesgo`
--

DROP TABLE IF EXISTS `evaluacion_riesgo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_riesgo` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nivel_riesgo` enum('BAJO','MEDIO','ALTO','CRITICO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `puntaje` int DEFAULT NULL,
  `factores_json` json DEFAULT NULL,
  `observaciones` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `evaluado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_evaluacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_eval_caso_fecha` (`caso_id`,`fecha_evaluacion`),
  KEY `idx_eval_denuncia` (`denuncia_id`),
  KEY `idx_eval_nivel` (`nivel_riesgo`),
  KEY `idx_eval_actor` (`evaluado_por`),
  CONSTRAINT `fk_eval_actor` FOREIGN KEY (`evaluado_por`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_eval_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_eval_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacion_riesgo`
--

LOCK TABLES `evaluacion_riesgo` WRITE;
/*!40000 ALTER TABLE `evaluacion_riesgo` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacion_riesgo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evidencia`
--

DROP TABLE IF EXISTS `evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evidencia` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `seguimiento_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subido_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_archivo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_mime` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `url_almacenamiento` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hash_sha256` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_evidencias_caso` (`caso_id`),
  KEY `idx_evidencias_denuncia` (`denuncia_id`),
  KEY `idx_evidencias_seguimiento` (`seguimiento_id`),
  KEY `fk_evidencias_subido_por` (`subido_por`),
  KEY `fk_evidencias_creado_por` (`creado_por`),
  KEY `fk_evidencias_actualizado_por` (`actualizado_por`),
  KEY `idx_evidencias_activo` (`activo`),
  KEY `idx_evidencias_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_evidencias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_seguimiento` FOREIGN KEY (`seguimiento_id`) REFERENCES `seguimiento_caso` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_subido_por` FOREIGN KEY (`subido_por`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evidencia`
--

LOCK TABLES `evidencia` WRITE;
/*!40000 ALTER TABLE `evidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `evidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_estado_caso`
--

DROP TABLE IF EXISTS `historial_estado_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_estado_caso` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado_anterior` enum('REGISTRADO','EN_EVALUACION','EN_ATENCION','DERIVADO','CERRADO','ARCHIVADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado_nuevo` enum('REGISTRADO','EN_EVALUACION','EN_ATENCION','DERIVADO','CERRADO','ARCHIVADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `prioridad_anterior` enum('BAJA','MEDIA','ALTA','CRITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prioridad_nueva` enum('BAJA','MEDIA','ALTA','CRITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `motivo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cambiado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_actor` enum('VICTIMA','RECEPCIONISTA','PSICOLOGO','DEFENSOR','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_cambio` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_historial_caso_fecha` (`caso_id`,`fecha_cambio`),
  KEY `idx_historial_actor` (`cambiado_por`),
  KEY `idx_historial_activo` (`activo`),
  CONSTRAINT `fk_historial_actor` FOREIGN KEY (`cambiado_por`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_historial_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_estado_caso`
--

LOCK TABLES `historial_estado_caso` WRITE;
/*!40000 ALTER TABLE `historial_estado_caso` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_estado_caso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medida_proteccion`
--

DROP TABLE IF EXISTS `medida_proteccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medida_proteccion` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_medida` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `estado` enum('SOLICITADA','APROBADA','RECHAZADA','VENCIDA','LEVANTADA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SOLICITADA',
  `fecha_solicitud` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_inicio` datetime(3) DEFAULT NULL,
  `fecha_fin` datetime(3) DEFAULT NULL,
  `entidad_emisora` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `documento_referencia` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_medidas_caso` (`caso_id`),
  KEY `idx_medidas_victima` (`victima_id`),
  KEY `idx_medidas_estado` (`estado`),
  KEY `idx_medidas_activo` (`activo`),
  KEY `idx_medidas_fechas` (`fecha_inicio`,`fecha_fin`),
  KEY `fk_medidas_creado_por` (`creado_por`),
  KEY `fk_medidas_actualizado_por` (`actualizado_por`),
  KEY `fk_medidas_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_medidas_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_medidas_fechas` CHECK (((`fecha_fin` is null) or (`fecha_inicio` is null) or (`fecha_fin` >= `fecha_inicio`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medida_proteccion`
--

LOCK TABLES `medida_proteccion` WRITE;
/*!40000 ALTER TABLE `medida_proteccion` DISABLE KEYS */;
/*!40000 ALTER TABLE `medida_proteccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacion`
--

DROP TABLE IF EXISTS `notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacion` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `usuario_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo` enum('RIESGO_CRITICO','SISTEMA','RECORDATORIO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SISTEMA',
  `prioridad` enum('BAJA','MEDIA','ALTA','CRITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MEDIA',
  `titulo` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `mensaje` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `leida` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_lectura` datetime(3) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notificaciones_usuario` (`usuario_id`,`leida`),
  KEY `idx_notificaciones_caso` (`caso_id`),
  KEY `idx_notificaciones_denuncia` (`denuncia_id`),
  KEY `fk_notificaciones_creado_por` (`creado_por`),
  KEY `fk_notificaciones_actualizado_por` (`actualizado_por`),
  KEY `idx_notificaciones_activo` (`activo`),
  KEY `idx_notificaciones_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_notificaciones_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacion`
--

LOCK TABLES `notificacion` WRITE;
/*!40000 ALTER TABLE `notificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pre_denuncia`
--

DROP TABLE IF EXISTS `pre_denuncia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pre_denuncia` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres_contacto` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellidos_contacto` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono_contacto` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo_contacto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `descripcion_hecho` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_violencia` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_incidente` datetime(3) DEFAULT NULL,
  `distrito` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion_referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `es_anonima` tinyint(1) NOT NULL DEFAULT '1',
  `estado` enum('PENDIENTE','EN_CONTACTO','FORMALIZADA','DESCARTADA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  `motivo_descarte` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `asignada_a` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_contacto` datetime(3) DEFAULT NULL,
  `fecha_formalizacion` datetime(3) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_predenuncias_estado` (`estado`),
  KEY `idx_predenuncias_activo` (`activo`),
  KEY `idx_predenuncias_fecha_creacion` (`fecha_creacion`),
  KEY `idx_predenuncias_asignada_a` (`asignada_a`),
  KEY `idx_predenuncias_victima` (`victima_id`),
  KEY `idx_predenuncias_denuncia` (`denuncia_id`),
  KEY `idx_predenuncias_caso` (`caso_id`),
  KEY `fk_predenuncias_creado_por` (`creado_por`),
  KEY `fk_predenuncias_actualizado_por` (`actualizado_por`),
  KEY `fk_predenuncias_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_predenuncias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_asignada_a` FOREIGN KEY (`asignada_a`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pre_denuncia`
--

LOCK TABLES `pre_denuncia` WRITE;
/*!40000 ALTER TABLE `pre_denuncia` DISABLE KEYS */;
/*!40000 ALTER TABLE `pre_denuncia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recuperacion_contrasena_codigo`
--

DROP TABLE IF EXISTS `recuperacion_contrasena_codigo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recuperacion_contrasena_codigo` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `usuario_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_hash` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expira_en` datetime(3) NOT NULL,
  `usado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_uso` datetime(3) DEFAULT NULL,
  `intentos` int NOT NULL DEFAULT '0',
  `max_intentos` int NOT NULL DEFAULT '5',
  `solicitado_desde_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `agente_usuario` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_recuperacion_codigo_hash` (`codigo_hash`),
  KEY `idx_recuperacion_usuario` (`usuario_id`),
  KEY `idx_recuperacion_expira_en` (`expira_en`),
  KEY `idx_recuperacion_usado_activo` (`usado`,`activo`),
  CONSTRAINT `fk_recuperacion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recuperacion_contrasena_codigo`
--

LOCK TABLES `recuperacion_contrasena_codigo` WRITE;
/*!40000 ALTER TABLE `recuperacion_contrasena_codigo` DISABLE KEYS */;
INSERT INTO `recuperacion_contrasena_codigo` VALUES ('f1343612-e4a4-413c-aa45-54286c17c2ea','901d5008-db96-4602-9cd4-ec4112977970','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','2026-05-25 12:41:46.025',1,'2026-05-25 07:26:50.312',1,5,NULL,NULL,'2026-05-25 07:25:16.583',0);
/*!40000 ALTER TABLE `recuperacion_contrasena_codigo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `usuario_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_hash` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expira_en` datetime(3) NOT NULL,
  `revocado` tinyint(1) NOT NULL DEFAULT '0',
  `revocado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_revocacion` datetime(3) DEFAULT NULL,
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_refresh_token_hash` (`token_hash`),
  KEY `idx_refresh_usuario` (`usuario_id`),
  KEY `idx_refresh_expira` (`expira_en`),
  KEY `fk_refresh_revocado_por` (`revocado_por`),
  KEY `idx_refresh_activo` (`activo`),
  CONSTRAINT `fk_refresh_revocado_por` FOREIGN KEY (`revocado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_refresh_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
INSERT INTO `refresh_token` VALUES ('3d17e472-7386-4b97-9d7d-2480add79870','a0993039-8b1c-416f-9cc8-0b31028765a9','31f6155cdd85d57f7b9b1a32eb79e831ebf910b6ced8ecefcf8f141b68a41795','2026-06-01 16:17:46.407',1,'a0993039-8b1c-416f-9cc8-0b31028765a9','2026-05-25 16:17:48.059','2026-05-25 16:17:46.407',0),('64b2c8ef-1600-4a82-b4a2-73f83b823821','a0993039-8b1c-416f-9cc8-0b31028765a9','28ac0c04713d773195c1997478d3f80655b5693f07f56cf74ba4570e09fa3908','2026-06-01 16:17:43.672',1,'a0993039-8b1c-416f-9cc8-0b31028765a9','2026-05-25 16:17:45.951','2026-05-25 16:17:43.672',0),('7190b881-a5f1-4338-99f1-66d36d2f07a0','064d7c77-dac6-4a5a-be67-290644ff9636','0ff0ab9165a2d6d2a1a8a4c5af49f5903892cf5441c61b8a4ec65e2c5b6b66c8','2026-06-04 03:19:50.089',0,NULL,NULL,'2026-05-28 03:19:50.245',1);
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seguimiento_caso`
--

DROP TABLE IF EXISTS `seguimiento_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seguimiento_caso` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `autor_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_autor` enum('PSICOLOGO','DEFENSOR','SOPORTE','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_seguimiento` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `contenido` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `proxima_accion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_proxima_accion` datetime(3) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_seguimientos_caso` (`caso_id`),
  KEY `idx_seguimientos_autor` (`autor_id`),
  KEY `idx_seguimientos_caso_fecha` (`caso_id`,`fecha_creacion`),
  KEY `idx_seguimientos_fecha_proxima` (`fecha_proxima_accion`),
  KEY `idx_seguimientos_creado_por` (`creado_por`),
  KEY `idx_seguimientos_actualizado_por` (`actualizado_por`),
  KEY `idx_seguimientos_activo` (`activo`),
  KEY `idx_seguimientos_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_seguimientos_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_autor` FOREIGN KEY (`autor_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_caso` FOREIGN KEY (`caso_id`) REFERENCES `caso` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seguimiento_caso`
--

LOCK TABLES `seguimiento_caso` WRITE;
/*!40000 ALTER TABLE `seguimiento_caso` DISABLE KEYS */;
/*!40000 ALTER TABLE `seguimiento_caso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` enum('VICTIMA','RECEPCIONISTA','PSICOLOGO','DEFENSOR','SOPORTE','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dni` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `distrito` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_usuarios_correo` (`correo`),
  UNIQUE KEY `uq_usuarios_dni` (`dni`),
  KEY `idx_usuarios_rol` (`rol`),
  KEY `idx_usuarios_distrito` (`distrito`),
  KEY `idx_usuarios_creado_por` (`creado_por`),
  KEY `idx_usuarios_actualizado_por` (`actualizado_por`),
  KEY `idx_usuarios_inactivado_por` (`inactivado_por`),
  KEY `idx_usuarios_activo` (`activo`),
  CONSTRAINT `fk_usuarios_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_usuarios_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_usuarios_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('064d7c77-dac6-4a5a-be67-290644ff9636','VICTIMA','evidencia.rf01.1779938386@safezone.test','$2a$10$jz/Cj755Fq3mpOMsb7ys.u9M1qxH8dnQ/1e0SYvu3vBR79NE8bSLK','Evidencia RF01','N/A','69825319',NULL,NULL,1,'2026-05-28 03:19:48.028','2026-05-28 03:19:48.028',NULL,NULL,NULL,NULL),('901d5008-db96-4602-9cd4-ec4112977970','VICTIMA','qa.auth.1779711910@mail.com','$2a$10$trBhcCGx5Bnk6KtBNre0kO93.8MsxkO7cno4KmOb9KMFKGB6YO1Oq','QA Usuario','N/A','26007861',NULL,NULL,1,'2026-05-25 12:25:11.018','2026-05-25 12:26:50.312',NULL,NULL,NULL,NULL),('a0993039-8b1c-416f-9cc8-0b31028765a9','VICTIMA','qa.seg.1779725861@mail.com','$2a$10$EFWooTimG6S/oncrQLcWCe4Dm5K7u9me.55orhJREtVx7lqCdmz2q','Admin QA','N/A','50090630',NULL,NULL,1,'2026-05-25 16:17:42.518','2026-05-25 16:17:42.518',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `victimas_alias`
--

DROP TABLE IF EXISTS `victimas_alias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `victimas_alias` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `alias_codigo` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_asignacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_fin` datetime(3) DEFAULT NULL,
  `creado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_victimas_alias_codigo` (`alias_codigo`),
  KEY `idx_victimas_alias_victima` (`victima_id`,`activo`),
  KEY `fk_victimas_alias_creado_por` (`creado_por`),
  KEY `fk_victimas_alias_actualizado_por` (`actualizado_por`),
  KEY `fk_victimas_alias_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_victimas_alias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `victimas_alias`
--

LOCK TABLES `victimas_alias` WRITE;
/*!40000 ALTER TABLE `victimas_alias` DISABLE KEYS */;
/*!40000 ALTER TABLE `victimas_alias` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-28 14:14:55
