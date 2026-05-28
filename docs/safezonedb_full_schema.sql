-- MySQL dump 10.13  Distrib 9.5.0, for macos26.1 (arm64)
--
-- Host: localhost    Database: safezonedb
-- ------------------------------------------------------
-- Server version	9.5.0

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
-- Table structure for table `asignaciones_caso`
--

DROP TABLE IF EXISTS `asignaciones_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones_caso` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profesional_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_profesional` enum('PSICOLOGO','DEFENSOR') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_asignacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_fin` datetime(3) DEFAULT NULL,
  `asignado_por` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_asignaciones_caso` (`caso_id`),
  KEY `idx_asignaciones_profesional` (`profesional_id`),
  KEY `idx_asignaciones_caso_activo` (`caso_id`,`activo`),
  KEY `fk_asignaciones_asignado_por` (`asignado_por`),
  KEY `idx_asignaciones_actualizado_por` (`actualizado_por`),
  KEY `idx_asignaciones_inactivado_por` (`inactivado_por`),
  KEY `idx_asignaciones_activo` (`activo`),
  CONSTRAINT `fk_asignaciones_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_asignado_por` FOREIGN KEY (`asignado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_profesional` FOREIGN KEY (`profesional_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_asignaciones_fechas` CHECK (((`fecha_fin` is null) or (`fecha_fin` >= `fecha_asignacion`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `rol_actor` enum('VICTIMA','RECEPCIONISTA','PSICOLOGO','DEFENSOR','SOPORTE','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_auditoria_actor` FOREIGN KEY (`actor_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `casos`
--

DROP TABLE IF EXISTS `casos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `casos` (
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
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_casos_victima` (`victima_id`),
  KEY `idx_casos_estado_prioridad` (`estado`,`prioridad`),
  KEY `idx_casos_fecha_creacion` (`fecha_creacion`),
  KEY `idx_casos_creado_por` (`creado_por`),
  KEY `idx_casos_actualizado_por` (`actualizado_por`),
  KEY `idx_casos_estado_activo` (`estado`,`activo`),
  KEY `idx_casos_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_casos_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_casos_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_casos_fechas` CHECK (((`fecha_cierre` is null) or (`fecha_cierre` >= `fecha_creacion`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `catalogo_tipos_seguimiento`
--

DROP TABLE IF EXISTS `catalogo_tipos_seguimiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_tipos_seguimiento` (
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
  CONSTRAINT `fk_cat_tseguimiento_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `catalogo_tipos_violencia`
--

DROP TABLE IF EXISTS `catalogo_tipos_violencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_tipos_violencia` (
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
  CONSTRAINT `fk_cat_tviolencia_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `especialista_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_cita` enum('PSICOLOGIA','LEGAL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` datetime(3) NOT NULL,
  `fecha_fin` datetime(3) NOT NULL,
  `estado` enum('PROGRAMADA','CONFIRMADA','CANCELADA','ATENDIDA','NO_ASISTIO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROGRAMADA',
  `motivo_cancelacion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_citas_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_especialista` FOREIGN KEY (`especialista_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_citas_rango` CHECK ((`fecha_fin` > `fecha_inicio`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `configuracion_sistema`
--

DROP TABLE IF EXISTS `configuracion_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_sistema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clave` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_valor` enum('STRING','NUMBER','BOOLEAN','JSON') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRING',
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_configuracion_clave` (`clave`),
  KEY `fk_config_creado_por` (`creado_por`),
  KEY `fk_config_actualizado_por` (`actualizado_por`),
  KEY `fk_config_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_config_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_config_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_config_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `denuncia_adjuntos`
--

DROP TABLE IF EXISTS `denuncia_adjuntos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncia_adjuntos` (
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
  CONSTRAINT `fk_dadj_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_dadj_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `denuncias`
--

DROP TABLE IF EXISTS `denuncias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncias` (
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
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_denuncias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluaciones_riesgo`
--

DROP TABLE IF EXISTS `evaluaciones_riesgo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluaciones_riesgo` (
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
  CONSTRAINT `fk_eval_actor` FOREIGN KEY (`evaluado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_eval_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_eval_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evidencias`
--

DROP TABLE IF EXISTS `evidencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evidencias` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `denuncia_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `seguimiento_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subido_por` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_archivo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_mime` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `url_almacenamiento` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hash_sha256` char(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_evidencias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_seguimiento` FOREIGN KEY (`seguimiento_id`) REFERENCES `seguimientos_caso` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_evidencias_subido_por` FOREIGN KEY (`subido_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `rol_actor` enum('VICTIMA','RECEPCIONISTA','PSICOLOGO','DEFENSOR','SOPORTE','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_cambio` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_historial_caso_fecha` (`caso_id`,`fecha_cambio`),
  KEY `idx_historial_actor` (`cambiado_por`),
  KEY `idx_historial_activo` (`activo`),
  CONSTRAINT `fk_historial_actor` FOREIGN KEY (`cambiado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_historial_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `medidas_proteccion`
--

DROP TABLE IF EXISTS `medidas_proteccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medidas_proteccion` (
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
  CONSTRAINT `fk_medidas_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_medidas_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_medidas_fechas` CHECK (((`fecha_fin` is null) or (`fecha_inicio` is null) or (`fecha_fin` >= `fecha_inicio`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `usuario_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `denuncia_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo` enum('RIESGO_CRITICO','SISTEMA','RECORDATORIO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SISTEMA',
  `prioridad` enum('BAJA','MEDIA','ALTA','CRITICA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MEDIA',
  `titulo` varchar(160) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mensaje` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `leida` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_lectura` datetime(3) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notificaciones_usuario` (`usuario_id`,`leida`),
  KEY `idx_notificaciones_caso` (`caso_id`),
  KEY `idx_notificaciones_denuncia` (`denuncia_id`),
  KEY `fk_notificaciones_creado_por` (`creado_por`),
  KEY `fk_notificaciones_actualizado_por` (`actualizado_por`),
  KEY `idx_notificaciones_activo` (`activo`),
  KEY `idx_notificaciones_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_notificaciones_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_notificaciones_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pre_denuncias`
--

DROP TABLE IF EXISTS `pre_denuncias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pre_denuncias` (
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
  CONSTRAINT `fk_predenuncias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_asignada_a` FOREIGN KEY (`asignada_a`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recuperacion_contrasena_codigos`
--

DROP TABLE IF EXISTS `recuperacion_contrasena_codigos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recuperacion_contrasena_codigos` (
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
  CONSTRAINT `fk_recuperacion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `usuario_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_hash` char(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expira_en` datetime(3) NOT NULL,
  `revocado` tinyint(1) NOT NULL DEFAULT '0',
  `revocado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_revocacion` datetime(3) DEFAULT NULL,
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_refresh_token_hash` (`token_hash`),
  KEY `idx_refresh_usuario` (`usuario_id`),
  KEY `idx_refresh_expira` (`expira_en`),
  KEY `fk_refresh_revocado_por` (`revocado_por`),
  KEY `idx_refresh_activo` (`activo`),
  CONSTRAINT `fk_refresh_revocado_por` FOREIGN KEY (`revocado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_refresh_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seguimientos_caso`
--

DROP TABLE IF EXISTS `seguimientos_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seguimientos_caso` (
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
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_seguimientos_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_autor` FOREIGN KEY (`autor_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
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
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `fk_usuarios_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_usuarios_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_usuarios_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `victimas_alias`
--

DROP TABLE IF EXISTS `victimas_alias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `victimas_alias` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alias_codigo` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_asignacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_fin` datetime(3) DEFAULT NULL,
  `creado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualizado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inactivado_por` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_inactivacion` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_victimas_alias_codigo` (`alias_codigo`),
  KEY `idx_victimas_alias_victima` (`victima_id`,`activo`),
  KEY `fk_victimas_alias_creado_por` (`creado_por`),
  KEY `fk_victimas_alias_actualizado_por` (`actualizado_por`),
  KEY `fk_victimas_alias_inactivado_por` (`inactivado_por`),
  CONSTRAINT `fk_victimas_alias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_victimas_alias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'safezonedb'
--

--
-- Dumping routines for database 'safezonedb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-25  6:46:34
