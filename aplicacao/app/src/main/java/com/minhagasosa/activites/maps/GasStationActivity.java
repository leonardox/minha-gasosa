package com.minhagasosa.activites.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.GasStation;

import java.io.Serializable;

public class GasStationActivity extends AppCompatActivity {
    private FloatingActionButton fabWrongLocal;
    private FloatingActionButton fabClosedGasStation;
    private FloatingActionButton fabWrongPrices;
    private FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        final GasStation gas = (GasStation) bundle.getParcelable("gas");

        setContentView(R.layout.activity_gas_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(gas.getName());

        RatingBar rating = (RatingBar) findViewById(R.id.rating);
        rating.setRating(gas.getRating());

        TextView tvGasPrice = (TextView) findViewById(R.id.tv_gasPage_gasPrice);
        TextView tvGasPlusPrice = (TextView) findViewById(R.id.tv_gasPage_gasPlusPrice);
        TextView tvAlcoolPrice = (TextView) findViewById(R.id.tv_gasPage_alcoolPrice);

        tvGasPrice.setText("R$ " + String.format("%.2f", gas.getGasPrice()));
        tvGasPlusPrice.setText("R$ " + String.format("%.2f", gas.getGasPlusPrice()));
        tvAlcoolPrice.setText("R$ " + String.format("%.2f", gas.getAlcoolPrice()));

        fabWrongLocal = (FloatingActionButton) findViewById(R.id.fab_wrong_local);
        fabClosedGasStation = (FloatingActionButton) findViewById(R.id.fab_closed_gas_station);
        fabWrongPrices = (FloatingActionButton) findViewById(R.id.fab_wrong_prices);

        fabWrongLocal.setOnClickListener(clickListener);
        fabClosedGasStation.setOnClickListener(clickListener);
        fabWrongPrices.setOnClickListener(clickListener);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);


        fabMenu.setClosedOnTouchOutside(true);
//
//        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fabMenu.isOpened()) {
//                    Toast.makeText(getApplicationContext(), fabMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
//                }
//
//                fabMenu.toggle(true);
//            }
//        });

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_wrong_local:
                    Snackbar.make(v, fabWrongLocal.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_closed_gas_station:
                    Snackbar.make(v, fabClosedGasStation.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_wrong_prices:
                    Snackbar.make(v, fabWrongPrices.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
            }
        }
    };
}
