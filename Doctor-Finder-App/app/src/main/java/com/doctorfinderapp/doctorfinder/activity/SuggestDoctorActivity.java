package com.doctorfinderapp.doctorfinder.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.doctorfinderapp.doctorfinder.R;

/**
 * Created by giovanni on 4/1/16.
 */
public class SuggestDoctorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_doctor);

        final Button invia_dati = (Button) findViewById(R.id.invia_dati_dottore);
        invia_dati.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View view) {

                                              Snackbar.make(invia_dati, R.string.access_ok, Snackbar.LENGTH_SHORT)
                                                      .setAction("Grazie per il suggerimento!", null).show();

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
