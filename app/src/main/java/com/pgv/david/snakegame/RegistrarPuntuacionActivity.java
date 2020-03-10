package com.pgv.david.snakegame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrarPuntuacionActivity extends Activity {
    private TextView title;
    private TextView titleRegistra;
    private EditText nombreET;
    private Button registrar;
    private Button reintentar;
    private Button volverMenu;
    private int puntuacion;
    private PuntuacionesManager puntuacionesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_puntuacion);
        this.titleRegistra = findViewById(R.id.titleRegistarPuntuacion);
        this.nombreET = findViewById(R.id.nombrePuntuacionEt);
        this.registrar = findViewById(R.id.btnRegistrar);
        this.reintentar = findViewById(R.id.btnReintentar);
        this.volverMenu = findViewById(R.id.btnMenuPrincipal);
        this.title = findViewById(R.id.titleMuerte);
        this.puntuacionesManager = new PuntuacionesManager(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/orange.ttf");
        title.setTypeface(typeface);
        // Puntuacion que ha obtenido en el juego
        puntuacion = Integer.parseInt(getIntent().getStringExtra("puntuacion"));
    }

    public void registrar(View view) {
        String nombre = nombreET.getText().toString();
        // Solo guardamos la puntuacion en la bd si inserta un nombre
        if(!nombre.isEmpty()) {
            Puntuacion resultado = new Puntuacion(nombre,puntuacion);
            if(!puntuacionesManager.actualizarPuntuacion(resultado)) {
                puntuacionesManager.insert(resultado);
            } else {
                puntuacionesManager.delete(resultado.getNombre());
                puntuacionesManager.insert(resultado);
            }
        }
        this.nombreET.setVisibility(View.GONE);
        this.titleRegistra.setVisibility(View.GONE);
        this.registrar.setVisibility(View.GONE);
        this.reintentar.setVisibility(View.VISIBLE);
        this.volverMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // Si se pulsa atras en esta pantalla no se hace
        // nada, que pulse los botones que para eso estan
    }

    public void reintentar(View view) {
        startActivity(new Intent(this,SnakeActivity.class));
    }

    public void volverMenu(View view) {
        startActivity(new Intent(this,MainMenuActivity.class));
    }
}