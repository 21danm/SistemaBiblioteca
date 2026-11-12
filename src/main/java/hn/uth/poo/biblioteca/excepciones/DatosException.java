/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.excepciones;

public class DatosException extends RuntimeException {

    public DatosException(String detail, Throwable cause) {
        super(detail, cause);
    }

    public DatosException(String detail) {
        super(detail);
    }
}
