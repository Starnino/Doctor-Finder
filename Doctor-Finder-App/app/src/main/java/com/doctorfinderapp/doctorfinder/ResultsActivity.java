package com.doctorfinderapp.doctorfinder;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.doctorfinderapp.doctorfinder.Qurami.MainActivityQurami;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.fragment.DoctorListFragment;
import com.doctorfinderapp.doctorfinder.fragment.DoctorMapsFragment;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.google.android.gms.maps.GoogleMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private static ViewPager viewPager;
    private static TabLayout tabs;
    private boolean isFabOpen = false;
    private FloatingActionButton fab, fab_location;
    private Animation fab_open_normal,fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //adding doctors data
        //AddDoctors.addData();
        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //ParseUser currentUser = ParseUser.getCurrentUser();

        //download from db
        showDataM();

        setContentView(R.layout.activity_results);

        //find fab buttons
        fab_location = (FloatingActionButton) findViewById(R.id.fab_location);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //load animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab_open_normal = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_normal);

        //onClick button
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoctorMapsFragment.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Toast.makeText(getApplicationContext(), "GPS is disabled!", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_results);
        navigationView.setNavigationItemSelectedListener(this);

        //setting header

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {

            View header = navigationView.getHeaderView(0);
            TextView nome = (TextView) header.findViewById(R.id.name_user);

            Log.d("", user.getString("fName"));
            String name = user.getString("fName");
            nome.setText(name);
            TextView email = (TextView) header.findViewById(R.id.email_user);

            email.setText(user.getEmail());
            //Re -set image

            if (GlobalVariable.UserPropic != null) {
                Log.d("ciao", GlobalVariable.UserPropic.toString());
                RoundedImageView mImg = (RoundedImageView) header.findViewById(R.id.user_propic);
                mImg.setImageBitmap(GlobalVariable.UserPropic);
            }
        }
    }


    // Add Fragments to Tabs------------------------------------------------------------------------
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        fab.startAnimation(fab_open_normal);
    }


    //search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItem filterItem = menu.findItem(R.id.action_filter);

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

        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.profile:
                Intent intent_user = new Intent(ResultsActivity.this, UserProfileActivity.class);
                startActivity(intent_user);
                break;

            case R.id.Qurami:
                Intent intent_qurami = new Intent(ResultsActivity.this, MainActivityQurami.class);
                startActivity(intent_qurami);
                break;

            case R.id.inserisci_dottore:
                Intent intent_dottore = new Intent(ResultsActivity.this, WebViewActivity.class);

                Bundle dottore = new Bundle();
                dottore.putString("URL",
                        "https://docs.google.com/forms/d/181fRG5ppgIeGdW6VjJZtXz3joc3ldIfCunl58GPcxi8/edit?usp=sharing" ); //Your id
                intent_dottore.putExtras(dottore);
                startActivity(intent_dottore);
                break;

            case R.id.about:
                Intent intent_about = new Intent(ResultsActivity.this, WebViewActivity.class);

                Bundle about = new Bundle();
                about.putString("URL",
                        "https://github.com/Starnino/Doctor-Finder/blob/master/README.md" ); //Your id
                intent_about.putExtras(about);
                startActivity(intent_about);
                break;

            case R.id.support:
                Intent intent_supporto = new Intent(ResultsActivity.this, WebViewActivity.class);

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

            case R.id.settings:
                break;

            case R.id.logout:
                ParseUser.logOut();
                Log.d("R", "Logged out");
                Toast.makeText(getApplicationContext(),
                        "Logged out",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResultsActivity.this, SplashActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    static class Adapter extends FragmentPagerAdapter {

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


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }//---------------------------------------------------------------------------------------------


    //switch fab
    public void switchFAB(int position){
        switch(position){
            case 0:
                Log.d("fab", "open");
                fab_location.startAnimation(fab_close);
                fab_location.setClickable(false);
                fab.startAnimation(fab_open_normal);
                fab.setClickable(true);
                break;

            case 1:
                Log.d("fab_location", "open");
                fab.startAnimation(fab_close);
                fab.setClickable(false);
                fab_location.startAnimation(fab_open_normal);
                fab_location.setClickable(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    //download doctors from DB

    public void showDataM() {

        //get query: All doctor
        ParseQuery<ParseObject> doctorsQuery = ParseQuery.getQuery("Doctor");

        //retrieve object with multiple city
        if (MainActivity.CITY.size() != 0 && MainActivity.CITY.size() != MainActivity.citta.length)
            doctorsQuery.whereContainedIn("Province", MainActivity.CITY);

        //retrieve object with multiple city
        if (MainActivity.SPECIAL.size() != 0 && MainActivity.SPECIAL.size() != MainActivity.special.length)
            doctorsQuery.whereContainedIn("Specialization", MainActivity.SPECIAL);

        //order by LastName
        if (MainActivity.CITY.size() != 0 || MainActivity.SPECIAL.size() != 0) {
            doctorsQuery.orderByAscending("LastName");
        }

        //progress dialog
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Caricamento");
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.docfinder));
        dialog.show();

        doctorsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    GlobalVariable.DOCTORS = objects;
                    for (int i = 0; i < GlobalVariable.DOCTORS.size(); i++) {
                        int j = i + 1;
                        Log.d("DOCTOR " + j, " --> " + objects.get(i).get("FirstName") + " " + objects.get(i).get("LastName"));
                    }

                    dialog.cancel();
                    setupViewPager(viewPager);

                    Toast.makeText(getApplicationContext(), GlobalVariable.DOCTORS.size() + " specialisti trovati", Toast.LENGTH_LONG).show();

                } else {
                    dialog.cancel();
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Qualcosa è andato storto!")
                            .show();
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
                Log.d("QUERY: " + query + "--> ", textSurname);
                filteredModelList.add(doctor);
            }
        }
        return filteredModelList;
    }
}



