package com.example.pan_pan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Perfil_Usuario extends AppCompatActivity {
    private TextView name_cliente, email_cliente, phone_cliente;
    private Button btn_actualizar, btn_cerrar_sesion;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        /** Inicializacion de firebase*/

        auth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        /** Inicializacion de variables*/

        name_cliente = findViewById(R.id.user_name);
        email_cliente = findViewById(R.id.user_email);
        phone_cliente = findViewById(R.id.user_phone);
        btn_actualizar = findViewById(R.id.btn_actualizar_info);
        btn_cerrar_sesion = findViewById(R.id.btncerrarSesion);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil_Usuario.this, Formulario_Cliente.class);
                startActivity(intent);
                finish();
            }
        });

        /** Evento para cerrar sesion*/
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent1 = new Intent(Perfil_Usuario.this, Inicio_Sesion.class);
                startActivity(intent1);
                finish();
            }
        });




        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            fetchUserData(user.getUid());
        }

    }


    /**Metodo para Traer los datos del usuario*/

    private void fetchUserData(String uid) {
    db.collection("Usuarios").document(uid).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()){
                       DocumentSnapshot documentSnapshot = task.getResult();
                       if (documentSnapshot.exists()){
                           String Name = documentSnapshot.getString("name");
                           String Email = documentSnapshot.getString("email");
                           String Phone = documentSnapshot.getString("Telefono");

                           name_cliente.setText(Name);
                           email_cliente.setText(Email);
                           phone_cliente.setText(Phone);

                       }else {
                           Toast.makeText(Perfil_Usuario.this, "No se encontraron los datos para este usuario", Toast.LENGTH_SHORT).show();
                       }
                   }else {
                       Toast.makeText(Perfil_Usuario.this, "Error al obtener datos del usuario: ", Toast.LENGTH_SHORT).show();
                   }
                }
            });

    }
}