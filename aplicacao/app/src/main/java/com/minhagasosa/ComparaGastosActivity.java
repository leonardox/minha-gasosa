package com.minhagasosa;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;

import java.util.GregorianCalendar;

public class ComparaGastosActivity extends AppCompatActivity {
    private ChartView chartViewCurrentMonth;
    private ChartView chartViewLastMonth;
    private boolean isFromLastYear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compara_gastos);
        ActionBar action = getSupportActionBar();
        if(action != null){
            action.setTitle("Comparação dos Gastos");
        }

        PieChart pieChartCurrentMonth = (PieChart) findViewById(R.id.chart_current_month);
        chartViewCurrentMonth = new ChartView(this, pieChartCurrentMonth);
        chartViewCurrentMonth.iniciaDistancias();

        PieChart pieChartLastMonth = (PieChart) findViewById(R.id.chart_last_month);
        chartViewLastMonth = new ChartView(this, pieChartLastMonth);

        GregorianCalendar calendar = new GregorianCalendar();
        String month = getLastMonth(calendar);
        String year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
        if (isFromLastYear) {
            year = String.valueOf(calendar.get(GregorianCalendar.YEAR) - 1);
        }

        chartViewLastMonth.iniciaDistancias(month, year);

        showTotalPercorrido(String.valueOf(getTotalPercorrido(month, year)));
    }

    private float getTotalPercorrido(String month, String year) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();

        Log.d("COMPARA_GASTOS_ACTIVITY", "total percorrido = " + Utils.calculaDistanciaTotal(session, month, year));

        return Utils.calculaDistanciaTotal(session, month, year);
    }

    private void showTotalPercorrido(String total) {
        TextView tvTotalDistance = (TextView) findViewById(R.id.tv_total_distance);
        tvTotalDistance.setText(tvTotalDistance.getText() + " " + total + " " + getResources().getText(R.string.kilometers));
    }

    private String getLastMonth(GregorianCalendar calendar) {
        // vai de 0 a 11;
        switch (calendar.get(GregorianCalendar.MONTH)) {
            case 0: // janeiro
                isFromLastYear = true;
                return "Dec";
            case 1: // fevereiro
                return "Jan";
            case 2: // março
                return "Feb";
            case 3: // abril
                return "Mar";
            case 4: // maio
                return "Apr";
            case 5: // junho
                return "May";
            case 6: // julho
                return "Jun";
            case 7: // agosto
                return "Jul";
            case 8: // setembro
                return "Aug";
            case 9: // outubro
                return "Set";
            case 10: // novembro
                return "Oct";
            case 11: // dezembro
                return "Nov";
        }
        return null;
    }
}
