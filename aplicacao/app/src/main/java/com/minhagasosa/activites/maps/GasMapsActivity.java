package com.minhagasosa.activites.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.vision.text.Text;
import com.minhagasosa.API.GasStationService;
import com.minhagasosa.activites.BaseFragmentActivity;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.GasStation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.minhagasosa.R.id.map;

/**
 * Classe de Mapa.
 */
public class GasMapsActivity extends BaseFragmentActivity
        implements OnMapReadyCallback {
    /**
     * API Google Mapa
     */
    private GoogleMap mMap;
    /**
     * atributo local de origem
     */
    private LatLng mOriginLocation;
    /**
     * Atributo Local de destino
     */
    private LatLng mDestinyLocation;
    /**
     * Atributo marca de origin
     */
    private Marker mOriginMark;
    /**
     * Atributo marca de destino
     */
    private Marker mDestinyMark;
    /**
     * Atributo desenho da rota de ida no mapa
     */
    private Polyline mDesenhoRotaIda;
    /**
     * desenho da rota de volta no mapa
     */
    private Polyline mDesenhoRotaVolta;
    /**
     * latitude e longitude da cidade no mapa
     */
    private LatLng mCityLatLng;
    /**
     * ida e volta
     */
    private boolean mIdaEvolta;
    /**
     * distancia de ida
     */
    private float mDistanciaIda = -1;
    /**
     * distancia de volta
     */
    private float mDistanciaVolta = -1;
    /**
     * atribute
     */
    private FloatingActionButton mFab;

    private Map<Marker, GasStation> allMarkersMap = new HashMap<Marker, GasStation>();

    boolean firstTime = true;


    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_maps);
        GasStationService gasService = retrofit.create(GasStationService.class);
        gasService.getAllGasStation().enqueue(new Callback<List<GasStation>>() {
            @Override
            public void onResponse(Call<List<GasStation>> call, final Response<List<GasStation>> response) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                                        @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        GasStation gas = allMarkersMap.get(marker);

                        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                        TextView tvDetails = (TextView) v.findViewById(R.id.tv_snippet);
                        TextView tvGasPrice = (TextView) v.findViewById(R.id.tv_gasPrice);
                        TextView tvGasPlusPrice = (TextView) v.findViewById(R.id.tv_gasPlusPrice);
                        TextView tvAlcoolPrice = (TextView) v.findViewById(R.id.tv_alcoolPrice);
                        TextView tvOpenning = (TextView) v.findViewById(R.id.tv_openning);
                        ImageButton bt_details = (ImageButton) v.findViewById(R.id.bt_gasStationsButton);

//                        bt_details.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent i = new Intent(GasMapsActivity.this, GasStationActivity.class);
//                                i.putExtra("gas", (Serializable) gas);
//                                startActivity(i);
//
//                            }
//                        });
                        tvTitle.setText(marker.getTitle());
                        tvDetails.setText(marker.getSnippet());
                        tvOpenning.setText("Aberto das 7:00 ás 21:00 horas");
                        // Set price
                        try { // SET GAS
                            Float gasPrice = new Float(gas.getGasPrice());
                            //float gasPrice = gas.getGasPrice();
                            tvGasPrice.setText(getString(R.string.gas_price) + ": R$ " + String.format("%.2f", gasPrice));
                        }catch(Exception e) {
                            tvGasPrice.setVisibility(View.GONE);
                        }

                        try { // SET GAS PLUS
                            Float gasPlusPrice = new Float(gas.getGasPlusPrice());
                            tvGasPlusPrice.setText(getString(R.string.gas_plus_price) + ": R$ " + String.format("%.2f", gasPlusPrice));
                        }catch(Exception e) {
                            tvGasPlusPrice.setVisibility(View.GONE);
                        }

                        try { // SET ALCOOL
                            Float alcoolPrice = new Float(gas.getAlcoolPrice());
                            tvAlcoolPrice.setText(getString(R.string.alcool_price) + ": R$ " + String.format("%.2f", alcoolPrice));
                        }catch(Exception e) {
                            tvAlcoolPrice.setVisibility(View.GONE);
                        }
                        return v;
                    }
                });
                if (response.code() == 200) {
                    Log.d("Stations", "Got " + response.body().size() + " Gas stations");
                    for (GasStation gas : response.body()) {
                        LatLng loc = new LatLng(gas.getLocation().getLat(), gas.getLocation().getLng());
                        setMarker(gas, loc);
                    }
                } else {
                    Log.e("ERROR", "error getting gas stations");
                }

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        GasStation gas = allMarkersMap.get(marker);

                        Intent i = new Intent(GasMapsActivity.this, GasStationActivity.class);
                        i.putExtra("gas", gas);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<GasStation>> call, Throwable t) {
                Log.e("Treta", "Error getting gas stations" + t.toString());
            }
        });


        mIdaEvolta = false;
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_local_gas_station_black_24dp_1x));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.setHasOptionsMenu(true);

        final FragmentActivity self = this;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(GasMapsActivity.this, GasStationListActivity.class);
                startActivity(i);
            }
        });
        Intent i = getIntent();
        double[] cityCords = i.getDoubleArrayExtra("cityCoords");
        if (cityCords != null) {
            mCityLatLng = new LatLng(cityCords[0], cityCords[1]);
        }
        mapFragment.getMapAsync(this);

        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (permissionGranted) {
            // {Some Code}
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // {Some Code}
                }
            }
        }
    }

    private void setMarker(GasStation gas, LatLng loc) {
        Marker m = mMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(gas.getName())
                .snippet(getString(R.string.rating) + ": " + String.format("%.2f", gas.getRating()))
        );
        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_gas_station_black_24dp_1x));
        allMarkersMap.put(m, gas);

    }


    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.undo_route:

                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public final void onMapReady(GoogleMap googleMap) {
        final GasMapsActivity ac = this;
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        final Activity self = this;
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (firstTime) {
                    firstTime = false;
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    private void setTextViewText(TextView tv, String text, Float price){
        if (price != null){
            tv.setText(text + ": R$ " + String.format("%.2f", price));
        }
        else {
            tv.setVisibility(View.GONE);
        }
    }
}
