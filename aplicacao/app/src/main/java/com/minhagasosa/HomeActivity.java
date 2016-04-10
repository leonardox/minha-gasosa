package com.minhagasosa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minhagasosa.preferences.MinhaGasosaPreference;

public class HomeActivity extends AppCompatActivity {
    EditText priceFuelEditText;
    EditText priceFuelEditText2;
    TextView secundarioText;
    TextView secundarioPriceText;
    CheckBox checkFlex;
    TextView porcentagem1;
    TextView porcentagem2;
    Spinner spinner_porcentagem1;
    Spinner spinner_porcentagem2;
    String[] porcento = {"0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%", "40%", "45%", "50%",
            "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%", "100%"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        secundarioText = (TextView) findViewById(R.id.textView8);
        secundarioText.setVisibility(View.GONE);
        secundarioPriceText = (TextView) findViewById(R.id.textView9);
        secundarioPriceText.setVisibility(View.GONE);
        priceFuelEditText2 = (EditText) findViewById(R.id.editText);
        priceFuelEditText2.setVisibility(View.GONE);
        porcentagem1 = (TextView) findViewById(R.id.textView10);
        porcentagem1.setVisibility(View.GONE);
        porcentagem2 = (TextView) findViewById(R.id.textView11);
        porcentagem2.setVisibility(View.GONE);
        spinner_porcentagem1 = (Spinner) findViewById(R.id.spinner);
        spinner_porcentagem1.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, porcento));
        spinner_porcentagem1.setVisibility(View.GONE);
        spinner_porcentagem2 = (Spinner) findViewById(R.id.spinner2);
        spinner_porcentagem2.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, porcento));
        spinner_porcentagem2.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RoutesActivity.class);
                startActivity(intent);
                //Toast.makeText(HomeActivity.this, "Ação de adicionar rota aqui...", Toast.LENGTH_SHORT).show();
            }
        });
        priceFuelEditText = (EditText) findViewById(R.id.editTextPrice);
        priceFuelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //empty
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().isEmpty()) {
                    MinhaGasosaPreference.putPrice(0, getApplicationContext());
                } else {
                    MinhaGasosaPreference.putPrice(Float.valueOf(s.toString()),
                            getApplicationContext());
                }
            }
        });
        checkFlex = (CheckBox) findViewById(R.id.checkBox);
        checkFlex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    secundarioText.setVisibility(View.VISIBLE);
                    secundarioPriceText.setVisibility(View.VISIBLE);
                    priceFuelEditText2.setVisibility(View.VISIBLE);
                    porcentagem1.setVisibility(View.VISIBLE);
                    porcentagem2.setVisibility(View.VISIBLE);
                    spinner_porcentagem1.setVisibility(View.VISIBLE);
                    spinner_porcentagem2.setVisibility(View.VISIBLE);
                }else{
                    secundarioText.setVisibility(View.GONE);
                    priceFuelEditText2.setText("");
                    secundarioPriceText.setVisibility(View.GONE);
                    priceFuelEditText2.setVisibility(View.GONE);
                    porcentagem1.setVisibility(View.GONE);
                    porcentagem2.setVisibility(View.GONE);
                    spinner_porcentagem1.setVisibility(View.GONE);
                    spinner_porcentagem2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.set_car) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("fromHome", true);
            startActivity(i);
        }else if(item.getItemId() == R.id.set_route){
            Intent i = new Intent(this, MapsActivity.class);
            Toast.makeText(HomeActivity.this, "Isso é só um teste, esse botão de mapinha vai sumir...", Toast.LENGTH_LONG).show();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
