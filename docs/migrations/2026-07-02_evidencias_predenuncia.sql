-- Vincula evidencias cargadas durante una predenuncia y permite cargas publicas asociadas a ese registro.
-- Ejecutar despues de que existan las tablas evidencia y pre_denuncia.

ALTER TABLE evidencia
  ADD COLUMN predenuncia_id CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER denuncia_id;

ALTER TABLE evidencia
  MODIFY COLUMN subido_por CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;

ALTER TABLE evidencia
  ADD INDEX idx_evidencias_predenuncia (predenuncia_id);

ALTER TABLE evidencia
  ADD CONSTRAINT fk_evidencias_predenuncia
  FOREIGN KEY (predenuncia_id) REFERENCES pre_denuncia(id)
  ON DELETE SET NULL ON UPDATE CASCADE;
