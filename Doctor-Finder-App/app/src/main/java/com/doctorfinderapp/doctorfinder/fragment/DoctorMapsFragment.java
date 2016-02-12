package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doctorfinderapp.doctorfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapsInitializer;

public class DoctorMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private static final String TAG = "Doctor Maps";


    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);



        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        /*S
        MAPFragment=mapFragment;
        if (mapFragment==null) {
            Log.v("gmap","Google map is null");
        }*/
        /*return view;




    }*/



    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap=gMap;

        if (gMap==null) {
            Log.v("gmap","Google map is null");
        }


        gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(41.9000, 12.5000))
        );
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.9000, 12.5000))      // Sets the center of the map to mi position
                .zoom(5)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //setUpMap(gMap);

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
    private void setUpMap(GoogleMap gMap){
        // Create a LatLngBounds that includes Australia.


        LatLng ROMA =new LatLng(41.9000, 12.5000);
        //prova 1
        final Marker marker = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        //prova2
        final Marker marker2 = gMap.addMarker(new MarkerOptions()
                .position(ROMA)
                .title("Hello world")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        Marker pinDoctor= gMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .position(new LatLng(10, 10))
        );

        gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(41.9000, 12.5000))
        );






    }
}
