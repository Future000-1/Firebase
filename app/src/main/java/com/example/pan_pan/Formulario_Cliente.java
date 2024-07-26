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

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Formulario_Cliente extends AppCompatActivity {

    private EditText edtName, edtphone;
    private TextView TvEmail;
    private Button btn_confirmar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cliente);

        /**Inicializacion de firebase*/

        auth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        /**Inicializacion de variables*/

        edtName = findViewById(R.id.edt_name);
        TvEmail = findViewById(R.id.edt_correo);
        edtphone = findViewById(R.id.edt_phone);
        btn_confirmar = findViewById(R.id.btn_confirmar);

        /** Metodo para obtener los datos del usuario*/

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            fetchUserData(user.getUid());
        }

        /**Evento para actualizarlos datos*/

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    actualizarInformacion(user.getUid());
                    Intent intent = new Intent(Formulario_Cliente.this, Perfil_Usuario.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    

    /**Metodo para traer y observar los datos del usuario*/

    private void fetchUserData(String userUid) {
        db.collection("Usuarios").document(userUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                String Name = documentSnapshot.getString("name");
                                String Email = documentSnapshot.getString("email");
                                String Phone = documentSnapshot.getString("Telefono");

                                edtName.setText(Name);
                                TvEmail.setText(Email);
                                edtphone.setText(Phone);

                            }else {
                                Toast.makeText(Formulario_Cliente.this, "Datos Actualizados exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Formulario_Cliente.this, "Error al actualizar los datos ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    /** Metodo para actualizar los datos*/

    private void actualizarInformacion(String userId) {

        String nuevoNombre = edtName.getText().toString().trim();
        String nuevoTelefono = edtphone.getText().toString().trim();

        /**Actualizar datos en FireStore*/

        Map<String, Object> updatedDatos = new HashMap<>();
        updatedDatos.put("name", nuevoNombre);
        updatedDatos.put("Telefono", nuevoTelefono);

        db.collection("Usuarios").document(userId).update(updatedDatos)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Formulario_Cliente.this, "Usuario Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Formulario_Cliente.this, "Error al actualizar el perfil en Firestore: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}