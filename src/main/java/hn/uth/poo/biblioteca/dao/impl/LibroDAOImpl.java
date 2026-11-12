/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.dao.impl;

import hn.uth.poo.biblioteca.dao.Conexion;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    public LibroDAOImpl() {
        Conexion.prepararTablas();
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Libro lb = new Libro();
        lb.setId(rs.getInt("id"));
        lb.setIsbn(rs.getString("isbn"));
        lb.setTitulo(rs.getString("titulo"));
        lb.setAnio(rs.getInt("anio"));
        lb.setPrecio(rs.getDouble("precio"));
        lb.setAutorId(rs.getInt("autor_id"));
        return lb;
    }

    @Override
    public void insertar(Libro libro) {
        String query = "INSERT INTO libros(isbn, titulo, anio, precio, autor_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, libro.getIsbn());
            pst.setString(2, libro.getTitulo());
            pst.setInt(3, libro.getAnio());
            pst.setDouble(4, libro.getPrecio());
            pst.setInt(5, libro.getAutorId());
            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    libro.setId(keys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new DatosException("No se pudo insertar el libro", ex);
        }
    }

    @Override
    public Libro buscarPorId(int id) {
        String query = "SELECT id, isbn, titulo, anio, precio, autor_id FROM libros WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new DatosException("Error al consultar libro por ID", ex);
        }
    }

    @Override
    public List<Libro> listar() {
        String query = "SELECT id, isbn, titulo, anio, precio, autor_id FROM libros";
        List<Libro> registros = new ArrayList<>();
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                registros.add(mapear(rs));
            }
            return registros;
        } catch (SQLException ex) {
            throw new DatosException("Error al obtener todos los libros", ex);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        String query = "UPDATE libros SET isbn = ?, titulo = ?, anio = ?, precio = ?, autor_id = ? WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, libro.getIsbn());
            pst.setString(2, libro.getTitulo());
            pst.setInt(3, libro.getAnio());
            pst.setDouble(4, libro.getPrecio());
            pst.setInt(5, libro.getAutorId());
            pst.setInt(6, libro.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DatosException("Error al actualizar el libro", ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String query = "DELETE FROM libros WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DatosException("Error al borrar el libro", ex);
        }
    }

    @Override
    public List<Libro> listarPorAutor(int autorId) {
        String query = "SELECT id, isbn, titulo, anio, precio, autor_id FROM libros WHERE autor_id = ?";
        List<Libro> registros = new ArrayList<>();
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setInt(1, autorId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapear(rs));
                }
            }
            return registros;
        } catch (SQLException ex) {
            throw new DatosException("Error al filtrar libros del autor", ex);
        }
    }
}
