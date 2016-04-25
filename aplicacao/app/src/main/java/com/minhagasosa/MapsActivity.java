package com.minhagasosa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mOriginLocation;
    private LatLng mDestinyLocation;
    private Marker mOriginMark;
    private Marker mDestinyMark;
    Polyline mDesenhoRotaIda;
    Polyline mDesenhoRotaVolta;
    private LatLng mCityLatLng;
    private boolean mIdaEvolta;
    private FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mIdaEvolta = false;
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
        mapFragment.setHasOptionsMenu(true);
        ImageButton btUndo = (ImageButton) findViewById(R.id.undoButton);
        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        Switch sIdaEVolta = (Switch) findViewById(R.id.switchIdaEVolta);
        final FragmentActivity self = this;
        sIdaEVolta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println();

                mIdaEvolta = isChecked;
                if(mOriginMark != null && mDestinyMark != null){
                    if(mDesenhoRotaIda != null) mDesenhoRotaIda.remove();
                    mDesenhoRotaIda = null;
                    if(mDesenhoRotaVolta != null) mDesenhoRotaVolta.remove();
                    mDesenhoRotaVolta = null;
                    DownloadTask dt = new DownloadTask(self, false);
                    String url = getDirectionsUrl(mOriginMark.getPosition(), mDestinyMark.getPosition());
                    dt.execute(url);
                }

            }
        });
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

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin.latitude+","+origin.longitude+"&destination="+dest.latitude+","+dest.longitude+"&sensor=false&mode=driving&alternatives=true&key=AIzaSyBayi_9rQWAHjoXrMYIL58KvhMVZ_GZbc0";


        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        Context mContext;
        boolean mIsVolta;
        public DownloadTask(Context c, boolean isVolta){
            mIsVolta = isVolta;
            mContext = c;
        }
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(mContext, mIsVolta);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }



    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        Context mContext;
        boolean mIsVolta;
        public ParserTask(Context c, boolean isVolta){
            mIsVolta = isVolta;
            mContext = c;
        }
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(final String... jsonData) {

            final JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                System.out.println("TESTE: " + jObject.toString());
                routes = parser.parse(jObject);
                runOnUiThread(new Runnable() {
                    public void run() {
                        float soma = somaDistanciasRotaJSON(jsonData[0]);
                        Toast.makeText(mContext, "distancia: " + soma, Toast.LENGTH_SHORT).show();
                        //Chamar método de léo aqui...
                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";



            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){   // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                if(!mIsVolta){
                    lineOptions.color(Color.RED);
                }else{
                    lineOptions.color(Color.CYAN);
                }
            }
            Log.e("Distance: " + distance, "Duration: " + duration);
            //tvDistanceDuration.setText("Distance: "+distance + ", Duration: "+duration);
            // Drawing polyline in the Google Map for the i-th route
            //if(mDesenhoRotaIda != null && !mIsVolta) mDesenhoRotaIda.remove();
            if(!mIsVolta){
                mDesenhoRotaIda = mMap.addPolyline(lineOptions);
                if(mIdaEvolta){
                    DownloadTask dt = new DownloadTask(mContext, true);
                    String url = getDirectionsUrl(mDestinyMark.getPosition(), mOriginMark.getPosition());
                    dt.execute(url);
                }
            }else{
                mDesenhoRotaVolta = mMap.addPolyline(lineOptions);
            }

        }
    }

    private Float somaDistanciasRotaJSON(String json) {
        Float soma = 0.0f;
        try {
            JSONObject result = new JSONObject(json);

            JSONArray rotas = result.getJSONArray("routes");
            for (int i = 0; i < rotas.length(); i++) {
                JSONObject rotaJson = rotas.getJSONObject(i);
                JSONArray legs = rotaJson.getJSONArray("legs");
                for (int j = 0; j < legs.length(); j++) {
                    JSONObject leg = legs.getJSONObject(j);
                    JSONObject distance = leg.getJSONObject("distance");
                    soma += distance.getInt("value");
                    break;
                }
            }
        } catch (JSONException e) {
            System.out.print("TRETAOO");
            e.printStackTrace();
        }
        return soma;
    }

    private void undo(){
        if(mDesenhoRotaIda != null) mDesenhoRotaIda.remove();
        if(mDesenhoRotaVolta != null) mDesenhoRotaVolta.remove();
        if(mOriginMark != null) mOriginMark.remove();
        if(mDestinyMark != null) mDestinyMark.remove();
        mDesenhoRotaIda = null;
        mDesenhoRotaVolta = null;
        mOriginMark = null;
        mDestinyMark = null;
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        mFab.setClickable(false);
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception whi", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onMapReady(GoogleMap googleMap) {
        final MapsActivity ac = this;
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                final LatLng location = latLng;
                if(mOriginMark != null && mDestinyMark == null){
                    mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    mFab.setClickable(true);
                    mDestinyMark = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.Destiny)));
                    //Toast.makeText(MapsActivity.this, R.string.destiny_message, Toast.LENGTH_SHORT).show();
                    DownloadTask dt = new DownloadTask(ac, false);
                    String url = getDirectionsUrl(mOriginMark.getPosition(), mDestinyMark.getPosition());
                    dt.execute(url);
                }else if(mOriginMark == null){
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
