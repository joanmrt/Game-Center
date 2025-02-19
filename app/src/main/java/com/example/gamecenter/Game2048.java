package com.example.gamecenter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Game2048 extends AppCompatActivity implements GestureDetector.OnGestureListener {

    // Primera parte de la matriz es el eje vertical, el segundo es el horizontal
    private Button[][] tablero;
    private Button[][] tableroAnterior;
    private String puntuacionString;
    private int puntuacion;
    private boolean partidaAcabada;
    private int columns = 4;
    private int rows = 4;
    private int textSizeDefault = 20;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_2048);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ASIGNAR VARIABLES Y LISTENERS DE BOTONES
        Button volver = findViewById(R.id.button_volver_2048);
        Button deshacer = findViewById(R.id.stepBack_2048);
        ImageButton restart = findViewById(R.id.reset);
        gestureDetector = new GestureDetector(this, this);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deshacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deshacer();
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
        generarNuevaFicha();
    }

    private void iniciarTablero() {
        TextView textView = findViewById(R.id.puntuacion);
        GridLayout gridLayout = findViewById(R.id.layout2048);
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);
        this.tablero = new Button[rows][columns];
        this.tableroAnterior = new Button[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                this.tablero[row][col] = iniciarBoton(row, col, gridLayout);
            }
        }
    }

    private Button iniciarBoton(int row, int col, GridLayout gridLayout) {
        Button button = new Button(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.width = 270;
        params.height = 270;
        button.setTextSize(textSizeDefault);
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row, 1f);
        params.setGravity(Gravity.CENTER);


        button.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color_0)));
        button.setTextColor(Color.BLACK);
        button.setClickable(false);

        button.setLayoutParams(params);
        gridLayout.addView(button);

        return button;
    }

    private void generarNuevaFicha() {
        boolean ocupado = false;
        while (!ocupado){
            int x = (int) (Math.random() * rows);
            int y = (int) (Math.random() * columns);
            if (this.tablero[x][y].getText().equals("")) {
                this.tablero[x][y].setText("2");
                this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_2));
                ocupado = true;

            } else {
                boolean casillasVacias = comprobarCasillasVacia();
                boolean victoria = comprobarVictoria();

                if (victoria){
                    ganar();
                    ocupado = true;
                } else if (!casillasVacias) {
                    perder();
                    ocupado = true;
                }
            }
        }
    }

    private void ganar() {
        partidaAcabada = true;
    }

    private void perder() {
        partidaAcabada = true;

    }

    // Comprueba si hay alguna casilla vacia aun en el tablero
    private boolean comprobarCasillasVacia() {
        for (int x = 0; x <= tablero.length - 1; x++){
            for (int y = 0; y <= this.tablero.length -1; y++){
                if (this.tablero[x][y].getText().equals("")){
                    return true;
                }
            }

        }

        return false;
    }

    private boolean comprobarVictoria(){
        for (int x = 0; x <= tablero.length - 1; x++){
            for (int y = 0; y <= this.tablero.length -1; y++){
                if (this.tablero[x][y].getText().equals("2048")){
                    return true;
                }
            }

        }

        return false;

    }

    private void reiniciar() {
    }

    private void deshacer() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        if (!partidaAcabada){
            if (Math.abs(velocityX) > Math.abs(velocityY)) {

                if (velocityX > 0) {
                    //Mover a la derecha
                    moverDerecha();
                    generarNuevaFicha();
                } else {
                    //Mover a la izquierda
                    moverIzquierda();
                    generarNuevaFicha();
                }
            } else {
                if (velocityY > 0) {
                    // Mover abajo
                    moverAbajo();
                    generarNuevaFicha();
                } else {
                    // Mover arriba
                    moverArriba();
                    generarNuevaFicha();
                }
            }
        }
        return true;
    }

    private void moverArriba() {
        boolean mover = true;
        // Hacer movimiento mientras haya mas espacio arriba
        while (mover) {
            mover = false;
            // Recorrer matriz entera en busca de fichas que mover
            for (int i = 0; i <= this.tablero.length -2 ; i++) {
                for (int j = 0; j <= this.tablero.length - 1; j++) {
                    // Si se encuentra una ficha, comprobar que a arriba no hay nada
                    if (this.tablero[i][j].getText().equals("") && !this.tablero[i + 1][j].getText().equals("")) {
                        mover = true;
                        // Transferir valor de la casilla de abajo a la de arriba
                        this.tablero[i][j].setText(this.tablero[i + 1][j].getText());
                        // Cambiar el valor de la casilla de abajo a nada
                        this.tablero[i + 1][j].setText("");
                        // Actualizar color de las casillas
                        actualizarColor();
                    }
                }
            }
        }
    }

    private void moverAbajo() {
        boolean mover = true;
        // Hacer movimiento mientras haya mas espacio abajo
        while (mover) {
            mover = false;
            // Recorrer matriz entera en busca de fichas que mover
            for (int i = this.tablero.length - 1; i >= 1; i--) {
                for (int j = 0; j <= this.tablero.length - 1; j++) {
                    // Si se encuentra una ficha, comprobar que a abajo no hay nada
                    if (this.tablero[i][j].getText().equals("") && !this.tablero[i - 1][j].getText().equals("")) {
                        mover = true;
                        // Transferir valor de la casilla de arriba a la de abajo
                        this.tablero[i][j].setText(this.tablero[i - 1][j].getText());
                        // Cambiar el valor de la casilla de arriba a nada
                        this.tablero[i - 1][j].setText("");
                        // Actualizar color de las casillas
                        actualizarColor();
                    }
                }
            }
        }
    }

    private void moverIzquierda() {
        boolean mover = true;
        // Hacer movimiento mientras haya mas espacio a la izquierda
        while (mover) {
            mover = false;
            // Recorrer matriz entera en busca de fichas que mover
            for (int i = 0; i <= this.tablero.length - 1; i++) {
                for (int j = 0; j <= this.tablero.length - 2; j++) {
                    // Si se encuentra una ficha, comprobar que a la izquierda no hay nada
                    if (this.tablero[i][j].getText().equals("") && !this.tablero[i][j + 1].getText().equals("")) {
                        mover = true;
                        // Transferir valor de la casilla derecha a la izquierda
                        this.tablero[i][j].setText(this.tablero[i][j + 1].getText());
                        // Cambiar el valor de la casilla derecha a nada
                        this.tablero[i][j + 1].setText("");
                        // Actualizar color de las casillas
                        actualizarColor();
                    }
                }
            }
        }
    }

    private void moverDerecha() {
        boolean mover = true;
        // Hacer movimiento mientras haya mas espacio a la derecha
        while (mover) {
            mover = false;
            // Recorrer matriz entera en busca de fichas que mover
            for (int i = 0; i <= this.tablero.length - 1; i++) {
                for (int j = this.tablero.length - 1; j >= 1; j--) {
                    // Si se encuentra una ficha, comprobar que a la derecha no hay nada
                    if (this.tablero[i][j].getText().equals("") && !this.tablero[i][j - 1].getText().equals("")) {
                        mover = true;
                        // Transferir valor de la casilla izquierda a la derecha
                        this.tablero[i][j].setText(this.tablero[i][j - 1].getText());
                        // Cambiar el valor de la casilla izquierda a nada
                        this.tablero[i][j - 1].setText("");
                        // Actualizar color de las casillas
                        actualizarColor();
                    } else if (this.tablero[i][j].getText().equals(this.tablero[i][j - 1].getText())) {
                        
                    }
                }
            }
        }
    }

    private void actualizarColor() {
        for (int x = 0; x <= tablero.length - 1; x++){
            for (int y = 0; y <= this.tablero.length -1; y++){
                String valorFicha = String.valueOf(this.tablero[x][y].getText());
                switch (valorFicha){
                    case "2":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_2));
                        break;
                    case "4":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_4));
                        break;
                    case "8":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_8));
                        break;
                    case "16":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_16));
                        break;
                    case "32":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_32));
                        break;
                    case "64":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_64));
                        break;
                    case "128":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_128));
                        break;
                    case "256":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_256));
                        break;
                    case "512":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_512));
                        break;
                    case "1024":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_1024));
                        break;
                    case "2048":
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_2048));
                        break;
                    default:
                        this.tablero[x][y].setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_0));

                }
            }
        }
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }


}