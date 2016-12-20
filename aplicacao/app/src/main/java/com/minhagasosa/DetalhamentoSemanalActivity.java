package com.minhagasosa;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.minhagasosa.ChartView;
import com.minhagasosa.R;
import com.minhagasosa.Utils;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;

import java.util.GregorianCalendar;

public class DetalhamentoSemanalActivity extends AppCompatActivity {
    private ChartView chartViewCurrentMonth;
    private ChartView chartViewLastMonth;
    private boolean isFromLastYear = false;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhamento_semanal);
        ActionBar action = getSupportActionBar();
        if(action != null){
            action.setTitle("Detalhamento Semanal");
        }

        PieChart pieChartCurrentMonth = (PieChart) findViewById(R.id.chart_current_month);
        chartViewCurrentMonth = new ChartView(this, pieChartCurrentMonth);
        chartViewCurrentMonth.iniciaDistanciasSemanalmente();

        PieChart pieChartLastMonth = (PieChart) findViewById(R.id.chart_last_month);
        chartViewLastMonth = new ChartView(this, pieChartLastMonth);

        GregorianCalendar calendar = new GregorianCalendar();
        String month = getLastMonth(calendar);
        String year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
        if (isFromLastYear) {
            year = String.valueOf(calendar.get(GregorianCalendar.YEAR) - 1);
        }

        chartViewLastMonth.iniciaDistancias(month, year);
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
