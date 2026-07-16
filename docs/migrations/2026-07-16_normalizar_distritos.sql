UPDATE caso
SET distrito = CASE
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('lima', 'lima cercado', 'cercado de lima') THEN 'Lima'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('los olivos', 'losolivos') THEN 'Los Olivos'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('villa el salvador', 'ves') THEN 'Villa El Salvador'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de lurigancho', 'sjl') THEN 'San Juan de Lurigancho'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de miraflores', 'sjm') THEN 'San Juan de Miraflores'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa anita' THEN 'Santa Anita'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa fe' THEN 'Santa Fe'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima norte' THEN 'Lima Norte'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima sur' THEN 'Lima Sur'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima este' THEN 'Lima Este'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima centro' THEN 'Lima Centro'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'comas' THEN 'Comas'
    ELSE TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))
END
WHERE distrito IS NOT NULL AND TRIM(distrito) <> '';

UPDATE denuncia
SET distrito = CASE
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('lima', 'lima cercado', 'cercado de lima') THEN 'Lima'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('los olivos', 'losolivos') THEN 'Los Olivos'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('villa el salvador', 'ves') THEN 'Villa El Salvador'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de lurigancho', 'sjl') THEN 'San Juan de Lurigancho'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de miraflores', 'sjm') THEN 'San Juan de Miraflores'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa anita' THEN 'Santa Anita'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa fe' THEN 'Santa Fe'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima norte' THEN 'Lima Norte'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima sur' THEN 'Lima Sur'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima este' THEN 'Lima Este'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima centro' THEN 'Lima Centro'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'comas' THEN 'Comas'
    ELSE TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))
END
WHERE distrito IS NOT NULL AND TRIM(distrito) <> '';

UPDATE pre_denuncia
SET distrito = CASE
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('lima', 'lima cercado', 'cercado de lima') THEN 'Lima'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('los olivos', 'losolivos') THEN 'Los Olivos'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('villa el salvador', 'ves') THEN 'Villa El Salvador'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de lurigancho', 'sjl') THEN 'San Juan de Lurigancho'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de miraflores', 'sjm') THEN 'San Juan de Miraflores'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa anita' THEN 'Santa Anita'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa fe' THEN 'Santa Fe'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima norte' THEN 'Lima Norte'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima sur' THEN 'Lima Sur'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima este' THEN 'Lima Este'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima centro' THEN 'Lima Centro'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'comas' THEN 'Comas'
    ELSE TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))
END
WHERE distrito IS NOT NULL AND TRIM(distrito) <> '';

UPDATE usuario
SET distrito = CASE
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('lima', 'lima cercado', 'cercado de lima') THEN 'Lima'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('los olivos', 'losolivos') THEN 'Los Olivos'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('villa el salvador', 'ves') THEN 'Villa El Salvador'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de lurigancho', 'sjl') THEN 'San Juan de Lurigancho'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) IN ('san juan de miraflores', 'sjm') THEN 'San Juan de Miraflores'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa anita' THEN 'Santa Anita'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'santa fe' THEN 'Santa Fe'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima norte' THEN 'Lima Norte'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima sur' THEN 'Lima Sur'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima este' THEN 'Lima Este'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'lima centro' THEN 'Lima Centro'
    WHEN LOWER(TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))) = 'comas' THEN 'Comas'
    ELSE TRIM(REPLACE(REPLACE(distrito, '  ', ' '), '  ', ' '))
END
WHERE distrito IS NOT NULL AND TRIM(distrito) <> '';
