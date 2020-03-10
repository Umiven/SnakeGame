package com.pgv.david.snakegame;

public class Puntuacion {
    public static final String TABLA = "Puntuaciones";

    public static final String CAMPO_nombre = "nombre";
    public static final String CAMPO_puntuacion = "puntuacion";

    private String nombre;
    private int puntuacion;

    public Puntuacion() {
    }

    public Puntuacion(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}