package com.pgv.david.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Serpiente {
    private Context context;
    private int numBloquesX;
    private int numBloquesY;
    private int serpienteLargo;
    private int[] serpienteX;
    private int[] serpienteY;
    private Direccion direccion;
    private Bitmap spriteCabeza;
    private Bitmap spriteCuerpo;
    private Bitmap spriteCola;

    public Serpiente(Context context, int tamanio, int numBloquesX, int numBloquesY) {
        this.context = context;
        this.numBloquesX = numBloquesX;
        this.numBloquesY = numBloquesY;
        this.serpienteX = new int[tamanio];
        this.serpienteY = new int[tamanio];
        // Posicionamos cabeza serpiente en el centro de la pantalla
        // al iniciar la partida
        this.serpienteX[0] = numBloquesX / 2;
        this.serpienteY[0] = numBloquesY / 2;
        // Dibujamos la cola al empezar tambien
        this.serpienteX[1] = serpienteX[0]-1;
        this.serpienteY[1] = serpienteX[1]-1;
        this.serpienteLargo = 2;
        this.direccion = Direccion.DERECHA;
    }

    public void moverSerpiente() {
        // Movemos todas las coordenadas de
        // los arrays una posicion hacia atras
        for (int i = serpienteLargo; i > 0; i--) {
            serpienteX[i] = serpienteX[i-1];
            serpienteY[i] = serpienteY[i-1];
        }

        // Y aumentamos las coordenadas de la cabeza
        // en funcion de hacia donde mire
        switch (direccion) {
            case DERECHA:
                serpienteX[0]++;
                break;
            case IZQUIERDA:
                serpienteX[0]--;
                break;
            case ABAJO:
                serpienteY[0]++;
                break;
            case ARRIBA:
                serpienteY[0]--;
                break;
        }
    }

    public boolean haMuerto() {
        boolean muerta = false;
        // Muerte al tocarse a su misma
        for(int i = 1; i < serpienteLargo; i++) {
            if(serpienteX[0] == serpienteX[i] && serpienteY[0] == serpienteY[i]) {
                muerta = true;
                break;
            }
        }

        //Muerte al tocar paredes
        if (serpienteX[0] == -1) muerta = true;
        if (serpienteX[0] == numBloquesX) muerta = true;
        if (serpienteY[0] == -1) muerta = true;
        if (serpienteY[0] == numBloquesY) muerta = true;

        return muerta;
    }

    public void pintar(Canvas canvas, Paint paint, int tamanioBloque) {
        for(int i = 0; i < serpienteLargo; i++) {
            if(i == 0) {
                setSpriteCabeza(tamanioBloque);
                canvas.drawBitmap(spriteCabeza,serpienteX[i] * tamanioBloque,serpienteY[i] * tamanioBloque,paint);
            } else if(i == serpienteLargo - 1) {
                setSpriteCola(i,tamanioBloque);
                canvas.drawBitmap(spriteCola,serpienteX[i] * tamanioBloque,serpienteY[i] * tamanioBloque,paint);
            } else {
                setSpriteCuerpo(i,tamanioBloque);
                canvas.drawBitmap(spriteCuerpo,serpienteX[i] * tamanioBloque,serpienteY[i] * tamanioBloque,paint);
            }
        }
    }

    private void setSpriteCabeza(int tamanioBloque) {
        // Seleccionamos el sprite correspondiente de la cabeza
        // en funcion de la direccion a la que se diriga
        Bitmap bitmap;
        switch (direccion) {
            case DERECHA:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cabeza_der_sprite);
                this.spriteCabeza = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
                break;
            case IZQUIERDA:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cabeza_izq_sprite);
                this.spriteCabeza = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
                break;
            case ARRIBA:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cabeza_arr_sprite);
                this.spriteCabeza = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
                break;
            case ABAJO:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cabeza_abajo_sprite);
                this.spriteCabeza = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
                break;
        }
    }

    private void setSpriteCuerpo(int posCuerpo, int tamanioBloque) {
        // Seleccionamos el sprite correspondiente del cuerpo
        // mirando el punto anterior y el punto siguiente
        // al que queremos pintar. En funcion de los mismos podemos
        // saber que tipo de cuerpo hay que escoger
        Bitmap bitmap;
        // De derecha a abajo - De arriba a izquierda
        if((serpienteX[posCuerpo] > serpienteX[posCuerpo + 1] && serpienteY[posCuerpo] < serpienteY[posCuerpo-1]) ||
                (serpienteY[posCuerpo] < serpienteY[posCuerpo+1] && serpienteX[posCuerpo] > serpienteX[posCuerpo-1])) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_giro_abajo_izq);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }

        // De izquierda a abajo - De arriba a derecha
        if((serpienteX[posCuerpo] < serpienteX[posCuerpo-1] && serpienteY[posCuerpo] < serpienteY[posCuerpo+1]) ||
                (serpienteY[posCuerpo] < serpienteY[posCuerpo-1] && serpienteX[posCuerpo] < serpienteX[posCuerpo+1])) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_giro_abajo_der);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }

        // De derecha a arriba - De abajo a izquierda
        if((serpienteX[posCuerpo] > serpienteX[posCuerpo-1] && serpienteY[posCuerpo] > serpienteY[posCuerpo+1]) ||
                (serpienteY[posCuerpo] > serpienteY[posCuerpo-1] && serpienteX[posCuerpo] > serpienteX[posCuerpo+1])) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_giro_arr_izq);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }

        // De izquieda a arriba - De abajo a derecha
        if((serpienteX[posCuerpo] < serpienteX[posCuerpo-1] && serpienteY[posCuerpo] > serpienteY[posCuerpo+1])
                || (serpienteY[posCuerpo] > serpienteY[posCuerpo-1] && serpienteX[posCuerpo] < serpienteX[posCuerpo+1])) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_giro_arr_der);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }

        // Vertical
        if((serpienteY[posCuerpo] > serpienteY[posCuerpo-1]) || serpienteY[posCuerpo] < serpienteY[posCuerpo-1]) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cuerpo_vert);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }

        // Horizontal
        if((serpienteX[posCuerpo] < serpienteX[posCuerpo-1]) || (serpienteX[posCuerpo] > serpienteX[posCuerpo-1])) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cuerpo_horiz);
            this.spriteCuerpo = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
            return;
        }
    }

    public void setSpriteCola(int posCola,int tamanioBloque) {
        // Seleccionamos el sprite correspondiente de la cola
        // mirando el punto anterior, y en funcion de su posicion
        // podemos saber que tipo de cola hay que escoger
        Bitmap bitmap;
        if (serpienteX[posCola] < serpienteX[posCola-1]) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cola_der_sprite);
            this.spriteCola = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
        }

        if(serpienteX[posCola] > serpienteX[posCola - 1]) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cola_izq_sprite);
            this.spriteCola = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
        }

        if(serpienteY[posCola] > serpienteY[posCola - 1]) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cola_arr_sprite);
            this.spriteCola = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
        }

        if(serpienteY[posCola] < serpienteY[posCola - 1]) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.serp_cola_abajo_sprite);
            this.spriteCola = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
        }
    }

    public boolean mismoPunto(Point punto) {
        for(int i = 0; i < serpienteLargo; i++) {
            if(punto.x == serpienteX[i] && punto.y == serpienteY[i]) {
                return true;
            }
        }
        return false;
    }

    public int[] getSerpienteX() {
        return serpienteX;
    }

    public int[] getSerpienteY() {
        return serpienteY;
    }

    public void aumentarTamanio() {
        this.serpienteLargo++;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
