package com.pgv.david.snakegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class SnakeEngine extends SurfaceView implements Runnable, SensorEventListener{
    private Thread hilo = null;
    private Random random;
    private Context contexto;
    private int pantallaX;
    private int pantallaY;

    private Serpiente serpiente;
    private Comida comida;

    private int puntuacion;

    // Siguiente momento en el tiempo en el
    // que se actualizara la pantalla
    private long siguienteActuFrame;
    // Actualiza el juego 4 veces cada segundo
    private long FPS = 4;


    // Se va a dividir la pantalla en bloques
    // en funcion de su resolucion. Preestablecemos
    // el numero de bloques que queremos en el ejeX,
    // y en funcion de eso sacamos los bloques que
    // caben en el ejeY, y el tamaño de esos
    // bloques, el cual nos hara falta a la hora de pintar.

    private final int numBloquesX = 25;
    private int numBloquesY;
    private int tamanioBloque;

    private volatile boolean estaJugando;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    // Variables acelerometro
    private SensorManager sensorManager;
    private Sensor sensAcelerometro;


    public SnakeEngine(Context contexto,Point tamanioPantalla) {
        super(contexto);
        this.random = new Random();
        this.contexto = contexto;
        // Tamanio pantalla
        this.pantallaX = tamanioPantalla.x;
        this.pantallaY = tamanioPantalla.y;
        // Tamanio de cada bloque en funcion del numero de bloques
        // que queramos en el ejeX
        this.tamanioBloque = pantallaX / this.numBloquesX;
        // Numero de bloques que caben en Y
        this.numBloquesY = pantallaY / tamanioBloque;
        // Inicializamos objetos para dibujar
        this.surfaceHolder = getHolder();
        this.paint = new Paint();

        // Acelerómetro
        this.sensorManager = (SensorManager) contexto.getSystemService(Context.SENSOR_SERVICE);
        this.sensAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensAcelerometro,SensorManager.SENSOR_DELAY_GAME);
        // Iniciamos partida
        newGame();
    }

    private void newGame() {
        // Inicializamos serpiente con un tamaño maximo de 50
        this.serpiente = new Serpiente(contexto,50,numBloquesX,numBloquesY);
        this.puntuacion = 0;
        apareceComida();
        this.siguienteActuFrame = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while(estaJugando) {
            if (actualizacionNecesaria()) {
                actualizar();
                pintar();
            }
        }
    }

    private void actualizar() {
        // Si ha muerto, se acaba la partida
        if (serpiente.haMuerto()) {
            // Puntuaciones
            Intent intent = new Intent(contexto,RegistrarPuntuacionActivity.class);
            intent.putExtra("puntuacion","" + this.puntuacion);
            contexto.startActivity(intent);
            //newGame();
        }

        if (serpiente.getSerpienteX()[0] == comida.getPosX() && serpiente.getSerpienteY()[0] == comida.getPosY()) {
            hasComido();
        }

        /* Posibles niveles
        if(puntuacion % 10 == 0 && puntuacion != 0) {
            this.FPS += 3;
        }*/
        serpiente.moverSerpiente();
    }

    private boolean actualizacionNecesaria() {
        // Si el tiempo actual es mayor o igual que el que tenemos guardado en la
        // variable, es momento de actualizar
        if (siguienteActuFrame <= System.currentTimeMillis()) {
            // El proximo momento en el que se actualizara la pantalla
            // se calcula en funcion del valor de FPS
            siguienteActuFrame = System.currentTimeMillis() + (1000 / FPS);
            return true;
        }
        return false;
    }

    private void hasComido() {
        serpiente.aumentarTamanio();
        puntuacion++;
        apareceComida();
    }

    private void apareceComida() {
        int siguientePosX = 0;
        int siguientePosY = 0;
        boolean comidaSeSuperpone = true;
        while(comidaSeSuperpone) {
            siguientePosX = random.nextInt(numBloquesX);
            siguientePosY = random.nextInt(numBloquesY);
            comidaSeSuperpone = serpiente.mismoPunto(new Point(siguientePosX,siguientePosY));
        }
        this.comida = new Comida(contexto,siguientePosX,siguientePosY);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] > 4) {
                // Si no se esta moviendo hacia arriba, puede girar
                if (serpiente.getSerpienteY()[0] >= serpiente.getSerpienteY()[1]) {
                    serpiente.setDireccion(Direccion.ABAJO);
                }
            }
            if (event.values[0] < -4) {
                // Si no se esta moviendo hacia abajo, puede girar
                if (serpiente.getSerpienteY()[0] <= serpiente.getSerpienteY()[1]) {
                    serpiente.setDireccion(Direccion.ARRIBA);
                }
            }
            if(event.values[1] < -4) {
                // Si no se esta moviendo a la derecha, puede girar
                if(serpiente.getSerpienteX()[0] <= serpiente.getSerpienteX()[1]) {
                    serpiente.setDireccion(Direccion.IZQUIERDA);
                }
            }
            if (event.values[1] > 4) {
                // Si no se esta moviendo a la izquierda, puede girar
                if(serpiente.getSerpienteX()[0] >= serpiente.getSerpienteX()[1]) {
                    serpiente.setDireccion(Direccion.DERECHA);
                }
            }
        }
    }

    private void pintar() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            // Pintamos fondo
            Drawable fondo = getResources().getDrawable(R.drawable.background, null);
            fondo.setBounds(0, 0, (numBloquesX + 1) * tamanioBloque, (numBloquesY + 1) * tamanioBloque+1);
            fondo.draw(canvas);
            // Cambiamos color pincel para pintar puntuacion
            paint.setColor(Color.argb(255, 255, 255, 255));
            // Puntuacion
            paint.setTextSize(90);
            canvas.drawText("" + puntuacion, 10, 70, paint);
            // Serpiente
            serpiente.pintar(canvas,paint,tamanioBloque);
            // Comida
            comida.pintar(canvas,paint,tamanioBloque);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void onPause() {
        this.estaJugando = false;
        try {
            sensorManager.unregisterListener(this);
            this.hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        estaJugando = true;
        sensorManager.registerListener(this, sensAcelerometro, SensorManager.SENSOR_DELAY_GAME);
        this.hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
