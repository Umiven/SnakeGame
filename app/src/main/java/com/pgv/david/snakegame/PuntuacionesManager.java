package com.pgv.david.snakegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PuntuacionesManager {
    private SQLiteHelper helper;

    public PuntuacionesManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    public void insert(Puntuacion puntuacion) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Puntuacion.CAMPO_nombre, puntuacion.getNombre());
        values.put(Puntuacion.CAMPO_puntuacion,puntuacion.getPuntuacion());
        db.insert(Puntuacion.TABLA, null, values);
        db.close();
    }

    public ArrayList<Puntuacion> query() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Puntuacion.CAMPO_nombre + "," +
                Puntuacion.CAMPO_puntuacion +
                " FROM " + Puntuacion.TABLA +
                " ORDER BY " + Puntuacion.CAMPO_puntuacion + " DESC ";

        ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndex(Puntuacion.CAMPO_nombre));
                int puntuacion = cursor.getInt(cursor.getColumnIndex(Puntuacion.CAMPO_puntuacion));
                puntuaciones.add(new Puntuacion(nombre,puntuacion));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return puntuaciones;
    }

    public void delete(String nombre) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(Puntuacion.TABLA, Puntuacion.CAMPO_nombre + "='" + nombre + "'", null);
        db.close();
    }

    public boolean actualizarPuntuacion(Puntuacion puntuacion) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Puntuacion.CAMPO_nombre + "," +
                Puntuacion.CAMPO_puntuacion +
                " FROM " + Puntuacion.TABLA +
                " WHERE " + Puntuacion.CAMPO_nombre + " = '" + puntuacion.getNombre() + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()) {
            // Si el nick esta en la bd, y la puntuacion es menor
            // que la nueva, devolvemos true para actualizarlo
            int puntu = cursor.getInt(cursor.getColumnIndex(Puntuacion.CAMPO_puntuacion));
            if(puntu <= puntuacion.getPuntuacion()) {
                cursor.close();
                db.close();
                return true;
            }
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return false;
    }
}
