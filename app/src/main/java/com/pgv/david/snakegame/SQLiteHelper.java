package com.pgv.david.snakegame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "puntuaciones.db";

    public SQLiteHelper(Context context ) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTablePuntuaciones = "CREATE TABLE " + Puntuacion.TABLA  + "("
                + Puntuacion.CAMPO_nombre + " TEXT PRIMARY KEY , "
                + Puntuacion.CAMPO_puntuacion + " INTEGER )";

        db.execSQL(createTablePuntuaciones);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
