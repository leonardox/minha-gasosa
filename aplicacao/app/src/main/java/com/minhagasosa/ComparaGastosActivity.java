package com.minhagasosa;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
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
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.util.ArrayList;
import java.util.List;

import static com.minhagasosa.Utils.calculaDistanciaTotal;
import static com.minhagasosa.Utils.calculaPrincipaisRotas;

public class ComparaGastosActivity extends AppCompatActivity {
    private float[] dadosYMesAtual = new float[4];
    private String[] dadosXMesAtual = new String[4];
    private PieChart mGraficoMesAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compara_gastos);

        iniciaGraficos();
    }

    private void iniciaGraficos() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        calculaDistanciaTotal(session, getApplicationContext());
        iniciaGraficoMesAtual(calculaPrincipaisRotas(session), MinhaGasosaPreference.getDistanciaTotal(getApplicationContext()));
    }

    private void iniciaGraficoMesAtual(List<Pair<String, Float>> principaisRotas, float distanciaTotal) {
        if (!principaisRotas.isEmpty()) {
            dadosXMesAtual[0] = principaisRotas.get(0).first;
            dadosXMesAtual[1] = principaisRotas.get(1).first;
            dadosXMesAtual[2] = principaisRotas.get(2).first;
            dadosXMesAtual[3] = "outras";

            dadosYMesAtual[0] = principaisRotas.get(0).second;
            dadosYMesAtual[1] = principaisRotas.get(1).second;
            dadosYMesAtual[2] = principaisRotas.get(2).second;
            dadosYMesAtual[3] = distanciaTotal - (dadosYMesAtual[0] + dadosYMesAtual[1] + dadosYMesAtual[2]);

            addChart();
        }
    }

    private void addChart() {
        mGraficoMesAtual = (PieChart) findViewById(R.id.chart1);
        mGraficoMesAtual.setUsePercentValues(true);
        mGraficoMesAtual.setDescription("");
        mGraficoMesAtual.getLegend().setEnabled(false);
        mGraficoMesAtual.setExtraOffsets(5, 10, 5, 5);

        mGraficoMesAtual.setDragDecelerationFrictionCoef(0.95f);

        // mGraficoMesAtual.setCenterText(generateCenterSpannableText());

        mGraficoMesAtual.setDrawHoleEnabled(true);
        mGraficoMesAtual.setHoleColor(Color.WHITE);

        mGraficoMesAtual.setTransparentCircleColor(Color.WHITE);
        mGraficoMesAtual.setTransparentCircleAlpha(110);

        mGraficoMesAtual.setHoleRadius(58f);
        mGraficoMesAtual.setTransparentCircleRadius(61f);

        mGraficoMesAtual.setDrawCenterText(true);

        mGraficoMesAtual.setRotationAngle(0);
        // enable rotation of the chart by touch
        mGraficoMesAtual.setRotationEnabled(true);
        mGraficoMesAtual.setHighlightPerTapEnabled(true);

        // mGraficoMesAtual.setUnit(" €");
        // mGraficoMesAtual.setDrawUnitsInChart(true);

        // add a selection listener
        mGraficoMesAtual.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(ComparaGastosActivity.this,
                        dadosXMesAtual[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addDadosAGraficoMesAtual();

        mGraficoMesAtual.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mGraficoMesAtual.spin(2000, 0, 360);

        Legend l = mGraficoMesAtual.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void addDadosAGraficoMesAtual() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < dadosYMesAtual.length; i++)
            yVals1.add(new Entry(dadosYMesAtual[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < dadosXMesAtual.length; i++)
            xVals.add(dadosXMesAtual[i]);

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

        mGraficoMesAtual.setData(data);

        // undo all highlights
        mGraficoMesAtual.highlightValues(null);

        // update pie chart
        mGraficoMesAtual.invalidate();
    }
}
