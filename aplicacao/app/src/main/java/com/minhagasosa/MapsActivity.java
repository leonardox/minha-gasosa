package com.minhagasosa;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mOriginLocation;
    private LatLng mDestinyLocation;
    private Marker mOriginMark;
    private Marker mDestinyMark;
    private LatLng mCityLatLng;
    private FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(this.getIntent().getStringArrayListExtra("LatLng") != null){
            ArrayList<String> loc = this.getIntent().getStringArrayListExtra("LatLng");
            //LatLng h = new LatLng();
        }else{
            Toast.makeText(this, getString(R.string.select_origin) ,Toast.LENGTH_LONG).show();
        }
        final Activity a = this;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOriginMark == null || mDestinyMark == null) {
                    Snackbar snack = Snackbar.make(v, R.string.select_origin_and_destiny, Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                } else {
                    //Calculate route here
                    mOriginLocation = mOriginMark.getPosition();
                    mDestinyLocation = mDestinyMark.getPosition();
                    Toast.makeText(MapsActivity.this, "Origin: " + mOriginLocation.longitude + ", " + mOriginLocation.longitude , Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapsActivity.this, "Destiny: " + mDestinyLocation.longitude + ", " + mDestinyLocation.longitude , Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent i = getIntent();
        double[] cityCords = i.getDoubleArrayExtra("cityCoords");
        if(cityCords != null){
            mCityLatLng = new LatLng(cityCords[0], cityCords[1]);
        }
        mapFragment.getMapAsync(this);

    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final MapsActivity ac = this;
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                final LatLng location = latLng;
                if(mOriginMark != null){
                    mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    mDestinyMark = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.Destiny)));
                    Toast.makeText(MapsActivity.this, R.string.destiny_message, Toast.LENGTH_SHORT).show();
                }else{
                    mOriginMark = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.origin)));
                    Toast.makeText(MapsActivity.this, R.string.origin_text, Toast.LENGTH_SHORT).show();
                }

            }
        });
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if(mCityLatLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCityLatLng, (float) 16.0));
        }else{
            mMap.setMyLocationEnabled(true);
            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange (Location location) {
                    LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
    }
}
