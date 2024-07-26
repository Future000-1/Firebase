package com.example.pan_pan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Inicio_Sesion extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText edtEmail, edtPassword;
    private Button btnRegistro, btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        /**Inicio de Firebase Authentication */
        auth = FirebaseAuth.getInstance();


        /** Vinculacion de elementos de la interfaz*/


        edtEmail = findViewById(R.id.edtEmail2);
        edtPassword=findViewById(R.id.edtPassword2);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnLogin = findViewById(R.id.btnIngresar);

        /** Intencion de los botones*/
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(Inicio_Sesion.this, Registro.class);
             startActivity(intent);
             finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();
            }
        });


    }
    /** Metodo para logear al usuario dependiendo de su tipo de usuario*/

    private void LoginUser() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        /** Inicio de  sesión con Firebase Authentication*/
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {

                        fetchUserData(user.getUid());
                    } else {
                        Toast.makeText(Inicio_Sesion.this, "Error al obtener el usuario actual", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(Inicio_Sesion.this, "Error en la autenticación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /**Accede a la colección 'Usuarios' y al documento correspondiente al ID del usuario*/

        db.collection("Usuarios").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                String nombre = document.getString("name");
                                String tipoUsuario = document.getString("tipoUsuario");

                                Toast.makeText(Inicio_Sesion.this,"Inicio de Sesión Exitoso", Toast.LENGTH_LONG).show();

                                /** Redirigimos segun el tipo de usuario*/

                                switch (tipoUsuario) {
                                    case "Administrador":
                                        startActivity(new Intent(Inicio_Sesion.this, Crear_producto.class));
                                        break;
                                    case "Cliente":
                                        startActivity(new Intent(Inicio_Sesion.this, Perfil_Usuario.class));
                                        break;
                                    default:
                                        Toast.makeText(Inicio_Sesion.this, "Tipo de usuario no reconocido", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                                finish();

                            } else {
                                Toast.makeText(Inicio_Sesion.this, "No se encontraron datos para este usuario", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Inicio_Sesion.this, "Error al obtener datos del usuario ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
