package com.doctorfinderapp.doctorfinder.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
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
import java.util.HashMap;
import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class DoctorMapsFragment extends SupportMapFragment

{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 122;
    public static Resources mResources;
    public static GoogleMap googleMap;
    private static List<ParseObject> doctors;
    private final String TAG = "Doctor Maps";
    private String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private Bitmap MarkerSmall;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                gMap.getUiSettings().setMapToolbarEnabled(true);
                setUpMap(googleMap);
                permissionRequest();
                LatLng ROMA = new LatLng(41.9000, 12.5000);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ROMA)      // Sets the center of the map to mi position
                        .zoom(5)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    /*@Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;



        Boolean u = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (u && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }


        setUpMap(gMap);
        gMap.getUiSettings().setMapToolbarEnabled(true);

        //permissionRequest();
    }*/

    private void permissionRequest() {
        //googleMap.setMyLocationEnabled(true);
        if (Build.VERSION.SDK_INT < 23) {
            if(googleMap!=null)googleMap.setMyLocationEnabled(true);
            Log.d(TAG, "sdk<23");
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission granted on check");
                if(googleMap!=null)googleMap.setMyLocationEnabled(true);
            } else {
                // request permission.
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, MY_PERMISSIONS_REQUEST_LOCATION);
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        if(googleMap!=null)googleMap.setMyLocationEnabled(true);
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();

    }

    private  void setUpMapIfNeeded() {
        if (googleMap == null) {
            getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gMap) {
                    googleMap = gMap;
                    setUpMap(googleMap);
                    gMap.getUiSettings().setMapToolbarEnabled(true);
                    permissionRequest();

                }
            });
        }
    }




    //-->Fedebyes Funct<--//
    public void setUpMap(GoogleMap gMap) {
        gMap.getUiSettings().setMapToolbarEnabled(true);
        //gMap.getUiSettings().setMyLocationButtonEnabled(false);

        doctors = GlobalVariable.DOCTORS;
        int numMarker = doctors.size();


        //rimpicciolisco il marker
        Bitmap markerSmall = resizeMarker(R.drawable.markermini, 40);

        ArrayList<String> spec;
        ArrayList<HashMap> position;
        String[][] latLong;


        ArrayList<Marker> markers = new ArrayList<Marker>();

        for (int i = 0; i < numMarker; i++) {
            ParseObject DOCTORTHIS = doctors.get(i);

            position = (ArrayList<HashMap>) DOCTORTHIS.get("Marker");
            latLong = Util.setPosition(position);
            //double lat = Double.parseDouble(curPosition.substring(6, 15));double lon = Double.parseDouble(curPosition.substring(22, 31));-->OLD<--
            spec = (ArrayList<String>) DOCTORTHIS.get("Specialization");
            Util.setSpecialization(spec);

            String sex = "";
            if (DOCTORTHIS.get("Sesso").equals("M"))
                sex = "Dott.";
            else
                sex = "Dott.ssa";

            double lat = 41.9000;
            double lon = 12.5000;

            String id = Integer.toString(i);

            if (latLong.length == 1) {
                lat = Double.parseDouble(latLong[0][0]);
                lon = Double.parseDouble(latLong[0][1]);
                Marker currentMarker = gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                                //.title(sex + " " + DOCTORTHIS.get("LastName") + " " + DOCTORTHIS.get("FirstName"))
                        .title(id)
                        .icon(BitmapDescriptorFactory.fromBitmap(markerSmall))
                        .snippet(Util.setSpecialization(spec)));
                markers.add(i, currentMarker);
            } else {
                for (int index = 0; index < latLong.length; index++) {
                    lat = Double.parseDouble(latLong[index][0]);
                    lon = Double.parseDouble(latLong[index][1]);
                    Marker currentMarker = gMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            //.title(sex + " " + DOCTORTHIS.get("LastName") + " " + DOCTORTHIS.get("FirstName"))
                            .title(id)
                            .icon(BitmapDescriptorFactory.fromBitmap(markerSmall))
                            .snippet(Util.setSpecialization(spec)));
                    markers.add(i, currentMarker);
                }

            }

        }


        gMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                                              @Override
                                              public void onInfoWindowClick(Marker marker) {
                                                  String id = marker.getTitle();
                                                  int index = parseInt(id);
                                                  Context context = getContext();
                                                  Intent intent = new Intent(context, DoctorActivity.class);
                                                  //------
                                                  intent.putExtra("index", index);
                                                  //------
                                                  context.startActivity(intent);
                                                  //Log.d("mappa", "infowindow3 clicked");
                                              }
                                          }
        );


        gMap.getUiSettings().setMapToolbarEnabled(true);

    }

    public float convertDpToPixel(float dp, Activity context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public Bitmap resizeMarker(int id, int width) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), id);
        if (imageBitmap == null) {
            //Log.d("mappa", "imagebitmapis null");
        }
        float aspectRatio = imageBitmap.getWidth() /
                (float) imageBitmap.getHeight();

        int height = Math.round(width / aspectRatio);

        /*imageBitmap = Bitmap.createScaledBitmap(
                imageBitmap, width, height, false);*/


        //Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, ((int) convertDpToPixel(width,getActivity())), ((int) convertDpToPixel(height,getActivity())), false);
       // ((int) convertDpToPixel(75))


        return resizedBitmap;
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private boolean isShowed=false;

        MyInfoWindowAdapter() {
            //LayoutInflater li = (LayoutInflater)getContext();
            myContentsView = getLayoutInflater(null).inflate(R.layout.custom_info_windows, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }


        @Override
        public View getInfoContents(final Marker marker) {


            String id = marker.getTitle();
            int index = parseInt(id);

            TextView name = (TextView) myContentsView.findViewById(R.id.name);
            TextView special = (TextView) myContentsView.findViewById(R.id.special);
            RatingBar ratingBar = (RatingBar) myContentsView.findViewById(R.id.ratingBar);
            TextView city = (TextView) myContentsView.findViewById(R.id.city);
            final RoundedImageView profile = (RoundedImageView) myContentsView.findViewById(R.id.profile_image_info);


            final ParseObject CURRENTDOCTOR = GlobalVariable.DOCTORS.get(index);

            name.setText(CURRENTDOCTOR.getString("FirstName") + " " + CURRENTDOCTOR.getString("LastName"));
            ArrayList<String> spec = (ArrayList<String>) CURRENTDOCTOR.get("Specialization");
            special.setText(Util.setCity(spec));
            ratingBar.setRating(parseFloat(CURRENTDOCTOR.get("Feedback").toString()));

            byte[] photo=GlobalVariable.DOCTORPHOTO.get(index);
            if(photo.length>0){
                profile.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
            }else{
                Drawable myDrawable = ContextCompat.getDrawable(getContext(), R.drawable.doctor_avatar);

                profile.setImageDrawable(myDrawable);

            }



               /* final ParseQuery<ParseObject> doctorph = ParseQuery.getQuery("DoctorPhoto");
                doctorph.whereEqualTo("Email", CURRENTDOCTOR.get("Email").toString());

                doctorph.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject doctorPhoto, ParseException e) {

                        if (doctorPhoto == null) {
                            Log.d("doctorphoto", CURRENTDOCTOR.get("Email").toString() + " isNull");
                            Drawable myDrawable = ContextCompat.getDrawable(getContext(), R.drawable.doctor_avatar);

                            profile.setImageDrawable(myDrawable);

                        } else {
                            Log.d("doctorphoto", CURRENTDOCTOR.get("Email").toString() + " exists");
                            ParseFile file = (ParseFile) doctorPhoto.get("profilePhoto");
                            if (e == null) {

                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {
                                            Log.d("doctorphoto", CURRENTDOCTOR.get("Email").toString() + " downloaded");

                                            profile.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                                            if (marker != null && marker.isInfoWindowShown()) {

                                                marker.hideInfoWindow();
                                                marker.showInfoWindow();

                                            }
                                        } else {
                                            Log.d("doctorphoto", CURRENTDOCTOR.get("Email").toString() + " exception" + e.toString());
                                        }


                                    }
                                });
                            }
                        }
                    }
                });*/



            return myContentsView;
        }

    }

}




