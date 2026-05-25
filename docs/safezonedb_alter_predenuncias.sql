USE `safezonedb`;

-- =========================================================
-- Tabla: pre_denuncias
-- Flujo: pre-registro de denuncia antes de formalizacion asistida
-- =========================================================
CREATE TABLE IF NOT EXISTS `pre_denuncias` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,

  -- datos de contacto inicial (pueden venir sin cuenta creada)
  `nombres_contacto` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellidos_contacto` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono_contacto` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo_contacto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,

  -- contenido del reporte inicial
  `descripcion_hecho` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_violencia` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_incidente` datetime(3) DEFAULT NULL,
  `distrito` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion_referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `es_anonima` tinyint(1) NOT NULL DEFAULT '1',

  -- ciclo de vida de predenuncia
  `estado` enum('PENDIENTE','EN_CONTACTO','FORMALIZADA','DESCARTADA')
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  `motivo_descarte` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,

  -- relacion con datos oficiales al formalizar
  `victima_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `denuncia_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `caso_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,

  -- control operativo y trazabilidad
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

  CONSTRAINT `fk_predenuncias_asignada_a` FOREIGN KEY (`asignada_a`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_victima` FOREIGN KEY (`victima_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_denuncia` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_caso` FOREIGN KEY (`caso_id`) REFERENCES `casos` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_actualizado_por` FOREIGN KEY (`actualizado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_predenuncias_inactivado_por` FOREIGN KEY (`inactivado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,

  CONSTRAINT `chk_predenuncias_formalizada` CHECK (
    (`estado` <> 'FORMALIZADA')
    OR (`denuncia_id` IS NOT NULL AND `caso_id` IS NOT NULL)
  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

