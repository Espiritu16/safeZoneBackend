-- Permite evidencias cargadas de forma independiente y vinculadas posteriormente.
-- Mantiene CHAR(36) para coincidir con las PK referenciadas y evitar incompatibilidades de FK.

ALTER TABLE evidencia
  MODIFY COLUMN caso_id CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;

ALTER TABLE evidencia
  MODIFY COLUMN denuncia_id CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;

ALTER TABLE evidencia
  MODIFY COLUMN fecha_actualizacion DATETIME(3) NULL;
