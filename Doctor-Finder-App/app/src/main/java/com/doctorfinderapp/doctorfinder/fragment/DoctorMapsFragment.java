package com.doctorfinderapp.doctorfinder.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class DoctorMapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static List<ParseObject> doctors;

    private final String TAG = "Doctor Maps";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    public static Resources mResources;

    private String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public static GoogleMap googleMap;
    private Bitmap MarkerSmall;


    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;


        //rimpicciolisco il marker
        // MarkerSmall = resizeMarker(R.drawable.markermini,100);

        mResources = getResources();
        if (mResources == null) {
            Log.d("mappa", "mResourcesis null");
        }

        // gMap.setMyLocationEnabled(true);
        Boolean u = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, u.toString());
        Log.d(TAG, Manifest.permission.ACCESS_COARSE_LOCATION.toString());
        if (u
                &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }
        permissionRequest();

        //setUpMap(gMap);
        //setup map

        setUpMap();

    }

    private void permissionRequest() {
        //googleMap.setMyLocationEnabled(true);
        if (Build.VERSION.SDK_INT < 23) {
            googleMap.setMyLocationEnabled(true);
            Log.d(TAG, "sdk<23");

        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission granted on check");
                googleMap.setMyLocationEnabled(true);

            } else {
                // request permission.
                ActivityCompat.requestPermissions(getActivity(),
                        PERMISSIONS,
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Log.d(TAG, "Requesting permission " + MY_PERMISSIONS_REQUEST_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "request code " + requestCode);
        Log.d(TAG, " permissions" + PERMISSIONS.toString());
        Log.d(TAG, "grant results" + grantResults.toString());
        switch (requestCode) {


            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);

                    }

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            getMapAsync(this);
            //setUpMap();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setUpMapIfNeeded();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //Fedebyes Funct
    public void setUpMap() {

        GoogleMap gMap = googleMap;


        gMap.getUiSettings().setMapToolbarEnabled(true);

        doctors = GlobalVariable.DOCTORS;
        int numMarker = doctors.size();


        LatLng ROMA = new LatLng(41.9000, 12.5000);

        //rimpicciolisco il marker
        Bitmap markerSmall = resizeMarker(R.drawable.markermini, 100);


        ArrayList<Marker> markers = new ArrayList<Marker>();

        for (int i = 0; i < numMarker; i++) {
            ParseObject DOCTORTHIS = doctors.get(i);
            String curPosition = DOCTORTHIS.get("Marker").toString();
            Log.d("mapp",DOCTORTHIS.getString("FirstName"));
            Log.d("lat", curPosition.substring(6, 15));
            Log.d("long", curPosition.substring(22, 31));
            double lat = Double.parseDouble(curPosition.substring(6, 15));
            double lon = Double.parseDouble(curPosition.substring(22, 31));
            ArrayList<String> spec = (ArrayList<String>) DOCTORTHIS.get("Specialization");
            Util.setSpecialization(spec);
//
            String sex = "";
            if (DOCTORTHIS.get("Sesso").equals("M"))
                sex = "Dott.";
            else
                sex = "Dott.ssa";

            String id = Integer.toString(i);
            Marker currentMarker = gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                            //.title(sex + " " + DOCTORTHIS.get("LastName") + " " + DOCTORTHIS.get("FirstName"))
                    .title(id)

                    .icon(BitmapDescriptorFactory.fromBitmap(markerSmall))
                    .snippet(Util.setSpecialization(spec)));
            markers.add(i, currentMarker);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ROMA)      // Sets the center of the map to mi position
                .zoom(5)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        gMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){

                                              @Override
                                              public void onInfoWindowClick(Marker marker) {
                                                  String id= marker.getTitle();
                                                  int index= parseInt(id);



                                                  Context context = getContext();
                                                  Intent intent = new Intent(context, DoctorActivity.class);
                                                  //------
                                                  intent.putExtra("index", index);
                                                  //------
                                                  context.startActivity(intent);
                                                  Log.d("mappa","infowindow3 clicked");
                                              }
                                          }
        );


    }

    public Bitmap resizeMarker(int id, int width) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), id);
        if (imageBitmap == null) {
            Log.d("mappa", "imagebitmapis null");
        }
        float aspectRatio = imageBitmap.getWidth() /
                (float) imageBitmap.getHeight();

        int height = Math.round(width / aspectRatio);

        imageBitmap = Bitmap.createScaledBitmap(
                imageBitmap, width, height, false);


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this.getContext(), "Info window clicked",
                Toast.LENGTH_SHORT).show();
        Log.d("mappa","infowindow clicked");
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            //LayoutInflater li = (LayoutInflater)getContext();
            myContentsView = getLayoutInflater(null).inflate(R.layout.custom_info_windows, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

           /* TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
            */

            String id= marker.getTitle();
            int index= parseInt(id);

            TextView name = (TextView) myContentsView.findViewById(R.id.name);
            TextView special = (TextView) myContentsView.findViewById(R.id.special);
            RatingBar ratingBar = (RatingBar) myContentsView.findViewById(R.id.ratingBar);
            TextView city = (TextView) myContentsView.findViewById(R.id.city);
            RoundedImageView profile = (RoundedImageView) myContentsView.findViewById(R.id.profile_image);


            ParseObject CURRENTDOCTOR=GlobalVariable.DOCTORS.get(index);

            name.setText(CURRENTDOCTOR.getString("FirstName") + " " + CURRENTDOCTOR.getString("LastName"));
            ArrayList<String> spec = (ArrayList<String>) CURRENTDOCTOR.get("Specialization");
            special.setText(Util.setCity(spec));
            ratingBar.setRating(parseFloat(CURRENTDOCTOR.getString("Feedback")));
            //todo set photo



            return myContentsView;
        }


    }


}




