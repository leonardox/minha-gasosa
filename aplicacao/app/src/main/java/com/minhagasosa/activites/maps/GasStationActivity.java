package com.minhagasosa.activites.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.GasStation;

import java.io.Serializable;

public class GasStationActivity extends AppCompatActivity {
    private FloatingActionButton fabWrongLocal;
    private FloatingActionButton fabClosedGasStation;
    private FloatingActionButton fabWrongPrices;

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

        final FloatingActionMenu fabMenu;
        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);


        fabWrongLocal.setEnabled(false);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.hideMenuButton(false);

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    Toast.makeText(getApplicationContext(), fabMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                fabMenu.toggle(true);
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, gas.getName() + " " + "Preço Reportado", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_wrong_local:
                    break;
                case R.id.fab_closed_gas_station:
                    fabWrongPrices.setVisibility(View.GONE);
                    break;
                case R.id.fab_wrong_prices:
                    fabWrongPrices.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
