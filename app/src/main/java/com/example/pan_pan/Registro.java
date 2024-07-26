package com.example.pan_pan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private EditText nombre, email, password, phone;
    private Button btnRegistrar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView crear_cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        /**Inicio de Firebase Authentication y Firestore*/

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        /** Vinculacion de elementos de la interfaz*/
        nombre = findViewById(R.id.edtNombre);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        phone = findViewById(R.id.edtPhone);
        crear_cuenta = findViewById(R.id.tienes_cuenta);


        /**En caso de no estar registrado*/

        crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Inicio_Sesion.class);
                startActivity(intent);
                finish();
            }
        });


        /** Obtener datos del usuario*/
        
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                
            }
        });
        
        
        
    }
    /**Obtención de datos y verificacion de campos*/

    private void registerUser() {
        String Nombre= nombre.getText().toString().trim();
        String Email= email.getText().toString().trim();
        String Contrasena= password.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String tipoUsuario = getIntent().getStringExtra("tipoUsuario");


        if (Nombre.isEmpty() || Email.isEmpty() || Contrasena.isEmpty() || Phone.isEmpty()){
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tipoUsuario == null) {
            Toast.makeText(this, "El tipo de usuario es nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        /** Creacion de usuario en Firebase Authentication y Firestore*/

        auth.createUserWithEmailAndPassword(Email, Contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null){
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", Nombre);
                        userData.put("email", Email);
                        userData.put("contraseña", Contrasena);
                        userData.put("Telefono", Phone);
                        userData.put("tipoUsuario", tipoUsuario);

                        db.collection("Usuarios")
                                .document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Registro.this, "Usuario Registrado con Exito", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        nombre.setText("");
                        email.setText("");
                        password.setText("");
                        phone.setText("");

                        Intent intent = new Intent(Registro.this, Inicio_Sesion.class);
                        startActivity(intent);
                        finish();


                    }
                }else {
                    Toast.makeText(Registro.this, "Error al registrar al usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
