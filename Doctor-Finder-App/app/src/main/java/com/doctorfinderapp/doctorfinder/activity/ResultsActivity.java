package com.doctorfinderapp.doctorfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.fragment.DoctorListFragment;
import com.doctorfinderapp.doctorfinder.fragment.DoctorMapsFragment;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuItem.OnMenuItemClickListener{

    private static final String TAG ="Results Activity" ;
    private DrawerLayout mDrawerLayout;
    private static ViewPager viewPager;
    private static TabLayout tabs;
    private static com.melnykov.fab.FloatingActionButton fab, fab_location;
    private MenuItem searchItem;
    private MenuItem filterItem;
    private SearchView searchView;
    private static Context c;
    private NavigationView navigationView;
    private static View coordinator;
    MaterialDialog dialog;
    RadioGroup radioGroup;
    RadioGroup group_mode;
    public static boolean research;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=getApplicationContext();


        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        setContentView(R.layout.activity_results);


        coordinator = findViewById(R.id.coordinator_results);


        //find fab buttons
        fab_location = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab_location);
        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title("Suggerisci uno specialitsta")
                        .content("Consigliaci uno specialista che vorresti fosse su questa applicazione!")
                        .inputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                        .input("Nome, Cognome, Email, Numero, \nSpecializzazione", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                Log.d("INPUT", input.toString());
                                String body = "";
                                if (ParseUser.getCurrentUser() != null)
                                     body += "Email Utente: " + ParseUser.getCurrentUser().getEmail() + "\n";

                                body += "Dottore Suggerito: " + input.toString();

                                BackgroundMail.newBuilder(fab.getContext())
                                        .withUsername("doctor.finder.dcf@gmail.com")
                                        .withPassword("quantomacina")
                                        .withMailto("info@doctorfinderapp.com")
                                        .withSubject("SUGGERIMENTO DOTTORE")
                                        .withBody(body)
                                        .send();

                                Util.SnackBarFiga(fab, v, "Grazie per il suggerimento!");
                            }
                        })
                        .iconRes(R.drawable.doctor_avatar)
                        .maxIconSizeRes(R.dimen.null_card)
                        .positiveText("Invia")
                        .negativeText("Annulla")
                        .widgetColor(getResources().getColor(R.color.colorPrimaryDark))
                        .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                        .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                        .show();
            }
        });

        //onClick button
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

               /* DoctorMapsFragment.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Snackbar snackbar = Snackbar
                                .make(v, "Cercando la tua posizione", Snackbar.LENGTH_LONG);

                        snackbar.show();s
                        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                           // Toast.makeText(getApplicationContext(), "GPS is disabled!", Toast.LENGTH_SHORT).show();

                            Snackbar snackbar2 = Snackbar
                                    .make(v, "GPS is disabled! ", Snackbar.LENGTH_LONG);
                            snackbar2.show();


                        }
                        return false;
                    }
                });*/
            }
        });

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Give the TabLayout
        tabs = (TabLayout) findViewById(R.id.tabs);


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(toolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_results);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_results);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //setProfileInformation(user);

        setupViewPager(viewPager);
        //download from db

        research = getIntent().getExtras().getBoolean("RESEARCH");

        if (research)
            showDatafromAdapter();

        else showDataM();

    }


      public void setProfileInformation(ParseUser user){
        if (user != null) {
            getUserImage(user);
            View header = navigationView.getHeaderView(0);
            TextView nome = (TextView) header.findViewById(R.id.name_user);

            if (user.getString("fName") != null)
                nome.setText(user.getString("fName"));

            TextView email = (TextView) header.findViewById(R.id.email_user);

            if (user.getEmail() != null)
                email.setText(user.getEmail());
            //Re -set image

            if (GlobalVariable.UserPropic != null) {
                //Log.d("ciao", GlobalVariable.UserPropic.toString());
                RoundedImageView mImg = (RoundedImageView) header.findViewById(R.id.user_propic);
                mImg.setImageBitmap(GlobalVariable.UserPropic);
            }
        }
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorListFragment(), "Lista");
        adapter.addFragment(new DoctorMapsFragment(), "Mappa");


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchFAB(position);
                switchMenuItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });

        dialog = new MaterialDialog.Builder(this)
                .title("Ordina Ricerca")
                .positiveText("Cerca")
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                .negativeText("annulla")
                .customView(R.layout.filter_view, true)
                .build();

        //DoctorListFragment.orderList(mode, grow);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.group_order);
        group_mode = (RadioGroup) dialog.findViewById(R.id.order_mode);

        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);
        fab.show();

    }

    @Override
    protected void onResume() {
        if (ParseUser.getCurrentUser() != null)
            setProfileInformation(ParseUser.getCurrentUser());

        super.onResume();
    }

    //search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        filterItem = menu.findItem(R.id.action_filter);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final List<ParseObject> doctorsFilter = filter(GlobalVariable.DOCTORS, newText);
                    DoctorListFragment.refreshDoctors(doctorsFilter);
                    return false;
                }
            });

        filterItem.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_filter){

            dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String mode = "feedback";
                    boolean grow = false;
                    if (!DoctorListFragment.ifNullAdapter()) {
                        switch (radioGroup.getCheckedRadioButtonId()) {

                            case R.id.feedback:
                                mode = "feedback";
                                break;

                            case R.id.prezzo:
                                mode = "prezzo";
                                break;

                            case R.id.cognome:
                                mode = "cognome";
                                break;
                        }

                        switch (group_mode.getCheckedRadioButtonId()) {

                            case R.id.crescente:
                                grow = true;
                                break;

                            case R.id.decrescente:
                                grow = false;
                                break;
                        }

                        DoctorListFragment.orderList(mode, grow);
                    }
                }
            }).show();

            return true;


        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {

            case R.id.profile:
                if(ParseUser.getCurrentUser() != null){
                    Intent intent_user = new Intent(this, UserProfileActivity.class);
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
                Util.sendFeedbackMail(ResultsActivity.this);
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
                        Intent intent = new Intent(ResultsActivity.this ,  SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                break;

            case R.id.informativa:
                Intent informativa = new Intent(this, Informativa.class);
                startActivity(informativa);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    static class Adapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //switch fab
    public void switchFAB(int position){
        switch(position){
            case 0:
                Log.d("fab", "open");
                //fab_location.startAnimation(fab_close);
                //fab_location.setClickable(false);
                fab.show();
                fab.setClickable(true);
                break;

            case 1:
                Log.d("fab_location", "open");
                fab.hide();
                fab.setClickable(false);
                //fab_location.startAnimation(fab_open_normal);
                //fab_location.setClickable(true);
                break;
        }
    }

    public void switchMenuItem(int position){
        if (position == 1) {
            searchItem.setVisible(false);
            searchView.setVisibility(View.INVISIBLE);
            filterItem.setVisible(false);
        }
        else {
            searchItem.setVisible(true);
            searchView.setVisibility(View.VISIBLE);
            filterItem.setVisible(true);
        }
    }


    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            GlobalVariable.DOCTORS=new ArrayList<>();
            super.onBackPressed();
        }

        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    //download doctors from DB


    public static void showDataM() {

        //get query: All doctor
        ParseQuery<ParseObject> doctorsQuery = ParseQuery.getQuery("Doctor");

        //retrieve object with multiple city

        if (MainActivity.CITY.size() != 0 && MainActivity.CITY.size() != MainActivity.citta.length)
            doctorsQuery.whereContainedIn("Province", MainActivity.CITY);

        //Log.d("RESULTS SEARCH FOR --> ", MainActivity.CITY + "");

        //retrieve object with multiple city
        if (MainActivity.SPECIAL.size() != 0 && MainActivity.SPECIAL.size() != MainActivity.special.length)
            doctorsQuery.whereContainedIn("Specialization", MainActivity.SPECIAL);

        //order by LastName
        if (MainActivity.CITY.size() != 0 || MainActivity.SPECIAL.size() != 0) {
            doctorsQuery.orderByAscending("LastName");
        }
        //refresh.setRefreshing(true);

        doctorsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                //refresh.setRefreshing(false);
                DoctorListFragment.stopRefresh();



                if (e == null) {

                    GlobalVariable.DOCTORS = objects;
                    String found = GlobalVariable.DOCTORS.size() + "";
                    if (objects.size() == 1)
                        found += " specialista trovato";
                    else found += " specialisti trovati";

                    Util.SnackBarFiga(fab, coordinator, found);


                    Util.dowloadDoctorPhoto(GlobalVariable.DOCTORS);
                    DoctorListFragment.refreshDoctors(GlobalVariable.DOCTORS);
                    DoctorListFragment.setProgressBar(View.GONE);

                    viewPager.getAdapter().notifyDataSetChanged();
                    if (objects.size()==0){
                        DoctorListFragment.CardNothingVisible();
                    }

                    //Log.d(TAG,"DOCTORS.size() "+GlobalVariable.DOCTORS.size());
                } else {

                }
            }
        });

    }

    public List<ParseObject> filter(List<ParseObject> Doctors,String query){
        query = query.toLowerCase();
        final List<ParseObject> filteredModelList = new ArrayList<>();
        for (ParseObject doctor : Doctors) {
            final String textName = doctor.getString("FirstName").toLowerCase();
            final String textSurname = doctor.getString("LastName").toLowerCase();
            if (textSurname.startsWith(query) || textName.startsWith(query)) {
                //Log.d("QUERY: " + query + "--> ", textSurname);
                filteredModelList.add(doctor);
            }
        }
        return filteredModelList;
    }

    public void profile_click(View v){
        if(ParseUser.getCurrentUser()!=null){
            Intent intent_user = new Intent(ResultsActivity.this, UserProfileActivity.class);
            startActivity(intent_user);
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

    public static void showDatafromAdapter() {

        //get query: All doctor
        ParseQuery<ParseObject> doctorsQuery = ParseQuery.getQuery("Doctor");

        //retrieve object with multiple city
        if (MainActivity.CITY2.size() != 0 && MainActivity.CITY2.size() != MainActivity.citta.length)
            doctorsQuery.whereContainedIn("Province", MainActivity.CITY2);


        //retrieve object with multiple city
        if (MainActivity.SPECIAL2.size() != 0 && MainActivity.SPECIAL2.size() != MainActivity.special.length)
            doctorsQuery.whereContainedIn("Specialization", MainActivity.SPECIAL2);

        //order by LastName
        if (MainActivity.CITY2.size() != 0 || MainActivity.SPECIAL2.size() != 0) {
            doctorsQuery.orderByAscending("LastName");
        }

        doctorsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    GlobalVariable.DOCTORS = objects;

                    String found = GlobalVariable.DOCTORS.size() + "";
                    if (objects.size() == 1)
                        found += " specialista trovato";
                    else found += " specialisti trovati";

                    Util.SnackBarFiga(fab, coordinator, found);

                    Util.dowloadDoctorPhoto(GlobalVariable.DOCTORS);
                    DoctorListFragment.refreshDoctors(GlobalVariable.DOCTORS);
                    DoctorListFragment.setProgressBar(View.GONE);

                    viewPager.getAdapter().notifyDataSetChanged();
                    if (objects.size()==0){
                        DoctorListFragment.CardNothingVisible();
                    }

                }
            }
        });

    }

}



