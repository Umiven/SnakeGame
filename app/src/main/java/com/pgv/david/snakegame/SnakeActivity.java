package com.pgv.david.snakegame;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class SnakeActivity extends Activity {
    private SnakeEngine snakeEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtenemos pantallla
        Display pantalla = getWindowManager().getDefaultDisplay();

        // Obtenemos resolucion de la pantalla
        Point tamanioPantalla = new Point();
        pantalla.getSize(tamanioPantalla);

        this.snakeEngine = new SnakeEngine(this,tamanioPantalla);
        setContentView(this.snakeEngine);
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.onResume();
    }
}
