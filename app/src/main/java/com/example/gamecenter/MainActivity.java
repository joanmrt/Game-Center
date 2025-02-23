package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    UserOpenHelper userOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginButton = findViewById(R.id.btn_login);
        TextView registerView = findViewById(R.id.tv_signup);
        userOpenHelper = new UserOpenHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        boolean userExists;
        userExists = checkUser();
        if (userExists){
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkUser() {
        EditText usuarioEditText = findViewById(R.id.et_username);
        EditText passwordEditText = findViewById(R.id.et_password);
        TextView resultView = findViewById(R.id.tv_result);

        String usuarioString = usuarioEditText.getText().toString().trim();
        String contrasenaString = passwordEditText.getText().toString().trim();


        if (usuarioString.isEmpty() || contrasenaString.isEmpty()) {
            resultView.setText("Username and password cannot be empty.");
            return false;
        }

        User nuevoUsuario = new User(usuarioString, contrasenaString);
        boolean encontrado = userOpenHelper.findUser(nuevoUsuario);

        if (!encontrado) {
            resultView.setText("User not found.");
        }

        return encontrado;
    }
}