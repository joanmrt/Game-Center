package com.example.gamecenter;

import static java.lang.Thread.sleep;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

public class GameTimer implements Runnable{
    private ProgressBar progressBar;
    private int tiempoRestante;
    private Handler handler;
    private boolean isRunning = false;
    private GameNonogram gameNonogram;

    public GameTimer(ProgressBar progressBar, int tiempoInicial, GameNonogram gameNonogram) {
        this.progressBar = progressBar;
        this.tiempoRestante = tiempoInicial;
        this.handler = new Handler(Looper.getMainLooper());
        this.gameNonogram = gameNonogram;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning && tiempoRestante > 0){
            tiempoRestante--;
            handler.post(() -> {
                progressBar.setProgress(tiempoRestante);
                if (tiempoRestante == 0) {
                    progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    gameNonogram.acabarPartida();
                }
            });

            try {
                sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void pararTimer(){
        isRunning = false;
        tiempoRestante = 100;
        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        handler.post(() -> progressBar.setProgress(tiempoRestante));
        try {
            sleep(600);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTiempo(int amount) {
        tiempoRestante = Math.min(tiempoRestante + amount, 100); // Prevent overflow
        handler.post(() -> progressBar.setProgress(tiempoRestante));
    }
}
