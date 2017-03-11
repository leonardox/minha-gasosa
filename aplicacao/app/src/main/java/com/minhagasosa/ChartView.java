package com.minhagasosa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.minhagasosa.Utils.calculaDistanciaTotal;
import static com.minhagasosa.Utils.calculaPrincipaisRotas;

public class ChartView {
    private Context mContext;
    private PieChart mChart;
    private String[] xData;
    private float[] yData;


    public ChartView(Context context, PieChart chart) {
        this.mContext = context;
        this.mChart = chart;
    }

    public void iniciaDistanciasSemanalmente() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();

        String dataAtual = new Date(System.currentTimeMillis()).toString();

        Utils.calculaDistanciaTotalSemanalmente(session, null, null, mContext);
        iniciaValoresGrafico(Utils.calculaPrincipaisRotasSemanalmente(session, null, null), getDistanciaTotal());
    }


    public void iniciaDistancias() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();

        String dataAtual = new Date(System.currentTimeMillis()).toString();

        calculaDistanciaTotal(session, null, null, mContext);
        iniciaValoresGrafico(calculaPrincipaisRotas(session, null, null), getDistanciaTotal());
    }

    public void iniciaDistancias(String month, String year) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();

        calculaDistanciaTotal(session, month, year, mContext);
        iniciaValoresGrafico(calculaPrincipaisRotas(session, month, year), getDistanciaTotal());
    }


    private void iniciaValoresGrafico(List<Pair<String, Float>> principaisRotas, float distanciaTotal) {
        if (!principaisRotas.isEmpty()) {
            xData = new String[principaisRotas.size()];
            yData = new float[principaisRotas.size()];
            switch (principaisRotas.size()) {
                case 1:
                    xData[0] = principaisRotas.get(0).first;

                    yData[0] = principaisRotas.get(0).second;
                    break;
                case 2:
                    xData[0] = principaisRotas.get(0).first;
                    xData[1] = principaisRotas.get(1).first;

                    yData[0] = principaisRotas.get(0).second;
                    yData[1] = principaisRotas.get(1).second;
                    break;
                case 3:
                    xData[0] = principaisRotas.get(0).first;
                    xData[1] = principaisRotas.get(1).first;
                    xData[2] = principaisRotas.get(2).first;

                    yData[0] = principaisRotas.get(0).second;
                    yData[1] = principaisRotas.get(1).second;
                    yData[2] = principaisRotas.get(2).second;
                    break;
                default:
                    xData = new String[principaisRotas.size() + 1];
                    yData = new float[principaisRotas.size() + 1];
                    xData[0] = principaisRotas.get(0).first;
                    xData[1] = principaisRotas.get(1).first;
                    xData[2] = principaisRotas.get(2).first;
                    xData[3] = "outras";

                    yData[0] = principaisRotas.get(0).second;
                    yData[1] = principaisRotas.get(1).second;
                    yData[2] = principaisRotas.get(2).second;
                    yData[3] = distanciaTotal - (yData[0] + yData[1] + yData[2]);
                    break;
            }

            addChart();
        }
    }

    private void addChart() {
        //mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

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

//            @Override
//            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                // display msg when value selected
//                if (e == null)
//                    return;
//
//                Toast.makeText(mContext,
//                        xData[e.getXIndex()] + " = " + e.getY() + "%", Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
                Toast.makeText(mContext, xData[h.getDataSetIndex()] + " = " + e.getY() + "KM", Toast.LENGTH_SHORT).show();
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

    private void addDataToUseInPieChart() {
        ArrayList<PieEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yVals1.add(new PieEntry(yData[i], xData[i]));
            Log.d("Adicionando", yData[i]  + " " + xData[i] + "");
        }

//        ArrayList<String> xVals = new ArrayList<String>();
//
//        for (int i = 0; i < xData.length; i++) {
//            xVals.add(xData[i]);
//        }

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
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }

    private String generateCenterSpannableText() {
        return "";
    }

    private float getDistanciaTotal() {
        return MinhaGasosaPreference.getDistanciaTotal(mContext);
    }
}
