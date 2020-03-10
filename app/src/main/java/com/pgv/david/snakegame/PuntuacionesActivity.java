package com.pgv.david.snakegame;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PuntuacionesActivity extends Activity {
    private TextView title;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);
        this.list = findViewById(R.id.listaPuntuaciones);
        this.title = findViewById(R.id.titlePuntuaciones);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/orange.ttf");
        title.setTypeface(typeface);

        PuntuacionesManager puntuacionesManager = new PuntuacionesManager(this);
        // Hacemos una consulta de todas la puntuaciones de la bd,
        // las guardamos en un array y los ponemos en el ListView
        ArrayList<Puntuacion> puntuaciones = puntuacionesManager.query();
        String[] puntuacionesString = new String[puntuaciones.size()];
        for(int i = 0; i < puntuaciones.size(); i++) {
            Puntuacion puntuacion = puntuaciones.get(i);
            puntuacionesString[i] = puntuacion.getNombre() + " --- " + puntuacion.getPuntuacion();
        }
        // android.R.layout.simple_list_item_1
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.lisview_puntuaciones,puntuacionesString);
        list.setAdapter(arrayAdapter);
    }
}
