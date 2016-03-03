package com.doctorfinderapp.doctorfinder.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private final String TAG = "Doctor Maps";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;

    private String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static GoogleMap googleMap;


    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

       // gMap.setMyLocationEnabled(true);
        Boolean u=ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG,u.toString());
        Log.d(TAG,Manifest.permission.ACCESS_COARSE_LOCATION.toString());
        if (u
                &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }
        /**permissionRequest();*/

        /**setUpMap(gMap);*/

    }

    private void permissionRequest(){
        googleMap.setMyLocationEnabled(true);
        if (Build.VERSION.SDK_INT < 23) {
            googleMap.setMyLocationEnabled(true);
            Log.d(TAG,"sdk<23");

        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"permission granted on check");
                googleMap.setMyLocationEnabled(true);

            } else {
                // request permission.
                ActivityCompat.requestPermissions(getActivity(),
                        PERMISSIONS,
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Log.d(TAG,"Requesting permission " + MY_PERMISSIONS_REQUEST_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "request code "+requestCode);
        Log.d(TAG," permissions" +PERMISSIONS.toString());
        Log.d(TAG,"grant results" +grantResults.toString());
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
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    private void setUpMap(GoogleMap gMap) {

        doctors = GlobalVariable.DOCTORS;
        int numMarker = doctors.size();

        // Create a LatLngBounds that includes Australia.
        LatLng ROMA =new LatLng(41.9000, 12.5000);

        //rimpicciolisco il marker
        Bitmap markerSmall = resizeMarker(R.drawable.markermini,100);


        ArrayList<Marker> markers= new ArrayList<Marker>();

        for (int i = 0; i < numMarker; i++) {
            ParseObject DOCTORTHIS = doctors.get(i);
            String curPosition = DOCTORTHIS.get("Marker").toString();
            //String latString=curPosition.substring(6, 15);
            //String lonString=curPosition.substring(22, 31);
            double lat = Double.parseDouble(curPosition.substring(6, 15));
            double lon = Double.parseDouble(curPosition.substring(22, 31));
            String sex="";
            if(DOCTORTHIS.get("Sesso").equals("M"))
                sex="Dott.";
            else
                sex="Dott.ssa";

                Marker currentMarker = gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lon, lat))
                        .title(sex + " " + DOCTORTHIS.get("LastName") + " " + DOCTORTHIS.get("FirstName"))
                        .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

            markers.add(i,currentMarker);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ROMA)      // Sets the center of the map to mi position
                .zoom(2)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public Bitmap resizeMarker(int id,int width){

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), id);
        float aspectRatio = imageBitmap.getWidth() /
                (float) imageBitmap.getHeight();

        int height = Math.round(width / aspectRatio);

        imageBitmap = Bitmap.createScaledBitmap(
                imageBitmap, width, height, false);


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }



}
