package com.minhagasosa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;

public class ComparaGastosActivity extends AppCompatActivity {
    private ChartView chartViewCurrentMonth;
    private ChartView chartViewLastMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compara_gastos);

        // ele já sabe construir o gráfico do mês atual.
        PieChart pieChartCurrentMonth = (PieChart) findViewById(R.id.chart_current_month);
        chartViewCurrentMonth = new ChartView(this, pieChartCurrentMonth);
        chartViewCurrentMonth.iniciaDistancias();

        // todo fazer o chatViewLastMonth saber montar o gráfico para o mês passado.
        PieChart pieChartLastMonth = (PieChart) findViewById(R.id.chart_last_month);
        chartViewLastMonth = new ChartView(this, pieChartLastMonth);
        chartViewLastMonth.iniciaDistancias();
    }
}
