package com.doctorfinderapp.doctorfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.doctorfinderapp.doctorfinder.adapter.ResearchAdapter;
import com.doctorfinderapp.doctorfinder.functions.AddDoctors;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView, sRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager, sLayoutManager;
    private DrawerLayout mDrawerLayout;
    private DoctorAdapter mAdapter;
    private ResearchAdapter sAdapter;
    private List<Doctor> doctors;
    private ArrayList<String[]> research = new ArrayList<>();
    static List<ParseObject> USERSMAIN = null;
    private String[] PERMISSIONS=new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    private String TAG= "Main Activity";
    public static final int FAB_OPEN_TIME = 1500;
    private boolean FLAGCITY = false, FLAGSPEC = false;

    //Parameters shared by fragment goes in activity

    private FloatingActionButton fab;
    private LinearLayout selcitta, selcateg;
    public static String[] citta, special;
    public static ArrayList<String> CITY, SPECIAL;
    private TextView cityText, specialText;
    private Animation fab_open;
    private CardView card_recent_doctor, card_recent_doctor_null,
            card_recent_search, card_recent_search_null;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //aggiungo le foto dei dottori
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

        if (ParseUser.getCurrentUser() == null) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Avviso importante")
                    .setContentText("Non usare questa applicazione in caso di emergenza!")
                    .setConfirmText("OK, ho capito")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog
                                    .setTitleText("Confermato")
                                    .setContentText("In caso di emergenza chiama sempre prima i soccorsi!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();
        }

        //set view for doctors visited
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_doctors);
        sRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_research);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        sRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sRecyclerView.setLayoutManager(sLayoutManager);

        //cards
        card_recent_doctor = (CardView) findViewById(R.id.card_doctors);
        card_recent_doctor_null = (CardView) findViewById(R.id.recent_doctors_null);
        card_recent_search = (CardView) findViewById(R.id.card_research);
        card_recent_search_null = (CardView) findViewById(R.id.recent_search_null);

        //set recycler doctors continuously
        updateRecyclerDoctor();

        //find Text selected
        cityText = (TextView) findViewById(R.id.cities_text_selected);
        specialText = (TextView) findViewById(R.id.special_text_selected);

        //set empty text
        cityText.setText("");
        specialText.setText("");


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
                /*TODO REMOVE COMMENT if (!FLAGSPEC)  Snackbar.make(v, "Seleziona almeno una Specializzazione!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                else if (!FLAGCITY)  Snackbar.make(v, "Seleziona almeno una Provincia!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();*/

                //else {
                    //showDataM();
                    Intent intent = new Intent(MainActivity.this,
                            ResultsActivity.class);
                    startActivity(intent);
                    GlobalVariable.FLAG_CARD_SEARCH_VISIBLE = true;
                    setLinear(specialText, cityText);
                //}
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
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("DoctorFinder");

        }


        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Doctor Finder");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255,255, 255)); //Color of your title


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

            //Log.d("dice di non usare i cazzo di log vuoti",user.getString("fName"));
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

                        if (title.equals("Seleziona Categoria")) {
                            specialText.setText("Nessuna");
                            FLAGSPEC = false;
                        } else if (title.equals("Seleziona Provincia")) {
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


    //code added to save activity states
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.profile:
                if(ParseUser.getCurrentUser()!=null){
                Intent intent_user = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent_user);}
                break;

            case R.id.Qurami:
                Intent intent_qurami = new Intent(MainActivity.this, MainActivityQurami.class);
                startActivity(intent_qurami);
                break;

            case R.id.inserisci_dottore:
                Intent intent_dottore = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle dottore = new Bundle();
                dottore.putString("URL",
                        "https://docs.google.com/forms/d/181fRG5ppgIeGdW6VjJZtXz3joc3ldIfCunl58GPcxi8/edit?usp=sharing" ); //Your id
                intent_dottore.putExtras(dottore);
                startActivity(intent_dottore);
                break;

            case R.id.about:
                Intent intent_about = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle about = new Bundle();
                about.putString("URL",
                        "https://github.com/Starnino/Doctor-Finder/blob/master/README.md" ); //Your id
                intent_about.putExtras(about);
                startActivity(intent_about);
                break;

            case R.id.support:
                Intent intent_supporto = new Intent(MainActivity.this, WebViewActivity.class);

                Bundle supporto = new Bundle();
                supporto.putString("URL",
                        "https://docs.google.com/forms/d/1qEf-MEshVbQAtGlmjehQi88D2bEklCuuETe7Gz9Xb80/prefill" );
                intent_supporto.putExtras(supporto);
                startActivity(intent_supporto);
                break;

            case R.id.like:
                Intent intent_like = Util.getOpenFacebookIntent(this);

                startActivity(intent_like);
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

            case R.id.informativa:
                Intent informativa = new Intent(MainActivity.this, Report_Feedback.class);
                startActivity(informativa);
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
        //update image



        updateRecyclerDoctor();
        updateRecentSearch();
        super.onResume();
    }

    //method tu update DoctorRecycler
    public void updateRecyclerDoctor(){
        if (GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE) {
            card_recent_doctor_null.setVisibility(View.INVISIBLE);
            card_recent_doctor.setVisibility(View.VISIBLE);
            GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE = true;
        }
        //initialize more Persons
        doctors = GlobalVariable.recentDoctors;
        // specify an adapter
        mAdapter = new DoctorAdapter(doctors);
        if (doctors.size() != 0) {
            mRecyclerView.getLayoutParams().height = 280;
            card_recent_doctor_null.getLayoutParams().height = 280;
        }
        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);
    }

    //method to update Linear recycler
    public void updateRecentSearch(){
        if (GlobalVariable.FLAG_CARD_SEARCH_VISIBLE) {
            card_recent_search_null.setVisibility(View.INVISIBLE);
            card_recent_search.setVisibility(View.VISIBLE);
            GlobalVariable.FLAG_CARD_SEARCH_VISIBLE = true;
        }

        //specify adapter
        sAdapter = new ResearchAdapter(research);
        if (research.size() != 0) sRecyclerView.getLayoutParams().height = 400;
        //set adapter to recycler view
        sRecyclerView.setAdapter(sAdapter);
    }

    //method to add Linear to recycler
    public void setLinear(TextView s, TextView c){
        String special = s.getText().toString();
        String city = c.getText().toString();
        String[] linear = {special, city};
        boolean flag = true;

        for (int i = 0; i < research.size() ; i++) {
            if (research.get(i)[0].equals(linear[0]) && research.get(i)[1].equals(linear[1])){
                flag = false;
            }

        }
        //if linear not exist add it
        if (flag){

            if (research.size() < 10) research.add(linear);
            else {
                research.add(0, linear);
                research.remove(10);
            }
        }
    }

}