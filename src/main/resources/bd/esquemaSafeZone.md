CREATE DATABASE  IF NOT EXISTS `safezonedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `safezonedb`;
-- MySQL dump 10.13  Distrib 8.0.43, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: safezonedb
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2003698b-cf30-11f0-ad12-c817f5bb185b:1-4369,
fb4e7bea-d857-11f0-88d8-416539ee0034:1-11579';

--
-- Table structure for table `asignaciones_caso`
--

DROP TABLE IF EXISTS `asignaciones_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones_caso` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `profesional_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_profesional` enum('PSICOLOGO','DEFENSOR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `eliminado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_asignacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_fin` datetime(3) DEFAULT NULL,
  `asignado_por` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_asignaciones_caso` (`caso_id`),
  KEY `idx_asignaciones_profesional` (`profesional_id`),
  KEY `idx_asignaciones_activo_eliminado` (`activo`,`eliminado`),
  KEY `idx_asignaciones_caso_activo` (`caso_id`,`activo`),
  KEY `fk_asignaciones_asignado_por` (`asignado_por`),
  CONSTRAINT `fk_asignaciones_asignado_por` FOREIGN KEY (`asignado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
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
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_evento` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `actor_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rol_actor` enum('VICTIMA','PSICOLOGO','DEFENSOR','ADMIN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accion` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entidad_tipo` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entidad_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resultado` enum('OK','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `detalle` text COLLATE utf8mb4_unicode_ci,
  `antes` json DEFAULT NULL,
  `despues` json DEFAULT NULL,
  `ip` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `agente_usuario` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `codigo_solicitud` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `idx_auditoria_fecha_evento` (`fecha_evento`),
  KEY `idx_auditoria_actor` (`actor_id`),
  KEY `idx_auditoria_entidad` (`entidad_tipo`,`entidad_id`),
  KEY `idx_auditoria_codigo_solicitud` (`codigo_solicitud`),
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
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` enum('ABIERTO','EN_SEGUIMIENTO','CERRADO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `prioridad` enum('BAJA','MEDIA','ALTA','CRITICA') COLLATE utf8mb4_unicode_ci NOT NULL,
  `resumen` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `distrito` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `eliminado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `fecha_cierre` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_casos_victima` (`victima_id`),
  KEY `idx_casos_estado_prioridad` (`estado`,`prioridad`),
  KEY `idx_casos_fecha_creacion` (`fecha_creacion`),
  KEY `idx_casos_estado_eliminado` (`estado`,`eliminado`),
  CONSTRAINT `fk_casos_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_casos_fechas` CHECK (((`fecha_cierre` is null) or (`fecha_cierre` >= `fecha_creacion`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `denuncias`
--

DROP TABLE IF EXISTS `denuncias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncias` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `victima_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_violencia` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_incidente` datetime(3) NOT NULL,
  `distrito` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion_referencia` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nivel_riesgo` enum('BAJO','MEDIO','ALTO','CRITICO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `es_anonima` tinyint(1) NOT NULL DEFAULT '0',
  `adjuntos` json DEFAULT NULL,
  `eliminado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `idx_denuncias_caso` (`caso_id`),
  KEY `idx_denuncias_victima` (`victima_id`),
  KEY `idx_denuncias_nivel_riesgo` (`nivel_riesgo`),
  KEY `idx_denuncias_fecha_incidente` (`fecha_incidente`),
  KEY `idx_denuncias_caso_fecha` (`caso_id`,`fecha_creacion`),
  CONSTRAINT `fk_denuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_denuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seguimientos_caso`
--

DROP TABLE IF EXISTS `seguimientos_caso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seguimientos_caso` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caso_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `autor_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol_autor` enum('PSICOLOGO','DEFENSOR','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_seguimiento` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contenido` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `proxima_accion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_proxima_accion` datetime(3) DEFAULT NULL,
  `eliminado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `idx_seguimientos_caso` (`caso_id`),
  KEY `idx_seguimientos_autor` (`autor_id`),
  KEY `idx_seguimientos_caso_fecha` (`caso_id`,`fecha_creacion`),
  KEY `idx_seguimientos_fecha_proxima` (`fecha_proxima_accion`),
  CONSTRAINT `fk_seguimientos_autor` FOREIGN KEY (`autor_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` enum('VICTIMA','PSICOLOGO','DEFENSOR','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dni` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `distrito` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `eliminado` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_creacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `fecha_actualizacion` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_usuarios_correo` (`correo`),
  UNIQUE KEY `uq_usuarios_dni` (`dni`),
  KEY `idx_usuarios_rol` (`rol`),
  KEY `idx_usuarios_distrito` (`distrito`),
  KEY `idx_usuarios_activo_eliminado` (`activo`,`eliminado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'safezonedb'
--

--
-- Dumping routines for database 'safezonedb'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-03 22:11:30
