/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.dao.impl;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.Conexion;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AutorDAOImpl implements AutorDAO {

    public AutorDAOImpl() {
        Conexion.prepararTablas();
    }

    private Autor mapear(ResultSet rs) throws SQLException {
        Autor a = new Autor();
        a.setId(rs.getInt("id"));
        a.setNombre(rs.getString("nombre"));
        a.setNacionalidad(rs.getString("nacionalidad"));
        return a;
    }

    @Override
    public void insertar(Autor autor) {
        String query = "INSERT INTO autores(nombre, nacionalidad) VALUES(?, ?)";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, autor.getNombre());
            pst.setString(2, autor.getNacionalidad());
            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    autor.setId(keys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new DatosException("No se pudo guardar el autor", ex);
        }
    }

    @Override
    public Autor buscarPorId(int id) {
        String query = "SELECT id, nombre, nacionalidad FROM autores WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new DatosException("Error consultando autor por ID", ex);
        }
    }

    @Override
    public List<Autor> listar() {
        String query = "SELECT id, nombre, nacionalidad FROM autores";
        List<Autor> registros = new ArrayList<>();
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                registros.add(mapear(rs));
            }
            return registros;
        } catch (SQLException ex) {
            throw new DatosException("Error obteniendo listado de autores", ex);
        }
    }

    @Override
    public void actualizar(Autor autor) {
        String query = "UPDATE autores SET nombre = ?, nacionalidad = ? WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, autor.getNombre());
            pst.setString(2, autor.getNacionalidad());
            pst.setInt(3, autor.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DatosException("Error modificando datos del autor", ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String query = "DELETE FROM autores WHERE id = ?";
        try (Connection cn = Conexion.abrir();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new DatosException("Error borrando autor de la base de datos", ex);
        }
    }
}