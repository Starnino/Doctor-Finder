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

import java.util.ArrayList;

public class DoctorMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

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



        //permissionRequest();

        setUpMap(gMap);

    }

    private  void permissionRequest(){
        //gMap.setMyLocationEnabled(true);
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
                Log.d(TAG,"Requesting permission "+MY_PERMISSIONS_REQUEST_LOCATION);
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

        /*googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
                startActivity(intent);
            }
        });*/

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




        // Create a LatLngBounds that includes Australia.


        LatLng ROMA =new LatLng(41.9000, 12.5000);

        //rimpicciolisco il marker
        Bitmap markerSmall= resizeMarker(R.drawable.markermini,100);



        //Creo lista Marker per prova
        ArrayList<Marker> markers= new ArrayList<>();


        final Marker marker1 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.922278, 12.488333))
                .title("Dott. De Cillis")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker2 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.908255, 12.517306))
                .title("Dott.ssa Tritto")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker3 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.860814, 12.483226))
                .title("Dott.ssa Salminto")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker4 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8771792, 12.469348))
                .title("Dott. Gitto")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker5 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8947128, 12.4848708))
                .title("Dott.ssa Canossa")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker6 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.9192198, 12.4671846))
                .title("Dott.ssa Dezi")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker7 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8803922, 12.5398233))
                .title("Dott. Maiese")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        //fine creazione di marker



        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ROMA)      // Sets the center of the map to mi position
                .zoom(10)                   // Sets the zoom
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
