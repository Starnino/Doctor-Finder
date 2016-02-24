package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.parse.ParseObject;

public class UserProfileActivity extends AppCompatActivity {

    private  AlertDialog.Builder alert;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //scrolling
        setContentView(R.layout.activity_scrolling_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get parameters from activity
        Bundle b = getIntent().getExtras();
        String id = b.getString("id");

        //creazione parseobject user
        ParseObject user=new ParseObject("User");

        int i=0;
            if(MainActivity.USERSMAIN.get(i).getObjectId()==id){
                Log.d("User", "object id " + MainActivity.USERSMAIN.get(i).getObjectId());
                Log.d("User","id "+id);
                user= MainActivity.USERSMAIN.get(i);
            }

        //floating button for report problems
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(UserProfileActivity.this);
                alert.setTitle("Hai bisogno di aiuto?");
                alert.setMessage("Se stai riscontrando problemi con il tuo profilo, o " +
                        "hai problemi con un dottore, manda un email a info@doctorfinderapp.com" +
                        " spiegando il tuo problema. Saremo a tua disposizione per aiutarti!");
                alert.setPositiveButton("Ho capito", null);
                alert.setIcon(R.drawable.ic_info_white_24dp);
                alert.show();
            }
        });


        }
    }
