package com.pgv.david.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Comida {
    private int posX;
    private int posY;
    private Bitmap sprite;
    private Context context;

    public Comida(Context context, int posX, int posY) {
        this.context = context;
        this.posX = posX;
        this.posY = posY;
    }

    public void pintar(Canvas canvas, Paint paint, int tamanioBloque) {
        if(sprite == null) setSprite(tamanioBloque);
        canvas.drawBitmap(sprite,posX * tamanioBloque,posY * tamanioBloque,paint);
    }

    private void setSprite(int tamanioBloque) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.comida_sprite);
        this.sprite = Bitmap.createScaledBitmap(bitmap, tamanioBloque, tamanioBloque, false);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
