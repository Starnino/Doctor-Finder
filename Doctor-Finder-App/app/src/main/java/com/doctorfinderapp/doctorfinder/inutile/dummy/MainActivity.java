package com.doctorfinderapp.doctorfinder.inutile.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.MainFragmentActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.parse.ParseUser;


//###Important

/*
Code to change between a fragment and another
Fragment fragment = new CityFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.list_fragment, fragment).commit();
 */

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivitylayout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        Button city_button = (Button) findViewById(R.id.select_city_button);
        city_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainFragmentActivity.class);
                startActivity(intent);
            }});
       /*F Button specalization_button = (Button) findViewById(R.id.select_spec_button);
        specalization_button
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code to show fragment
                        Log.v("MainActivity", "Spec button pressed fragment incoming");

                ragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .show(new SpecialFragment())
                        .commit();*/

                      /*  FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        SpecialFragment f = (SpecialFragment) fm.findFragmentByTag("tag");

                        if(f == null) {  // not added
                            f = new SpecialFragment();
                            ft.add(R.id.SpecialFragment3, f, "tag");
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                        } else {  // already added

                            ft.remove(f);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        }

                        ft.commit();
            }
        });*/



                //drawer Layout
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
            }

            //if back is pressed drawer is closed
            @Override
            public void onBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main2, menu);
                return true;
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

            //this funct is for drawer menu items
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action


                } else if (id == R.id.exit) {

                } else if (id == R.id.logout) {

                    ParseUser.logOut();
                    Log.d("R", "Logged out");
                    Toast.makeText(getApplicationContext(),
                            "Logged out",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intent);

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        }
