package com.example.gamecenter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
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
        generarSolucionAleatoria();
        iniciarTablero();
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
                        partidaAcabada = comprobarSolucion();
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

    private boolean comprobarSolucion() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int colorTablero = getColorFromBackground(tablero[row][col]);
                int colorSolucion = getColorFromBackground(tableroSolucion[row][col]);

                if (colorTablero != colorSolucion) {
                    return false; // Mismatch found
                }
            }
        }
        TextView partidaAcabada = findViewById(R.id.partida_acabada_nonogram);
        partidaAcabada.setText("You Win!");
        return true;
    }

    private int getColorFromBackground(TextView tile) {
        if (tile.getBackground() instanceof ColorDrawable) {
            return ((ColorDrawable) tile.getBackground()).getColor();
        }
        return Color.TRANSPARENT; // Default in case of no color
    }

    private void toggleColor(TextView tile) {
        if (!partidaAcabada) {
            if (tile.getBackground() != null && ((android.graphics.drawable.ColorDrawable) tile.getBackground()).getColor() == Color.BLACK) {
                tile.setBackgroundColor(Color.WHITE);
            } else {
                tile.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private void generarSolucionAleatoria() {
        Random random = new Random();

        // Generate a random number of black tiles (between 7 and 20)
        int blackTilesCount = random.nextInt(14) + 7; // 14 (range) + 7 (minimum) = [7, 20]

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
                tablero[row][col] = tile;
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


    private void reiniciar() {
    }
}