package com.example.pan_pan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Usuario_Administrador extends AppCompatActivity {
    private Button btnUsuario, btnAdministrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_administrador);
        btnUsuario = findViewById(R.id.button_usuario);
        btnAdministrador=findViewById(R.id.button_administrador);

        /** Intent para redigir a la actividad y tipo de usuario cliente*/
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registro.class);
                intent.putExtra("tipoUsuario", "Cliente");
                startActivity(intent);

            }
        });

        /** Intent para redigir a la actividad y tipo de usuario administrador*/
        btnAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent1 = new Intent(getApplicationContext(), Registro.class);
            intent1.putExtra("tipoUsuario", "Administrador");
            startActivity(intent1);
            }
        });

    }
}