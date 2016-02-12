package com.doctorfinderapp.doctorfinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doctorfinderapp.doctorfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DoctorMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private static final String TAG = "Doctor Maps";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        // Create a LatLngBounds that includes Australia.
         LatLngBounds AUSTRALIA = new LatLngBounds(
                new LatLng(-44, 113), new LatLng(-10, 154));

    // Set the camera to the greatest possible zoom level that includes the
    // bounds
        final Marker marker = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        Marker pinDoctor= gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
    }

    @Override
    public void onResume() {
        super.onResume();
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




    }
}
