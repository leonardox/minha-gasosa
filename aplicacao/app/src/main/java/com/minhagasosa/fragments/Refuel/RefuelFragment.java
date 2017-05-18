package com.minhagasosa.fragments.Refuel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.minhagasosa.ChartView;
import com.minhagasosa.MyReceiver;
import com.minhagasosa.R;
import com.minhagasosa.RoutesActivity;
import com.minhagasosa.fragments.carsettings.CarSettingsFragment;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class RefuelFragment extends Fragment{

    private ChartView chartView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        PieChart pieChart = (PieChart) view.findViewById(R.id.chart);
        chartView = new ChartView(getContext(), pieChart);
        View viewKeyboard = getActivity().getCurrentFocus();
        if (viewKeyboard != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return view;
    }




}