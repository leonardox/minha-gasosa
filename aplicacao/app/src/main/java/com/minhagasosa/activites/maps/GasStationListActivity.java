package com.minhagasosa.activites.maps;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.minhagasosa.API.GasStationService;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.activites.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GasStationListActivity extends BaseActivity {
    protected List<GasStation> gasStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_station_list);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Postos ordenados por preço");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GasStationService gasService = retrofit.create(GasStationService.class);
        gasService.getAllGasStation().enqueue(new Callback<List<GasStation>>() {
            @Override
            public void onResponse(Call<List<GasStation>> call, final Response<List<GasStation>> response) {
                if (response.code() == 200) {
                    gasStation = response.body();

                    //System.out.print(response.body()); Visualização da resposta
//                    for (GasStation gas : response.body()) {
//                        String name = gas.getName();
//                        String gasPrice = Float.toString(gas.getGasPrice());
//                        String gasPlusPrice = Float.toString(gas.getGasPlusPrice());
//                        String gasAlcoolPrice = Float.toString(gas.getAlcoolPrice());
//
//                        gasStation.add(new String[]{name, gasPrice, gasPlusPrice, gasAlcoolPrice});
//                    }
                }

                for(int i = 0; i < gasStation.size(); i++){
                    for(int j = 0; j < gasStation.size() - 1; j++){
                        if(gasStation.get(j).getGasPrice() > gasStation.get(j + 1).getGasPrice()){
                            GasStation aux = gasStation.get(j);
                            gasStation.set(j, gasStation.get(j + 1));
                            gasStation.set(j + 1, aux);
                        }
                    }
                }

                //System.out.print(gasStation); Visualização do array
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, gasStation) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setTextColor(Color.BLACK);
                        text2.setTextColor(Color.BLACK);

                        text1.setText(gasStation.get(position).getName());
                        text2.setText("Valor: " + Float.toString(gasStation.get(position).getGasPrice()) + " R$");
                        return view;
                    }
                };

                ListView list = (ListView) findViewById(R.id.gas_station_list);
                list.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<GasStation>> call, Throwable t) {
                Log.e("Error", "Error getting gas stations" + t.toString());
            }
        });


    }
}
