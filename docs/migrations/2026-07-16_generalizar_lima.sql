UPDATE caso
SET distrito = 'Lima'
WHERE distrito IS NOT NULL
  AND LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN (
      'lima', 'lima cercado', 'cercado de lima', 'lima norte', 'lima sur', 'lima este', 'lima centro'
  );

UPDATE denuncia
SET distrito = 'Lima'
WHERE distrito IS NOT NULL
  AND LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN (
      'lima', 'lima cercado', 'cercado de lima', 'lima norte', 'lima sur', 'lima este', 'lima centro'
  );

UPDATE pre_denuncia
SET distrito = 'Lima'
WHERE distrito IS NOT NULL
  AND LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN (
      'lima', 'lima cercado', 'cercado de lima', 'lima norte', 'lima sur', 'lima este', 'lima centro'
  );

UPDATE usuario
SET distrito = 'Lima'
WHERE distrito IS NOT NULL
  AND LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN (
      'lima', 'lima cercado', 'cercado de lima', 'lima norte', 'lima sur', 'lima este', 'lima centro'
  );
