package com.doctorfinderapp.doctorfinder.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.doctorfinderapp.doctorfinder.Doctor.Doctor;
import com.doctorfinderapp.doctorfinder.R;
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

    private static final String TAG = "Doctor Maps";
    private GoogleMap googleMap;


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



        setUpMap(gMap);

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

        //rimpicciolisco il marker
        Bitmap markerSmall= resizeMarker(R.drawable.markermini,100);

        //creo lista dottori
        ArrayList<Doctor> arraydoctors = new ArrayList<Doctor>();

        //Creo lista Marker per prova
        ArrayList<Marker> markers= new ArrayList<>();



        final Marker marker1 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.923, 12.2300))
                .title("Hello world1")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));
        final Marker marker2 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.83, 12.2500))
                .title("Hello world2")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker3 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8803922, 12.5398233))
                .title("Hello world3")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        final Marker marker4 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8771792, 12.469348))
                .title("Hello world4")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));
        final Marker marker5 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8947128, 12.4848708))
                .title("Hello world5")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));
        final Marker marker6 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.9192198, 12.4671846))
                .title("Hello world6")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));
        final Marker marker7 = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.8803922, 12.5398233))
                .title("Hello world7")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));

        //fine creazione di marker

        //prova 1
        final Marker marker = gMap.addMarker(new MarkerOptions()
                .position(ROMA)
                .title("Hello world")
                .icon(BitmapDescriptorFactory.fromBitmap(markerSmall)));




        //animate camera

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
