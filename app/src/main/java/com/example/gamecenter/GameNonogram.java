package com.example.gamecenter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameNonogram extends AppCompatActivity {

    private final int columns = 5;
    private final int rows = 5;
    private TextView[][] tablero;
    private TextView[][] tableroSolucion;
    private boolean partidaAcabada;
    private GameTimer gameTimer;
    private int puntuacion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_nonogram);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button volver = findViewById(R.id.button_volver_nonogram);
        ImageButton restart = findViewById(R.id.reset_nonogram);
        ProgressBar timerProgressBar = findViewById(R.id.progressBar_nonogram);

        gameTimer = new GameTimer(timerProgressBar, 100, this);
        partidaAcabada = false;
        this.tablero = new TextView[rows][columns];
        this.tableroSolucion = new TextView[rows][columns];


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
            }
        });

        empezarJuego();
    }

    private void empezarJuego() {
        iniciarTablero();
        generarSolucionAleatoria();
        inicarTimer();
    }

    private void inicarTimer(){
        Thread timerThread = new Thread(gameTimer);
        timerThread.start();
    }

    public void acabarPartida(){
        partidaAcabada = true;
        TextView partidaAcabada = findViewById(R.id.partida_acabada_nonogram);
        partidaAcabada.setText("Time!");
    }

    private void vaciarTablero(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                this.tablero[row][col].setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void iniciarTablero() {
        GridLayout tableroGrid = findViewById(R.id.layout_nonogram);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                final int r = row, c = col; // Final variables for click listener

                // Create a new TextView as a tile
                TextView tile = new TextView(this);
                tile.setLayoutParams(new GridLayout.LayoutParams());
                tile.setWidth(150);
                tile.setHeight(150);
                tile.setBackgroundColor(Color.WHITE);
                tile.setTextSize(18);
                tile.setGravity(android.view.Gravity.CENTER);

                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleColor(tablero[r][c]);
                        if (comprobarSolucion()){
                            sumarPuntos();
                            vaciarTablero();
                            generarSolucionAleatoria();
                        }
                    }
                });

                // Add tile to array and GridLayout
                tablero[row][col] = tile;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.setMargins(5, 5, 5, 5);
                tile.setLayoutParams(params);

                tableroGrid.addView(tile);
            }
        }
    }

    private void sumarPuntos() {
        puntuacion++;
        TextView puntuacionView = findViewById(R.id.puntuacion_nonogram);
        puntuacionView.setText("Score: " + puntuacion);
        this.gameTimer.addTiempo(20);
    }

    private boolean comprobarSolucion() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int colorTablero = getColorFromBackground(tablero[row][col]);
                int colorSolucion = getColorFromBackground(tableroSolucion[row][col]);
                // Comprobar el valor del integer del color de la casilla (-16777216 para negro, -1 para blanco y -65536 para rojo)
                // Comprobar si el valor es diferente de la solucion, ignorando el color rojo

                // Si la casilla solucion es negra y la del tablero no es negra, no esta solucionado
                if (colorSolucion == -16777216 && colorTablero != -16777216) {
                    return false;
                }

                // Si la casilla solucion es blanca y la del tablero es negra no esta solucionado, ignora las rojas
                if (colorSolucion == -1 && (colorTablero == -16777216)) {
                    return false; // Hay una casilla negra donde no deberia
                }
            }
        }
        return true;
    }

    private int getColorFromBackground(TextView tile) {
        if (tile.getBackground() instanceof ColorDrawable) {
            return ((ColorDrawable) tile.getBackground()).getColor();
        }
        return Color.TRANSPARENT;
    }

    // Rotar entre los 3 colores en este orden: Blanco>Negro>Rojo
    private void toggleColor(TextView tile) {
        if (!partidaAcabada) {
            if (tile.getBackground() != null && ((android.graphics.drawable.ColorDrawable) tile.getBackground()).getColor() == Color.RED) {
                tile.setBackgroundColor(Color.WHITE);
            } else if (tile.getBackground() != null && ((android.graphics.drawable.ColorDrawable) tile.getBackground()).getColor() == Color.BLACK) {
                tile.setBackgroundColor(Color.RED);
            } else {
                tile.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private void generarSolucionAleatoria() {
        Random random = new Random();
        int maxTiles = 20;
        int minTiles = 11;

        // Generate a random number of black tiles (between 7 and 20)
        int blackTilesCount = random.nextInt(maxTiles-minTiles) + minTiles; // 14 (range) + 7 (minimum) = [7, 20]

        // Create a list of all possible tile positions
        ArrayList<int[]> positions = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                positions.add(new int[]{row, col});
            }
        }

        // Shuffle the positions randomly
        Collections.shuffle(positions, random);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                TextView tile = new TextView(this);
                tile.setBackgroundColor(Color.WHITE); // Default to white
                tableroSolucion[row][col] = tile;
            }
        }
        // Pick the first `blackTilesCount` positions and set them to black
        for (int i = 0; i < blackTilesCount; i++) {
            int[] pos = positions.get(i);
            int row = pos[0], col = pos[1];
            tableroSolucion[row][col].setBackgroundColor(Color.BLACK);
        }


        iniciarTableroSolucion();
        calcularPistasFilas();
        calcularPistasColumnas();
    }

    private void iniciarTableroSolucion() {
        GridLayout tableroGrid = findViewById(R.id.layout_nonogram_solucion);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                final int r = row, c = col; // Final variables for click listener

                // Create a new TextView as a tile
                TextView tile = this.tableroSolucion[row][col];
                tile.setLayoutParams(new GridLayout.LayoutParams());
                tile.setWidth(50);
                tile.setHeight(50);

                tile.setTextSize(18);
                tile.setGravity(android.view.Gravity.CENTER);

                // Add tile to array and GridLayout
                tableroSolucion[row][col] = tile;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 50;
                params.height = 50;
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.setMargins(1, 1, 1, 1);
                tile.setLayoutParams(params);

                tableroGrid.addView(tile);
            }
        }
    }

    private void calcularPistasFilas(){
        int[] filasViews = {R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5};

        for (int row = 0; row < rows; row++) {
            StringBuilder textoFila = new StringBuilder();
            int contador = 0;
            boolean primerNumero = true;
            boolean entrado = false;
            for (int col = 0; col < columns; col++) {
                int casillaActual = getColorFromBackground(this.tableroSolucion[row][col]);

                // Comprobar el valor del integer del color de la casilla (-16777216 para negro y -1 para blanco)
                if (casillaActual == -16777216) {
                    contador++;
                    entrado = true;
                }
                // Primera casilla blanca encontrada despues de haber encontrado blancas
                else if (casillaActual == -1 && contador > 0) {
                    if (primerNumero){
                        textoFila.append(contador);
                        primerNumero = false;
                        contador = 0;
                    }
                    else {
                        textoFila.append(" ").append(contador);
                        contador = 0;
                    }
                }
            }
            if (contador > 0 && primerNumero) {
                textoFila.append(contador);
            } else if (contador > 0) {
                textoFila.append(" ").append(contador);
            }
            if (!entrado) {
                textoFila.append("0");
            }
            TextView filaView = findViewById(filasViews[row]);
            filaView.setText(textoFila.toString());
        }
    }

    private void calcularPistasColumnas(){
        int[] columnasViews = {R.id.column1, R.id.column2, R.id.column3, R.id.column4, R.id.column5};

        for (int col = 0; col < columns; col++) {
            StringBuilder textoColumna = new StringBuilder();
            int contador = 0;
            boolean primerNumero = true;
            boolean entrado = false;
            for (int row = 0; row < rows; row++) {
                int casillaActual = getColorFromBackground(this.tableroSolucion[row][col]);

                // Comprobar el valor del integer del color de la casilla (-16777216 para negro y -1 para blanco)
                if (casillaActual == -16777216) {
                    contador++;
                    entrado = true;
                }
                // Primera casilla blanca encontrada despues de haber encontrado blancas
                else if (casillaActual == -1 && contador > 0) {
                    if (primerNumero){
                        textoColumna.append(contador);
                        primerNumero = false;
                        contador = 0;
                    }
                    else {
                        textoColumna.append("\n").append(contador);
                        contador = 0;
                    }

                }
            }
            if (contador > 0 && primerNumero) {
                textoColumna.append(contador);
            } else if (contador > 0) {
                textoColumna.append("\n").append(contador);
            }
            if (!entrado) {
                textoColumna.append(0);
            }
            TextView columnaView = findViewById(columnasViews[col]);
            columnaView.setText(textoColumna.toString());
        }
    }


    private void reiniciar() {
        gameTimer.pararTimer();
        puntuacion = 0;
        partidaAcabada = false;
        TextView puntuacionView = findViewById(R.id.puntuacion_nonogram);
        TextView particaAcabadaView = findViewById(R.id.partida_acabada_nonogram);
        puntuacionView.setText("Score: " + puntuacion);
        particaAcabadaView.setText("");

        vaciarTablero();
        generarSolucionAleatoria();
        inicarTimer();
    }
}