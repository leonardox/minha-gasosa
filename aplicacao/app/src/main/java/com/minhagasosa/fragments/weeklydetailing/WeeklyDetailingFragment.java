package com.minhagasosa.fragments.weeklydetailing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.minhagasosa.ChartView;
import com.minhagasosa.R;

import java.util.GregorianCalendar;

public class WeeklyDetailingFragment extends Fragment {
    private ChartView chartViewCurrentMonth;
    private ChartView chartViewLastMonth;
    private boolean isFromLastYear = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Detalhamento Semanal");
        View view = inflater.inflate(R.layout.activity_detalhamento_semanal, container, false);
        PieChart pieChartCurrentMonth = (PieChart) view.findViewById(R.id.chart_current_month);
        chartViewCurrentMonth = new ChartView(getContext(), pieChartCurrentMonth);
        chartViewCurrentMonth.iniciaDistanciasSemanalmente();

        PieChart pieChartLastMonth = (PieChart) view.findViewById(R.id.chart_last_month);
        chartViewLastMonth = new ChartView(getContext(), pieChartLastMonth);

        GregorianCalendar calendar = new GregorianCalendar();
        String month = getLastMonth(calendar);
        String year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
        if (isFromLastYear) {
            year = String.valueOf(calendar.get(GregorianCalendar.YEAR) - 1);
        }

        chartViewLastMonth.iniciaDistancias(month, year);
        return view;
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
