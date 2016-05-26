package com.minhagasosa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Classe de planejamento.
 */
public class PlanningActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final EditText editValor = (EditText) findViewById(R.id.ed_valor_maximo);

        Button button = (Button) findViewById(R.id.buttonOK);
        float distancia = getIntent().getFloatExtra("distance", -1);
        float consumo = getIntent().getFloatExtra("consumo", -1);
        float capacidade = MinhaGasosaPreference.getCapacidadeDoTanque(getApplicationContext());
        Log.e("Distania: ", distancia+"");
        Log.e("Consumo: ", consumo+"");
        Log.e("Capacidade: ", capacidade+"");
        if(distancia > 0 && capacidade != -1 && consumo != -1){
            TextView edVezes = (TextView) findViewById(R.id.ed_tanque_vezes);
            float vezes = (distancia/consumo)/capacidade;
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
            edVezes.setText(getString(R.string.neeeded_times) + df.format(vezes) + " " + getString(R.string.times) + ".");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float valor = Float.parseFloat(editValor.getText().toString());
                MinhaGasosaPreference.putValorMaximoParaGastar(valor, PlanningActivity.this);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
