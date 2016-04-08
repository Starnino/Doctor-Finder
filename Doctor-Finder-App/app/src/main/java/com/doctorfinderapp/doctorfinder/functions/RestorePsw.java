package com.doctorfinderapp.doctorfinder.functions;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doctorfinderapp.doctorfinder.R;

/**
 * Created by giovanni on 4/8/16.
 */
public class RestorePsw extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restorepsw);

        final Button recupero_mail = (Button) findViewById(R.id.invio_recupero_mail);
        recupero_mail.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View view) {

                                              Snackbar.make(recupero_mail, R.string.access_ok, Snackbar.LENGTH_SHORT)
                                                      .setAction("Controlla la tua posta!", null).show();

                                              onBackPressed();

                                          }
                                      }
        );

        Button cancella_dati = (Button) findViewById(R.id.cancella_recupero);
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
