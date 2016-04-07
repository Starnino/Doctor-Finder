package com.doctorfinderapp.doctorfinder.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.access.FirstActivity;
import com.doctorfinderapp.doctorfinder.objects.Doctor;
import com.doctorfinderapp.doctorfinder.activity.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.adapter.DoctorAdapter;
import com.doctorfinderapp.doctorfinder.adapter.ResearchAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView, sRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager, sLayoutManager;
    private DrawerLayout mDrawerLayout;
    private DoctorAdapter mAdapter;
    private ResearchAdapter sAdapter;
    private static List<Doctor> doctors;
    private List<String[]> research;
    private String[] PERMISSIONS=new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    private String TAG= "Main Activity";
    private boolean FLAGCITY = true, FLAGSPEC = true;
    private static Context mContext;

    //Parameters shared by fragment goes in activity
    private com.melnykov.fab.FloatingActionButton fab;
    private LinearLayout selcitta, selcateg;
    public static String[] citta, special;
    public static ArrayList<String> CITY, SPECIAL, CITY2, SPECIAL2;
    private TextView cityText, specialText;
    private Animation fab_open;
    private CardView card_recent_doctor, card_recent_doctor_null,
            card_recent_search, card_recent_search_null;
    private NavigationView navigationView;
    public ProgressBar progress;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        View parentLayout = findViewById(R.id.drawer_main);

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
        }else{

            Snackbar.make(parentLayout, R.string.good_login, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
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
        //updateRecyclerDoctor();

        //find Text selected
        cityText = (TextView) findViewById(R.id.cities_text_selected);
        specialText = (TextView) findViewById(R.id.special_text_selected);

        //set empty text
        cityText.setText("Tutte");
        specialText.setText("Tutte");

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

        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fabmain);
        //fab action results activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Download parse data
                if (!FLAGSPEC)
                    Snackbar.make(v, "Seleziona almeno una Specializzazione!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                else if (!FLAGCITY)  Snackbar.make(v, "Seleziona almeno una Provincia!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                else if(!Util.isOnline(getApplicationContext()))
                    Snackbar.make(v, R.string.connection_control, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                else {
                    progress.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(MainActivity.this,
                            ResultsActivity.class);
                    intent.putExtra("RESEARCH", false);
                    startActivity(intent);

                    setLinear(specialText, cityText);
                    //Log.d("SET LINEAR --> ", CITY + "");
                }
            }
        });

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

        //add drawer listener not exists
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        //setting header
        //download user image
        ParseUser user=ParseUser.getCurrentUser();
        setProfileInformation(user);
    }

    public void setProfileInformation(ParseUser user){
        if(user != null
                //&& GlobalVariable.SEMAPHORE
                ){
            getUserImage(ParseUser.getCurrentUser());
            //after 2 sec re set image if is the first time

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    getUserImage(ParseUser.getCurrentUser());
                }};
            Timer timer = new Timer();
            timer.schedule(task, 2000);
            View header = navigationView.getHeaderView(0);
            TextView nome = (TextView) header.findViewById(R.id.name_user);
            String name = user.getString("fName") ;
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
                            //Log.d("List " + i + " ----> ", checked.get(i));
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
                        //Log.d("List isEmpty? --> ", "is " + checked.isEmpty());
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
                if(ParseUser.getCurrentUser()!=null
                        //&& GlobalVariable.SEMAPHORE
                        ){
                    Intent intent_user = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(intent_user);
                }
                else Snackbar.make(mDrawerLayout, R.string.effettua_login, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;

            case R.id.inserisci_dottore:
                Intent intent_dottore = new Intent(this, WebViewActivity.class);

                Bundle dottore = new Bundle();
                dottore.putString("URL",
                        GlobalVariable.URLDoctorForm );
                intent_dottore.putExtras(dottore);
                startActivity(intent_dottore);
                break;

            case R.id.support:
                Util.sendFeedbackMail(MainActivity.this);
                break;

            case R.id.like:
                Intent intent_like = Util.getOpenFacebookIntent(this);

                startActivity(intent_like);
                break;

            case R.id.logout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("R", "Logged out");
                        Toast.makeText(getApplicationContext(),
                                "Logged out",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                break;

            case R.id.informativa:
                Intent informativa = new Intent(MainActivity.this, Informativa.class);
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
        if(GlobalVariable.UserPropic==null) {
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

    }


    @Override
    protected void onResume() {
        //update image
        if (ParseUser.getCurrentUser() != null) {
            setProfileInformation(ParseUser.getCurrentUser());
        }
        updateRecyclerDoctor();
        updateRecentSearch();
        super.onResume();
    }

    //method tu update DoctorRecycler
    public void updateRecyclerDoctor(){

        ParseQuery<ParseObject> recentSearch = ParseQuery.getQuery("recentDoctor");
        recentSearch.orderByDescending("DATE");
        recentSearch.fromLocalDatastore();
        recentSearch.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {

                        if (objects.size() > 9) {
                            try {
                                objects.get(9).unpin();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            doctors = doctors.subList(0, 9);
                        }

                        card_recent_doctor_null.setVisibility(View.INVISIBLE);
                        card_recent_doctor.setVisibility(View.VISIBLE);
                        doctors = Util.transformList(objects);

                        mAdapter = new DoctorAdapter(doctors);

                        mRecyclerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.doctor_item_height);
                        card_recent_doctor.getLayoutParams().height = (int) getResources().getDimension(R.dimen.doctor_item_height);
                        //set adapter to recycler view
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }
        });
    }

    //method to update Linear recycler
    public void updateRecentSearch(){

        ParseQuery<ParseObject> recentSearch = ParseQuery.getQuery("recentSearch");
        recentSearch.orderByDescending("DATE");
        recentSearch.fromLocalDatastore();
        recentSearch.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() != 0) {

                        if (objects.size() > 9) {
                            try {
                                objects.get(9).unpin();

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            research = research.subList(0, 9);
                        }

                        card_recent_search_null.setVisibility(View.INVISIBLE);
                        card_recent_search.setVisibility(View.VISIBLE);
                        research = Util.transformSearch(objects);

                        //specify adapter
                        sAdapter = new ResearchAdapter(research);
                        sRecyclerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.linear_recycler);
                        //set adapter to recycler view
                        sRecyclerView.setAdapter(sAdapter);
                    }
                }
            }
        });
    }

    //method to add Linear to recycler
    public void setLinear(TextView s, TextView c){
        String special = s.getText().toString();
        String city = c.getText().toString();
        final ParseObject recentSearch = new ParseObject("recentSearch");
        recentSearch.put("SPEC", special);
        recentSearch.put("CITY", city);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        recentSearch.put("DATE", simpleDateFormat.format(Calendar.getInstance().getTime()));
        ParseQuery<ParseObject> pin = ParseQuery.getQuery("recentSearch");
        pin.fromLocalDatastore();
        pin.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                boolean flag = true;
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i).getString("SPEC").equals(recentSearch.getString("SPEC"))
                            && objects.get(i).getString("CITY").equals(recentSearch.getString("CITY")))
                                flag = false;
                }
                if (flag)
                    try {
                        recentSearch.pin();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
            }
        });
    }

    public void profile_click(View v){

        if (ParseUser.getCurrentUser()!=null){
            Intent intent_user = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent_user);
        }
    }

    public static void research(List<String> specialization, List<String> city, Context mContext){

        if (city.size() == 1 && city.get(0).equals("Tutte"))
            CITY2 = new ArrayList<>(Arrays.asList(citta));

        else CITY2 = new ArrayList<>(city);

        if (city.size() == 1 && specialization.get(0).equals("Tutte"))
            SPECIAL2 = new ArrayList<>(Arrays.asList(special));

        else SPECIAL2 = new ArrayList<>(specialization);

        Intent intent = new Intent(mContext, ResultsActivity.class);
        intent.putExtra("RESEARCH", true);
        mContext.startActivity(intent);
    }
}