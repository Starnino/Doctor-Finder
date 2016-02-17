package com.doctorfinderapp.doctorfinder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.fragment.CityFragment;
import com.doctorfinderapp.doctorfinder.fragment.SpecialFragment;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.parse.ParseUser;


public class MainFragmentActivity extends AppCompatActivity  {

    private DrawerLayout mDrawerLayout;
    public String CITTA="All";
    public String SPECIALIZZAZIONE="All";
    //###Important

/*
Code to change between a fragment and another
Fragment fragment = new CityFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.list_fragment, fragment).commit();
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_start_fragment_list_layout);
        Button specalization_button = (Button) findViewById(R.id.select_city_button);
        specalization_button
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code to show fragment
                        Log.v("Activity", "Spec button pressed fragment incoming");
                        Fragment fragment = new SpecialFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                .replace(R.id.MainFragment, fragment)
                                .commit();

                    }
                });
        Button city_button = (Button) findViewById(R.id.select_spec_button);
        specalization_button
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code to show fragment
                        Log.v("Activity", "City button pressed fragment incoming");
                        Fragment fragment = new CityFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                .replace(R.id.MainFragment, fragment)
                                .commit();
                        // Fragment fragment = new CityFragment();
                        //FragmentManager fm = getFragmentManager();
                        //fm.beginTransaction().replace(R.id.list_fragment, fragment).commit();

                    }
                });



        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Doctor Finder");
        }

        //menu icon
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Set behavior of Navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });


        //funct to show city fragment

    }

    //respond to toolbar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
            /**TODO*/
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    //action view search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    public void selectDrawerItem(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.logout:

                ParseUser.logOut();
                Log.d("R", "Logged out");
                Toast.makeText(getApplicationContext(),
                        "Logged out",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainFragmentActivity.this, SplashActivity.class);
                startActivity(intent);
            case R.id.exit:
               super.finish();
            case R.id.action_settings:
                Intent intent2 = new Intent(MainFragmentActivity.this, ResultsActivity.class);
                startActivity(intent2);
            case R.id.like:
                Intent intent3 = new Intent(MainFragmentActivity.this, DoctorProfileActivity.class);
                startActivity(intent3);

        }
    }

    //code added to save activity state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    //android.app.Fragment fragment = getActivity().getFragmentManager().findFragmentByTag("YOUR_FRAGMENT_TAG");
    //getActivity().getFragmentManager().beginTransaction().hide(fragment);
}