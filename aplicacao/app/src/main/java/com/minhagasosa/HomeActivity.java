package com.minhagasosa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    EditText priceFuelEditText;
    EditText priceFuelEditText2;
    TextView secundarioText;
    TextView secundarioPriceText;
    CheckBox checkFlex;
    TextView porcentagem1;
    TextView porcentagem2;
    TextView consumoS;
    TextView consumoM;
    TextView porcento1;
    TextView porcento2;
    Spinner spinner_porcentagem1;
    Spinner spinner_porcentagem2;
    String[] porcento = {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50",
            "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"};

    private PieChart mChart;
    private float[] yData = {5, 10, 15};
    private String[] xData = {"Academia", "Trabalho", "Faculdade"};

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
        consumoS = (TextView) findViewById(R.id.textView7);
        consumoM = (TextView) findViewById(R.id.textView6);
        porcento1 = (TextView) findViewById(R.id.textView2);
        porcento1.setVisibility(View.GONE);
        porcento2 = (TextView) findViewById(R.id.textView12);
        porcento2.setVisibility(View.GONE);
        porcentagem2 = (TextView) findViewById(R.id.textView11);
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
                    gerarPrevisao();
                }
            }
        });
        priceFuelEditText2.addTextChangedListener(new TextWatcher() {
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
                if (!s.toString().isEmpty()) {
                    gerarPrevisao();
                }
            }
        });
        spinner_porcentagem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gerarPrevisao();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_porcentagem2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gerarPrevisao();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkFlex = (CheckBox) findViewById(R.id.checkBox);
        checkFlex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secundarioText.setVisibility(View.VISIBLE);
                    secundarioPriceText.setVisibility(View.VISIBLE);
                    priceFuelEditText2.setVisibility(View.VISIBLE);
                    porcentagem1.setVisibility(View.VISIBLE);
                    porcentagem2.setVisibility(View.VISIBLE);
                    spinner_porcentagem1.setVisibility(View.VISIBLE);
                    spinner_porcentagem2.setVisibility(View.VISIBLE);
                    porcento1.setVisibility(View.VISIBLE);
                    porcento2.setVisibility(View.VISIBLE);
                } else {
                    secundarioText.setVisibility(View.GONE);
                    priceFuelEditText2.setText("");
                    secundarioPriceText.setVisibility(View.GONE);
                    priceFuelEditText2.setVisibility(View.GONE);
                    porcentagem1.setVisibility(View.GONE);
                    porcentagem2.setVisibility(View.GONE);
                    spinner_porcentagem1.setVisibility(View.GONE);
                    spinner_porcentagem2.setVisibility(View.GONE);
                    porcento1.setVisibility(View.GONE);
                    porcento2.setVisibility(View.GONE);
                }
            }
        });
        addChart();
    }

    private void gerarPrevisao() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (checkFlex.isChecked() == false) {
            float precoPrincipal = Float.parseFloat(priceFuelEditText.getText().toString());
            //float distancias = 150.0f;
            float distancias = MinhaGasosaPreference.getDistanciaTotal(getApplicationContext());
            Log.d("HomeActivity", "Distancias: " + distancias);
            float consumoUrbano = MinhaGasosaPreference.
                    getConsumoUrbanoPrimario(getApplicationContext());
            Log.d("HomeActivity", "Consumo: " + consumoUrbano);
            float result = (distancias / consumoUrbano) * precoPrincipal;
            consumoS.setText("R$ " + df.format(result));
            consumoM.setText("R$ " + df.format(result * 4));
        } else {
            float precoPrincipal = 0.0f;
            if (!priceFuelEditText.getText().toString().isEmpty()) {
                precoPrincipal = Float.parseFloat(priceFuelEditText.getText().toString());
            }
            float precoSecundario = 0.0f;
            if (!priceFuelEditText2.getText().toString().isEmpty()) {
                precoSecundario = Float.parseFloat(priceFuelEditText2.getText().toString());
            }
            float distancias = 100.0f;
            float consumoUrbano = MinhaGasosaPreference.
                    getConsumoUrbanoPrimario(getApplicationContext());
            float consumoUrbanoSecundario = MinhaGasosaPreference.
                    getConsumoUrbanoSecundario(getApplicationContext());
            int porcentagemPrincipal = Integer.parseInt(
                    spinner_porcentagem1.getSelectedItem().toString());
            int porcentagemSecundaria = Integer.parseInt(
                    spinner_porcentagem2.getSelectedItem().toString());
            float result = 0.0f;
            if (porcentagemPrincipal != 0 || porcentagemSecundaria != 0) {
                float autonomia = (distancias / (((consumoUrbano * porcentagemPrincipal) / 100) +
                        ((consumoUrbanoSecundario * porcentagemSecundaria) / 100)));
                result = ((((autonomia * porcentagemPrincipal) / 100) * precoPrincipal) +
                        (((autonomia * porcentagemSecundaria) / 100) * precoSecundario));
            }
            consumoS.setText("R$ " + df.format(result));
            consumoM.setText("R$ " + df.format(result * 4));
        }
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
        } else if (item.getItemId() == R.id.set_route) {
            Intent i = new Intent(this, MapsActivity.class);
            Toast.makeText(HomeActivity.this, "Isso é só um teste, esse botão de mapinha vai sumir...", Toast.LENGTH_LONG).show();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addChart() {
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(HomeActivity.this,
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addDataToUseInPieChart();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private CharSequence generateCenterSpannableText() {
        SpannableString s = new SpannableString("R$");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 2, 0);
        return s;
    }

    private void addDataToUseInPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(3);

        // add colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }
}
