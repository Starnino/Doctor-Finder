package com.doctorfinderapp.doctorfinder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.access.FirstActivity;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.AddDoctors;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private PersonAdapter mAdapter;
    private ArrayList<Person> persons;
    public String CITTA="All";
    public String SPECIALIZZAZIONE="All";
    //static List<ParseObject> DOCTORSMAIN=null;
    static List<ParseObject> USERSMAIN = null;

    //Parameters shared by fragment goes in activity
    //private static int SIZEM=0;
    private FloatingActionButton fab;


    private LinearLayout selcitta;
    private LinearLayout selcateg;


    ArrayList<String> selected_city;
    ArrayList<String> selected_special;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
        persons = new ArrayList<>();
        persons.add(new Person("Dott. Francesco", R.drawable.p1));
        persons.add(new Person("Dott. Federico", R.drawable.p2));
        persons.add(new Person("Dott.ssa Giampa", R.drawable.p3));
        persons.add(new Person("Dott.Vincenzo", R.drawable.p4));
        persons.add(new Person("Dott.ssa Chiara", R.drawable.p5));
        persons.add(new Person("Dott. Giampo", R.drawable.giampa));

        // specify an adapter
        mAdapter = new PersonAdapter(persons);

        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);


        //Dialog for cities
        selcitta = (LinearLayout) findViewById(R.id.select_city_button);
        String[] citta = getResources().getStringArray(R.array.cities);
        selected_city = new ArrayList<>();
        final AlertDialog dialogCity = OnCreateDialog("Seleziona Provincia", selected_city, citta);
        selcitta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCity.show();
            }
        });

        //Dialog for specialization
        selcateg = (LinearLayout) findViewById(R.id.select_special_button);
        String[] special = getResources().getStringArray(R.array.Specializations);
        selected_special = new ArrayList<>();
        final AlertDialog dialogSpecial = OnCreateDialog("Seleziona Categoria", selected_special, special);
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
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Doctor");
        //progress dialog
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Caricamento. Attendere...", true);

        //query.
        //query.whereEqualTo("Citta",NOMECITTA);//per starna
        //query.whereEqualTo("Specializzazione",NOMESPECIALIZZAZIONE)
       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {

               if(e==null) {

                   GlobalVariable.DOCTORS = objects;
                   Log.d("Main",GlobalVariable.DOCTORS.toString());


                   Intent intent = new Intent(MainActivity.this,
                           ResultsActivity.class);
                   startActivity(intent);
                   //SIZEM=objects.size();
               }else{


                   Log.d("Main","Error downloading parse data ");
               }
           }
       });

    }

}