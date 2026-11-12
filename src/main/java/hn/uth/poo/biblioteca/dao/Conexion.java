/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.dao;

import hn.uth.poo.biblioteca.excepciones.DatosException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String DB_URL = "jdbc:sqlite:biblioteca.db";

    public static Connection abrir() {
        try {
            Connection cn = DriverManager.getConnection(DB_URL);
            try (Statement stmt = cn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return cn;
        } catch (SQLException ex) {
            throw new DatosException("Fallo al abrir conexion con SQLite", ex);
        }
    }

    public static void prepararTablas() {
        String tablaAutores = "CREATE TABLE IF NOT EXISTS autores ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "nacionalidad TEXT)";

        String tablaLibros = "CREATE TABLE IF NOT EXISTS libros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "isbn TEXT NOT NULL UNIQUE, "
                + "titulo TEXT NOT NULL, "
                + "anio INTEGER, "
                + "precio REAL NOT NULL, "
                + "autor_id INTEGER NOT NULL REFERENCES autores(id))";

        try (Connection cn = abrir();
             Statement stmt = cn.createStatement()) {
            stmt.execute(tablaAutores);
            stmt.execute(tablaLibros);
        } catch (SQLException ex) {
            throw new DatosException("No se pudieron crear las tablas iniciales", ex);
        }
    }
}