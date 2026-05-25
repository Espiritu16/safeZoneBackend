USE `safezonedb`;

CREATE TABLE IF NOT EXISTS `recuperacion_contrasena_codigos` (
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
  KEY `idx_recuperacion_usado_activo` (`usado`, `activo`),

  CONSTRAINT `fk_recuperacion_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

