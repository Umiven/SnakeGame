package com.pgv.david.snakegame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        this.title = findViewById(R.id.titleMainMenu);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/orange.ttf");
        title.setTypeface(typeface);
    }

    public void comenzarJuego(View view) {
        startActivity(new Intent(MainMenuActivity.this,SnakeActivity.class));
    }
    
    public void mostarPuntuaciones(View view) {
        startActivity(new Intent(MainMenuActivity.this,PuntuacionesActivity.class));
    }

    @Override
    public void onBackPressed() {
        // Si pulsa atras, no hace nada, ya
        // que no puedo forzar el cierre de la app
    }
}
