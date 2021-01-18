package com.example.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity2 extends AppCompatActivity {

    private TimePicker heureDepart ;
    private Spinner stationDepart ;
    private Spinner stationArrivee ;
    private Spinner preferenceDeTransport;

    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

    //connect to firebase :
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<String> list  = new ArrayList<>()  ;
        ArrayList<String> listArrivee = new ArrayList<>();
        stationDepart = findViewById(R.id.stationDepart);
        stationArrivee = findViewById(R.id.stationArrivee);

        heureDepart = findViewById(R.id.heureDepart) ;
        heureDepart.setCurrentHour(Calendar.HOUR_OF_DAY);
        Intent intent = getIntent();
        preferenceDeTransport = findViewById(R.id.spinner);
        preferenceDeTransport.setSelection(Integer.parseInt(intent.getStringExtra("preferenceDeTransport")));


        db.collection("Stations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.get("nom")) ;
                                list.add(String.valueOf((document.get("nom"))));

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity2.this,
                                        android.R.layout.simple_spinner_item, list);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                stationDepart.setAdapter(dataAdapter);
                                stationArrivee.setAdapter(dataAdapter);


                            }



                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }





    public void confirmerInfo(View view) {

        if(stationDepart.getSelectedItem() != stationArrivee.getSelectedItem()) {
            Toast.makeText(MainActivity2.this, "De "+ stationDepart.getSelectedItem() + " à "+ stationArrivee.getSelectedItem() , Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity2.this, " §§ Bonne route  !! "  , Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity2.this, "vous êtes deja à " + stationDepart.getSelectedItem() + " !" , Toast.LENGTH_SHORT).show();
        }


    }
}