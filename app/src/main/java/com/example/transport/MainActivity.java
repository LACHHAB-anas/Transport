package com.example.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.*;

public class MainActivity extends AppCompatActivity {

    private EditText nom, age, minPrix, maxPrix;
    private RadioGroup genre;
    private Switch handicap;
    private Spinner preferenceDeTransport;

    private ProgressDialog loader;
    //firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();

        nom = findViewById(R.id.nom);
        age = findViewById(R.id.age);
        minPrix = findViewById(R.id.minprix);
        maxPrix = findViewById(R.id.maxprix);
        genre = findViewById(R.id.genre);
        handicap = findViewById(R.id.handicap);
        preferenceDeTransport = findViewById(R.id.spinner);


    }

    public void validerInfo(View view) {

        if(parseInt(valueOf(minPrix.getText())) > parseInt(valueOf(maxPrix.getText()))){
            Toast.makeText(MainActivity.this, "Prix Min > Prix Min !!" , Toast.LENGTH_SHORT).show();

        }


        else if (nom.getText().length() != 0 && minPrix.getText().length() != 0 && maxPrix.getText().length() != 0) {
            loader = new ProgressDialog(this);
            loader.setMessage("Enregistrement des données...");
            loader.setCanceledOnTouchOutside(false);
            loader.show();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("nom", nom.getText().toString().trim());
            user.put("age", age.getText().toString().trim());
            user.put("genre", genre.getCheckedRadioButtonId());
            user.put("handicap", handicap.isChecked());
            user.put("preferenceDeTransport", preferenceDeTransport.getSelectedItem());
            user.put("minPrix", Integer.valueOf(minPrix.getText().toString().trim()));
            user.put("maxPrix", Integer.valueOf(maxPrix.getText().toString().trim()));


// Add a new document with a generated ID
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            loader.dismiss();
                            Toast.makeText(MainActivity.this, "Opération reussite !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("preferenceDeTransport", valueOf(preferenceDeTransport.getSelectedItemPosition()));
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loader.dismiss();
                            Toast.makeText(MainActivity.this, "Operation failed!" + e, Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(MainActivity.this, "Veuillez remplir toute les informations", Toast.LENGTH_SHORT).show();
        }

    }

}
