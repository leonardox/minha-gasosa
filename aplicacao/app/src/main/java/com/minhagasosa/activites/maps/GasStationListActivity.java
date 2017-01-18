package com.minhagasosa.activites.maps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.minhagasosa.R;


public class GasStationListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_station_list);

        String[] gasStation = new String[]{"Posto 01", "Posto 02", "Posto 03", "Posto 04"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gasStation);
        ListView list = (ListView) findViewById(R.id.gas_station_list);
        list.setAdapter(adapter);
    }
}
