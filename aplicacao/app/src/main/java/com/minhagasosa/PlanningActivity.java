package com.minhagasosa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.minhagasosa.preferences.MinhaGasosaPreference;

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

        final EditText editValor = (EditText) findViewById(R.id.ed_valor_maximo);

        Button button = (Button) findViewById(R.id.buttonOK);

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

}
