/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.servicio;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.modelo.Autor;
import java.util.List;

public class ServicioAutores {
    private final AutorDAO daoAutor;
    private final LibroDAO daoLibro;

    public ServicioAutores(AutorDAO daoAutor, LibroDAO daoLibro) {
        this.daoAutor = daoAutor;
        this.daoLibro = daoLibro;
    }

    public void guardar(Autor autor) {
        verificarCampos(autor);
        daoAutor.insertar(autor);
    }

    public Autor obtenerPorId(int id) {
        Autor encontrado = daoAutor.buscarPorId(id);
        if (encontrado == null) {
            throw new DatoInvalidoException("El autor con ID " + id + " no se encuentra registrado.");
        }
        return encontrado;
    }

    public List<Autor> obtenerTodos() {
        return daoAutor.listar();
    }

    public void modificar(Autor autor) {
        obtenerPorId(autor.getId());
        verificarCampos(autor);
        daoAutor.actualizar(autor);
    }

    public void borrar(int id) {
        obtenerPorId(id);
        if (!daoLibro.listarPorAutor(id).isEmpty()) {
            throw new DatoInvalidoException("Operacion rechazada: El autor tiene libros asociados y no puede borrarse.");
        }
        daoAutor.eliminar(id);
    }

    private void verificarCampos(Autor a) {
        if (a == null || a.getNombre() == null || a.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre del autor es un campo requerido.");
        }
    }
}