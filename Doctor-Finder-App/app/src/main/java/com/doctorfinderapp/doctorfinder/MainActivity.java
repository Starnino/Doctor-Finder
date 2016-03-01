package com.doctorfinderapp.doctorfinder;

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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.adapter.DoctorAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private DoctorAdapter mAdapter;
    private ArrayList<Doctor> doctors;
    static List<ParseObject> USERSMAIN = null;
    private String[] PERMISSIONS=new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    private String TAG= "Main Activity";
    public static final int FAB_OPEN_TIME = 1500;

    //Parameters shared by fragment goes in activity
    //private static int SIZEM=0;
    private FloatingActionButton fab;
    private LinearLayout selcitta, selcateg;
    private String[] citta, special;
    private ArrayList<String> CITY, SPECIAL;
    private TextView cityText, specialText;
    private Animation fab_open;

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
        doctors.add(new Doctor("Giampo", "Giampo",R.drawable.giampa, "Sessuologo", true));
        doctors.add(new Doctor("Chiara", "Carboni",R.drawable.chiara, "Tettologa", false));
        doctors.add(new Doctor("Federico", "Bacci",R.drawable.fedebyes, "Ormonologo", true));
        doctors.add(new Doctor("Francesco", "Starna", R.drawable.starnino, "Oculista", true));
        doctors.add(new Doctor("Ginevra", "Lado",R.drawable.p1, "Pediatra", false));
        doctors.add(new Doctor("Giampa", "Giampa",R.drawable.giampa, "Chirurgo", false));

        // specify an adapter
        mAdapter = new DoctorAdapter(doctors);

        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);

        //find Text selected
        cityText = (TextView) findViewById(R.id.cities_text_selected);
        specialText = (TextView) findViewById(R.id.special_text_selected);
        cityText.setText("Nessuna");
        specialText.setText("Nessuna");

        //Dialog for cities
        selcitta = (LinearLayout) findViewById(R.id.select_city_button);
        citta = getResources().getStringArray(R.array.cities);
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
        special = getResources().getStringArray(R.array.Specializations);
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

        //set animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);

        //start animation
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fab.startAnimation(fab_open);
                    }
                });

            }
        }, FAB_OPEN_TIME);

        //Drawer settings
        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout , toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout .setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public AlertDialog OnCreateDialog(final String title, final ArrayList<String> checked, final String[] items){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) checked.add(items[which]);
                        else checked.remove(items[which]);

                        switch (title) {
                            case "Seleziona Categoria":
                                //set text near Categoria when cities are checked
                                if (checked.size() == 1) specialText.setText(checked.get(0));

                                else if (checked.size() == 0) specialText.setText("");

                                else if (checked.size() == 2)
                                    specialText.setText(checked.get(0) + "\ne " + checked.get(1));

                                else if (checked.size() > 0 && checked.size() != 2)
                                    specialText.setText(checked.get(0) + "\ne altre " + (checked.size() - 1));

                                break;

                            case "Seleziona Provincia":
                                //set text near Provincia when cities are checked
                                if (checked.size() == 1) cityText.setText(checked.get(0));

                                else if (checked.size() == 0) cityText.setText("");

                                else if (checked.size() == 2)
                                    cityText.setText(checked.get(0) + "\ne " + checked.get(1));

                                else if (checked.size() > 0 && checked.size() != 2)
                                    cityText.setText(checked.get(0) + "\ne altre " + (checked.size() - 1));

                                break;
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        for (int i = 0; i < checked.size(); i++) {
                            Log.d("List " + i + " ----> ", checked.get(i));
                        }
                    }
                }).setNeutralButton("tutte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < items.length; i++) {
                            ((AlertDialog) dialog).getListView().setItemChecked(i, true);
                            checked.add(items[i]);
                        }

                        if (title.equals("Seleziona Categoria"))
                            specialText.setText("Tutte");
                        else if (title.equals("Seleziona Provincia"))
                            cityText.setText("Tutte");

                        for (int i = 0; i < checked.size(); i++) {
                            Log.d("List " + i + " ----> ", checked.get(i));
                        }
                    }
                }).setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < items.length; i++) {
                            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                        }

                        if (title.equals("Seleziona Categoria"))
                            specialText.setText("Nessuna");
                        else if (title.equals("Seleziona Provincia"))
                            cityText.setText("Nessuna");

                        checked.clear();
                        Log.d("List isEmpty? --> ", "is " + checked.isEmpty());
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
        /*SwitchCompat sw = (SwitchCompat) menu.findItem(R.id.switchForActionBar).getActionView().findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Query Localizations TODO AROUND ME
                Toast.makeText(getApplicationContext() ,"Localizzazione " + (isChecked ? "on":"off") ,Toast.LENGTH_SHORT).show();
            }
        });*/
        return super.onCreateOptionsMenu(menu);
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

        //get query: All doctor
        ParseQuery<ParseObject> allDoctors = ParseQuery.getQuery("Doctor");

        //main query
        ParseQuery mainQuery = ParseQuery.getQuery("DoctorNull"); /** E VUOTA*/

        //retrieve object with multiple city
        if (CITY.size() != 0)
            allDoctors.whereContainedIn("Provence", CITY);

        //retrieve object with multiple city
        if (SPECIAL.size() != 0)
            allDoctors.whereContainedIn("Specialization", SPECIAL);

        //order by LastName
        if (CITY.size() != 0 || SPECIAL.size() != 0) {
            mainQuery = allDoctors.orderByAscending("LastName");
        }

        //progress dialog
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Caricamento...", true);

       mainQuery.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {

               if(e==null) {

                   GlobalVariable.DOCTORS = objects;
                   for (int i = 0; i < objects.size(); i++) {
                       int j = i+1;
                       Log.d("DOCTOR " + j, " --> " + objects.get(i).get("FirstName") +" "+ objects.get(i).get("LastName"));
                   }

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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.profile:
                Intent intent_user = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent_user);
                break;

            case R.id.gestisci:
                break;

            case R.id.inserisci_dottore:
                Intent intent2 = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle b = new Bundle();
                b.putString("URL",
                        "https://docs.google.com/forms/d/181fRG5ppgIeGdW6VjJZtXz3joc3ldIfCunl58GPcxi8/edit?usp=sharing" ); //Your id
                intent2.putExtras(b);
                startActivity(intent2);
                break;

            case R.id.about:
                Intent intent3 = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle b3 = new Bundle();
                b3.putString("URL",
                        "https://github.com/Starnino/Doctor-Finder/blob/master/README.md" ); //Your id
                intent3.putExtras(b3);
                startActivity(intent3);
                /*String url_github = "https://github.com/Starnino/Doctor-Finder/blob/master/README.md";
                Intent i_github = new Intent(Intent.ACTION_VIEW);
                i_github.setData(Uri.parse(url_github));
                startActivity(i_github);*/
                break;

            case R.id.support:
                String url_support = "https://docs.google.com/forms/d/1qEf-MEshVbQAtGlmjehQi88D2bEklCuuETe7Gz9Xb80/edit?usp=sharing";
                Intent i_support = new Intent(Intent.ACTION_VIEW);
                i_support.setData(Uri.parse(url_support));
                startActivity(i_support);
                break;

            case R.id.like:
                Intent intent4 = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle b4 = new Bundle();
                b4.putString("URL",
                        "https://www.facebook.com/dcfind" ); //Your id
                intent4.putExtras(b4);
                startActivity(intent4);
                /*String url_face = "https://www.facebook.com/dcfind/?ref=bookmarks";
                Intent i_face = new Intent(Intent.ACTION_VIEW);
                i_face.setData(Uri.parse(url_face));
                startActivity(i_face);*/
                break;

            case R.id.settings:
                Intent intent_settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent_settings);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}