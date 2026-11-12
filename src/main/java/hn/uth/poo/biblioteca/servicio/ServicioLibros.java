/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.servicio;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.modelo.Libro;
import java.time.Year;
import java.util.List;

public class ServicioLibros {
    private final LibroDAO daoLibro;
    private final AutorDAO daoAutor;

    public ServicioLibros(LibroDAO daoLibro, AutorDAO daoAutor) {
        this.daoLibro = daoLibro;
        this.daoAutor = daoAutor;
    }

    public void guardar(Libro libro) {
        comprobarReglas(libro, 0);
        daoLibro.insertar(libro);
    }

    public Libro obtenerPorId(int id) {
        Libro lb = daoLibro.buscarPorId(id);
        if (lb == null) {
            throw new DatoInvalidoException("No se encontro ningun libro con el ID " + id + ".");
        }
        return lb;
    }

    public List<Libro> obtenerTodos() {
        return daoLibro.listar();
    }

    public List<Libro> obtenerPorAutor(int autorId) {
        if (daoAutor.buscarPorId(autorId) == null) {
            throw new DatoInvalidoException("El autor indicado (ID " + autorId + ") no existe.");
        }
        return daoLibro.listarPorAutor(autorId);
    }

    public void modificar(Libro libro) {
        obtenerPorId(libro.getId());
        comprobarReglas(libro, libro.getId());
        daoLibro.actualizar(libro);
    }

    public void borrar(int id) {
        obtenerPorId(id);
        daoLibro.eliminar(id);
    }

    private void comprobarReglas(Libro lb, int idIgnorar) {
        if (lb == null) {
            throw new DatoInvalidoException("Datos del libro incompletos.");
        }
        if (lb.getTitulo() == null || lb.getTitulo().isBlank()) {
            throw new DatoInvalidoException("El titulo del libro es obligatorio.");
        }
        if (lb.getIsbn() == null || lb.getIsbn().isBlank()) {
            throw new DatoInvalidoException("El ISBN es obligatorio.");
        }

        int limiteAnio = Year.now().getValue();
        if (lb.getAnio() < 1450 || lb.getAnio() > limiteAnio) {
            throw new DatoInvalidoException("El anio debe ubicarse en el rango de 1450 a " + limiteAnio + ".");
        }
        if (lb.getPrecio() < 0) {
            throw new DatoInvalidoException("El precio debe ser mayor o igual a 0.");
        }
        if (daoAutor.buscarPorId(lb.getAutorId()) == null) {
            throw new DatoInvalidoException("El autor con ID " + lb.getAutorId() + " no existe en la base de datos.");
        }

        for (Libro existente : daoLibro.listar()) {
            if (existente.getIsbn().equalsIgnoreCase(lb.getIsbn().trim()) && existente.getId() != idIgnorar) {
                throw new DatoInvalidoException("El ISBN '" + lb.getIsbn() + "' ya pertenece a otro libro registrado.");
            }
        }
    }
}