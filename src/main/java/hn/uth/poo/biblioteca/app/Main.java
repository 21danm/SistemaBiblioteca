/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hn.uth.poo.biblioteca.app;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.dao.impl.AutorDAOImpl;
import hn.uth.poo.biblioteca.dao.impl.LibroDAOImpl;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;
import hn.uth.poo.biblioteca.modelo.Libro;
import hn.uth.poo.biblioteca.servicio.ServicioAutores;
import hn.uth.poo.biblioteca.servicio.ServicioLibros;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner lector = new Scanner(System.in);

    public static void main(String[] args) {
        AutorDAO daoAutor = new AutorDAOImpl();
        LibroDAO daoLibro = new LibroDAOImpl();

        ServicioAutores srvAutores = new ServicioAutores(daoAutor, daoLibro);
        ServicioLibros srvLibros = new ServicioLibros(daoLibro, daoAutor);

        boolean activo = true;
        while (activo) {
            System.out.println("\n+---------------------------------------+");
            System.out.println("|       CONTROL DE BIBLIOTECA UTH       |");
            System.out.println("+---------------------------------------+");
            System.out.println("| 1. Modulo de Autores                  |");
            System.out.println("| 2. Modulo de Libros                   |");
            System.out.println("| 3. Salir de la aplicacion             |");
            System.out.println("+---------------------------------------+");

            int op = pedirEntero(">> Elija un modulo: ");
            switch (op) {
                case 1 -> menuAutores(srvAutores);
                case 2 -> menuLibros(srvLibros);
                case 3 -> {
                    System.out.println("Programa finalizado.");
                    activo = false;
                }
                default -> System.out.println("* Opcion fuera de rango.");
            }
        }
    }

    private static void menuAutores(ServicioAutores srv) {
        System.out.println("\n--- [ MODULO AUTORES ] ---");
        System.out.println("1) Registrar  2) Listar  3) Buscar ID  4) Modificar  5) Eliminar  0) Volver");
        int op = pedirEntero("Accion: ");
        try {
            switch (op) {
                case 1 -> {
                    System.out.print("Nombre completo: ");
                    String nom = lector.nextLine();
                    System.out.print("Pais / Nacionalidad: ");
                    String nac = lector.nextLine();
                    Autor a = new Autor(nom, nac);
                    srv.guardar(a);
                    System.out.println(">> Guardado con exito. ID asignado: " + a.getId());
                }
                case 2 -> {
                    List<Autor> lista = srv.obtenerTodos();
                    if (lista.isEmpty()) {
                        System.out.println("(Sin registros de autores)");
                    } else {
                        lista.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    int id = pedirEntero("ID del autor a consultar: ");
                    System.out.println("Resultado: " + srv.obtenerPorId(id));
                }
                case 4 -> {
                    int id = pedirEntero("ID del autor a modificar: ");
                    System.out.println("Actual: " + srv.obtenerPorId(id));
                    System.out.print("Nuevo nombre: ");
                    String nom = lector.nextLine();
                    System.out.print("Nueva nacionalidad: ");
                    String nac = lector.nextLine();
                    srv.modificar(new Autor(id, nom, nac));
                    System.out.println(">> Registro actualizado.");
                }
                case 5 -> {
                    int id = pedirEntero("ID del autor a borrar: ");
                    srv.borrar(id);
                    System.out.println(">> Autor borrado de la base de datos.");
                }
                case 0 -> { }
                default -> System.out.println("* Opcion invalida.");
            }
        } catch (DatoInvalidoException | DatosException ex) {
            System.out.println("(!) Rechazado: " + ex.getMessage());
        }
    }

    private static void menuLibros(ServicioLibros srv) {
        System.out.println("\n--- [ MODULO LIBROS ] ---");
        System.out.println("1) Registrar  2) Listar todos  3) Buscar ID  4) Modificar  5) Eliminar  6) Filtrar por Autor  0) Volver");
        int op = pedirEntero("Accion: ");
        try {
            switch (op) {
                case 1 -> {
                    System.out.print("Codigo ISBN: ");
                    String isbn = lector.nextLine();
                    System.out.print("Titulo de la obra: ");
                    String tit = lector.nextLine();
                    int anio = pedirEntero("Anio publicacion: ");
                    double precio = pedirDecimal("Precio unitario: ");
                    int idAutor = pedirEntero("ID del Autor: ");
                    Libro lb = new Libro(isbn, tit, anio, precio, idAutor);
                    srv.guardar(lb);
                    System.out.println(">> Libro guardado con ID: " + lb.getId());
                }
                case 2 -> {
                    List<Libro> lista = srv.obtenerTodos();
                    if (lista.isEmpty()) {
                        System.out.println("(Sin registros de libros)");
                    } else {
                        lista.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    int id = pedirEntero("ID del libro a consultar: ");
                    System.out.println("Resultado: " + srv.obtenerPorId(id));
                }
                case 4 -> {
                    int id = pedirEntero("ID del libro a modificar: ");
                    System.out.println("Actual: " + srv.obtenerPorId(id));
                    System.out.print("Nuevo ISBN: ");
                    String isbn = lector.nextLine();
                    System.out.print("Nuevo titulo: ");
                    String tit = lector.nextLine();
                    int anio = pedirEntero("Nuevo anio: ");
                    double precio = pedirDecimal("Nuevo precio: ");
                    int idAutor = pedirEntero("Nuevo ID Autor: ");
                    srv.modificar(new Libro(id, isbn, tit, anio, precio, idAutor));
                    System.out.println(">> Libro modificado exitosamente.");
                }
                case 5 -> {
                    int id = pedirEntero("ID del libro a borrar: ");
                    srv.borrar(id);
                    System.out.println(">> Libro borrado.");
                }
                case 6 -> {
                    int idAutor = pedirEntero("Ingrese ID del autor: ");
                    List<Libro> porAutor = srv.obtenerPorAutor(idAutor);
                    if (porAutor.isEmpty()) {
                        System.out.println("(Este autor no tiene libros registrados)");
                    } else {
                        porAutor.forEach(System.out::println);
                    }
                }
                case 0 -> { }
                default -> System.out.println("* Opcion invalida.");
            }
        } catch (DatoInvalidoException | DatosException ex) {
            System.out.println("(!) Rechazado: " + ex.getMessage());
        }
    }

    private static int pedirEntero(String etiqueta) {
        while (true) {
            try {
                System.out.print(etiqueta);
                return Integer.parseInt(lector.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("* Ingrese un numero entero.");
            }
        }
    }

    private static double pedirDecimal(String etiqueta) {
        while (true) {
            try {
                System.out.print(etiqueta);
                return Double.parseDouble(lector.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("* Ingrese un monto valido.");
            }
        }
    }
}
