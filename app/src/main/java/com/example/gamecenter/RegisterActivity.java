package com.example.gamecenter;

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

public class RegisterActivity extends AppCompatActivity {
    private UserOpenHelper userOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerButton = findViewById(R.id.btn_register);
        userOpenHelper = new UserOpenHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar() {
        EditText usuarioEditText = findViewById(R.id.et_username_register);
        EditText passwordEditText = findViewById(R.id.et_password_register);
        EditText passwordRepeatEditText = findViewById(R.id.et_password2_register);
        TextView resultView = findViewById(R.id.tv_register_result);

        String usuarioString = usuarioEditText.getText().toString().trim();
        String contrasenaString = passwordEditText.getText().toString().trim();
        String contrasenaRepetidaString = passwordRepeatEditText.getText().toString().trim();

        if (usuarioString.isEmpty() || contrasenaString.isEmpty() || contrasenaRepetidaString.isEmpty()) {
            resultView.setText("All fields must be filled.");
            return;
        }

        // Check if passwords match
        if (!contrasenaString.equals(contrasenaRepetidaString)) {
            resultView.setText("The passwords do not match.");
            return;
        }

        // Register the user
        User nuevoUsuario = new User(usuarioString, contrasenaString);
        userOpenHelper.addUser(nuevoUsuario);
        resultView.setText("User successfully registered.");
    }
}