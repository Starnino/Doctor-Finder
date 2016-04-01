package com.doctorfinderapp.doctorfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.doctorfinderapp.doctorfinder.R;

/**
 * Created by giovanni on 4/1/16.
 */
public class ClaimDoctorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim_doctor);

        Button invia_dati = (Button) findViewById(R.id.invia_dati_dottore);
        invia_dati.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View view) {
                                              Toast.makeText(getApplicationContext(), "Grazie per il suggerimento!",
                                                      Toast.LENGTH_LONG).show();

                                              onBackPressed();

                                          }
                                      }
        );

        Button cancella_dati = (Button) findViewById(R.id.cancella_dati);
        cancella_dati.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick(View view) {
                                                      onBackPressed();

                                                   }
                                               }
        );



    }

    @Override
    public void onBackPressed() {

        this.finish();
    }

}
