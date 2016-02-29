package com.doctorfinderapp.doctorfinder;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.adapter.DoctorAdapter;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private DoctorAdapter mAdapter;
    private ArrayList<Doctor> doctors;
    public String CITTA="All";
    public String SPECIALIZZAZIONE="All";
    //static List<ParseObject> DOCTORSMAIN=null;
    static List<ParseObject> USERSMAIN = null;
    private String[] PERMISSIONS=new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    private String TAG= "Main Activity";

    //Parameters shared by fragment goes in activity
    //private static int SIZEM=0;
    private FloatingActionButton fab;
    private LinearLayout selcitta;
    private LinearLayout selcateg;

    ArrayList<String> CITY;
    ArrayList<String> SPECIAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //request permission
        //todo put this in button switch
        if(
            ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED

                &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                MY_PERMISSIONS_REQUEST_LOCATION);
        Log.d(TAG, "Requesting permission " + MY_PERMISSIONS_REQUEST_LOCATION);
        }

        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //set view for doctors visited
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_doctors);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //initialize more Persons
        doctors = new ArrayList<>();
        doctors.add(new Doctor("Giampo", "Giampo",R.drawable.giampa, "Frociologo", true));
        doctors.add(new Doctor("Chiara", "Carboni",R.drawable.chiara, "Tettologa", false));
        doctors.add(new Doctor("Federico", "Bacci",R.drawable.fedebyes, "Ormonologo", true));
        doctors.add(new Doctor("Francesco", "Starna",R.drawable.starnino, "Oculista", true));
        doctors.add(new Doctor("Ginevra", "Lado",R.drawable.p1, "Pediatra", false));
        doctors.add(new Doctor("Giampa", "Giampa",R.drawable.giampa, "Chirurgo", false));

        // specify an adapter
        mAdapter = new DoctorAdapter(doctors);

        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);


        //Dialog for cities
        selcitta = (LinearLayout) findViewById(R.id.select_city_button);
        String[] citta = getResources().getStringArray(R.array.cities);
        CITY = new ArrayList<>();
        final AlertDialog dialogCity = OnCreateDialog("Seleziona Provincia", CITY, citta);
        selcitta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCity.show();
            }
        });

        //Dialog for specialization
        selcateg = (LinearLayout) findViewById(R.id.select_special_button);
        String[] special = getResources().getStringArray(R.array.Specializations);
        SPECIAL = new ArrayList<>();
        final AlertDialog dialogSpecial = OnCreateDialog("Seleziona Categoria", SPECIAL, special);
        selcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSpecial.show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fabmain);
        //fab action results activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Download parse data
                showDataM();


            }
        });

        //Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Set behavior of Navigation drawer  
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public AlertDialog OnCreateDialog(String title, final ArrayList<String> checked, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) checked.add(items[which]);
                        else checked.remove(items[which]);

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        for (int i = 0; i < checked.size(); i++) {
                            Log.d("List " + i + " ----> ", checked.get(i));
                        }
                    }
                }).setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
        
                    }
                });
        return builder.create();
    }

    //respond to toolbar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    //action view search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        SwitchCompat sw = (SwitchCompat) menu.findItem(R.id.switchForActionBar).getActionView().findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Query Localizations TODO AROUND ME
                Toast.makeText(getApplicationContext() ,"Localizzazione " + (isChecked ? "on":"off") ,Toast.LENGTH_SHORT).show();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void selectDrawerItem(MenuItem menuItem){
        switch (menuItem.getItemId()) {

            case R.id.profile:
                break;
            case R.id.gestisci:
                break;
            case R.id.suggerisci:
                break;
            case R.id.about:
                String url_github = "https://github.com/Starnino/Doctor-Finder";
                Intent i_github = new Intent(Intent.ACTION_VIEW);
                i_github.setData(Uri.parse(url_github));
                startActivity(i_github);
                break;
            case R.id.support:
                break;
            case R.id.like:
                String url_face = "https://www.facebook.com/dcfind/?ref=bookmarks";
                Intent i_face = new Intent(Intent.ACTION_VIEW);
                i_face.setData(Uri.parse(url_face));
                startActivity(i_face);
                break;
            case R.id.settings:
                break;
            case R.id.logout:
                ParseUser.logOut();
                Log.d("R", "Logged out");
                Toast.makeText(getApplicationContext(),
                        "Logged out",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
                break;

        }
    }

    //code added to save activity states
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    //download doctors from DB
    //This must be done only here

    public  void showDataM() {
        //list of query
        List<ParseQuery<ParseObject>> queryList = new ArrayList<>();

        //main query to find
        ParseQuery<ParseObject> mainQuery;

        //get query: All doctors
        ParseQuery<ParseObject> allDoctors=ParseQuery.getQuery("Doctor");

        //add list of query per special
        for (int i = 0; i < SPECIAL.size() ; i++) {
            queryList.add(new ParseQuery<>(allDoctors.whereEqualTo("Specialization", SPECIAL.get(i))));
        }

        //put in Or all specialization queries (if necessary)
        if (!queryList.isEmpty()) {
            mainQuery = ParseQuery.or(queryList);
        } else mainQuery = allDoctors;

        //list empty
        queryList.clear();

        //add list of query per city
        for (int i = 0; i < CITY.size() ; i++) {
            queryList.add(new ParseQuery<>(mainQuery.whereEqualTo("Provence", CITY.get(i))));
        }

        //put in OR all city queries (if necessary)
        if (!queryList.isEmpty()) {
            mainQuery = ParseQuery.or(queryList);
        } else mainQuery = allDoctors;

        //progress dialog
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Caricamento... Attendere...", true);

       mainQuery.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {

               if(e==null) {

                   GlobalVariable.DOCTORS = objects;
                   Log.d("Main",GlobalVariable.DOCTORS.toString());


                   Intent intent = new Intent(MainActivity.this,
                           ResultsActivity.class);
                   startActivity(intent);
                   dialog.dismiss();
                   //SIZEM=objects.size();
               }else{


                   Log.d("Main","Error downloading parse data ");
               }
           }
       });

    }

    

}