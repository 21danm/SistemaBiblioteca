-- Activacion de claves foraneas en SQLite
PRAGMA foreign_keys = ON;

-- Tabla de Autores
CREATE TABLE IF NOT EXISTS autores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    nacionalidad TEXT
);

-- Tabla de Libros relacionada con Autores
CREATE TABLE IF NOT EXISTS libros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    isbn TEXT NOT NULL UNIQUE,
    titulo TEXT NOT NULL,
    anio INTEGER,
    precio REAL NOT NULL,
    autor_id INTEGER NOT NULL REFERENCES autores(id)
);
