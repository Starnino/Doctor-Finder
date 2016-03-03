package com.doctorfinderapp.doctorfinder;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.doctorfinderapp.doctorfinder.Qurami.MainActivityQurami;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.adapter.DoctorAdapter;
import com.doctorfinderapp.doctorfinder.functions.AddDoctors;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
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
    boolean FLAGCITY = false, FLAGSPEC = false;

    //Parameters shared by fragment goes in activity

    private FloatingActionButton fab;
    private LinearLayout selcitta, selcateg;
    private String[] citta, special;
    private ArrayList<String> CITY, SPECIAL;
    private TextView cityText, specialText;
    private Animation fab_open;
    private CardView card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //AddDoctors.addPhoto(getResources());

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

        //set and update recycler view
        updateRecycler();

        //find Text selected
        cityText = (TextView) findViewById(R.id.cities_text_selected);
        specialText = (TextView) findViewById(R.id.special_text_selected);

        //set empty text
        cityText.setText("");
        specialText.setText("");

        //cards
        card2 = (CardView) findViewById(R.id.card2);


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
                if (!FLAGSPEC)  Snackbar.make(v, "Seleziona almeno una Specializzazione!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                else if (!FLAGCITY)  Snackbar.make(v, "Seleziona almeno una Provincia!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                else showDataM();
            }
        });

        //set animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_normal);

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
        //to set logo doc
        //toolbar.setLogo(R.drawable.logotext);

        //set drawer things
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout , toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //add drawer listner not exists
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        //setting header
        //download user image
        ParseUser user=ParseUser.getCurrentUser();
        if(user!=null){
            getUserImage(ParseUser.getCurrentUser());
        View header = navigationView.getHeaderView(0);
        TextView nome= (TextView) header.findViewById(R.id.name_user);

        Log.d("",user.getString("fName"));
        String name =user.getString("fName") ;
           nome.setText(name);
        TextView email= (TextView) header.findViewById(R.id.email_user);

        email.setText(user.getEmail());
        }
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

                                if (checked.size() != 0) FLAGSPEC = true;
                                else FLAGSPEC = false;

                                break;

                            case "Seleziona Provincia":
                                //set text near Provincia when cities are checked
                                if (checked.size() == 1) cityText.setText(checked.get(0));

                                else if (checked.size() == 0) cityText.setText("");

                                else if (checked.size() == 2)
                                    cityText.setText(checked.get(0) + "\ne " + checked.get(1));

                                else if (checked.size() > 0 && checked.size() != 2)
                                    cityText.setText(checked.get(0) + "\ne altre " + (checked.size() - 1));

                                if (checked.size() != 0) FLAGCITY = true;
                                else FLAGCITY = false;

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
                            if (checked.contains(items[i])) continue;
                            checked.add(items[i]);
                        }

                        if (title.equals("Seleziona Categoria")) {
                            specialText.setText("Tutte");
                            FLAGSPEC = true;
                        }

                        else if (title.equals("Seleziona Provincia")) {
                            cityText.setText("Tutte");
                            FLAGCITY = true;
                        }

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

                        if (title.equals("Seleziona Categoria")){
                            specialText.setText("Nessuna");
                            FLAGSPEC = false;
                        }

                        else if (title.equals("Seleziona Provincia")) {
                            cityText.setText("Nessuna");
                            FLAGCITY = false;
                        }
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

        //get query: All doctor
        ParseQuery<ParseObject> doctorsQuery = ParseQuery.getQuery("Doctor");

        //retrieve object with multiple city
        if (CITY.size() != 0 && CITY.size() != citta.length)
            doctorsQuery.whereContainedIn("Province", CITY);

        //retrieve object with multiple city
        if (SPECIAL.size() != 0 && SPECIAL.size() != special.length)
            doctorsQuery.whereContainedIn("Specialization", SPECIAL);

        //order by LastName
        if (CITY.size() != 0 || SPECIAL.size() != 0) {
            doctorsQuery.orderByAscending("LastName");
        }

        //progress dialog
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Caricamento...", true);

       doctorsQuery.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {

               if (e == null) {

                   GlobalVariable.DOCTORS = objects;
                   for (int i = 0; i < GlobalVariable.DOCTORS.size(); i++) {
                       int j = i + 1;
                       Log.d("DOCTOR " + j, " --> " + objects.get(i).get("FirstName") + " " + objects.get(i).get("LastName"));
                   }

                   Intent intent = new Intent(MainActivity.this,
                           ResultsActivity.class);
                   startActivity(intent);
                   dialog.dismiss();

               } else {

                   Log.d("Main", "Error downloading parse data ");
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
                Intent intent_qurami = new Intent(MainActivity.this, MainActivityQurami.class);
                startActivity(intent_qurami);
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


    private void getUserImage(ParseUser user){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPhoto");
        query.whereEqualTo("username", user.getEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userPhoto, ParseException e) {
                //userphoto exists

                if (userPhoto == null) {
                    Log.d("userphoto", "isnull");

                } else {
                    ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                    file.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                //data is the image in array byte
                                //must change image on profile
                                GlobalVariable.UserPropic = BitmapFactory.decodeByteArray(data, 0, data.length);
                                Log.d("Userphoto", "downloaded");

                                RoundedImageView mImg = (RoundedImageView) findViewById(R.id.user_propic);
                                mImg.setImageBitmap(GlobalVariable.UserPropic);
                                //iv.setImageBitmap(bitmap );

                            } else {
                                // something went wrong
                                Log.d("UserPhoto ", "problem download image");
                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onResume() {
        updateRecycler();
        super.onResume();
    }

    public void updateRecycler(){

        //initialize more Persons
        doctors = GlobalVariable.recentDoctors;

        // specify an adapter
        mAdapter = new DoctorAdapter(doctors);

        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);


    }
}