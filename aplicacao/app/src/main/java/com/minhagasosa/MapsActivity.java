package com.minhagasosa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private LatLng mCurrentLocation;
    private Marker mCurrentMark;
    private LatLng mCityLatLng;
    FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if(this.getIntent().getStringArrayListExtra("LatLng") != null){
            ArrayList<String> loc = this.getIntent().getStringArrayListExtra("LatLng");
            //LatLng h = new LatLng();
        }else{
            Toast.makeText(this, R.string.select_initial_point, Toast.LENGTH_LONG).show();
        }
        final Activity a = this;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentMark == null) {
                    Snackbar snack = Snackbar.make(v, R.string.select_initial_point, Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                } else {
                    Intent i = new Intent();
                    i.putExtra("location", mCurrentMark.getPosition());
                    setResult(2, i);
                    finish();
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
                if(mCurrentMark != null){
                    mCurrentMark.remove();
                }else{
                    mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
                mCurrentMark = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.initial_point)));
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
