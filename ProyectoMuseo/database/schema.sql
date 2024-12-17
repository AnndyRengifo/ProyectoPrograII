-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS Usuarios (
    id CHAR(13) PRIMARY KEY,   -- Usamos CHAR(13) para garantizar 13 caracteres fijos
    nombre TEXT NOT NULL,
    rol TEXT NOT NULL, -- Ejemplo: "admin" o "visitante"
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

-- Tabla Pinturas actualizada
CREATE TABLE IF NOT EXISTS Pinturas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    anio INTEGER NOT NULL,
    descripcion TEXT,
    codigo_barras TEXT NOT NULL,
    ubicacion TEXT,
    imagen TEXT
);


-- Tabla Visitantes
CREATE TABLE IF NOT EXISTS Visitantes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    fecha_visita TEXT NOT NULL
);
