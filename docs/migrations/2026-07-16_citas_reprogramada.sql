ALTER TABLE cita
  ADD COLUMN reprogramada TINYINT(1) NOT NULL DEFAULT 0 AFTER observaciones;
