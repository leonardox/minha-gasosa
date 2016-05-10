package com.minhagasosa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;

public class ComparaGastosActivity extends AppCompatActivity {
    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compara_gastos);

        PieChart pieChart = (PieChart) findViewById(R.id.chart_current_month);
        chartView = new ChartView(this, pieChart);
        chartView.iniciaDistancias();
    }
}
