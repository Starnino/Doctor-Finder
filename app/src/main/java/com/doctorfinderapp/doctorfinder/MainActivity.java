package com.doctorfinderapp.doctorfinder;


import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    EditText editsearch;
    String[] name;
    String[] special;
    String[] feedback;
    ArrayList<Doctors> arraylist = new ArrayList<Doctors>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate sample data
        name = new String[] { "Lorenzo Gitto", "Marco dell'Aquila", "Francesco Maiese", "Elena Dezi", "Wassim Arleo", "Marco Frison", "Mirim Renati", "Alexandra Zanni", "Stefania Bianchin", "Michele Benevento", "Francesco Silla", "Milena D'Arco", "Enzo Gatta", "Sofia Mancini", "Marco La Casa", "Antonio Russo" };

        special = new String[] { "Dermatologist", "Cardiologist", "Dentist",
                "Dermatologist", "Medical Geneticist", "Dentist", "Dentist", "Cardiologist", "Dermatologist", "Dermatologist", "Cardiologist", "Allergist","Medical Geneticist","Allergist","Cardiologist","Medical Geneticist"  };

        feedback = new String[] { "**", "****", "*", "***", "****", "**", "*", "***", "***", "****", "****", "**", "*", "*", "***", "****" };

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < name.length; i++)
        {
            Doctors wp = new Doctors(name[i], special[i],
                    feedback[i]);
            // Binds all strings into an array
            arraylist.add(wp);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }
}

