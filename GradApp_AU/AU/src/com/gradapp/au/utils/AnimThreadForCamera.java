package com.gradapp.au.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class AnimThreadForCamera extends Thread {

    private SurfaceHolder holder;
    private boolean running = true;
    int i = 0;

    public AnimThreadForCamera(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void run() {
        while(running ) {
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                 synchronized (holder) {
                    // draw
                    canvas.drawColor(Color.BLACK);
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(i++, 100, 50, paint);
                }
            }
            finally {
                    if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas);
                        }
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}