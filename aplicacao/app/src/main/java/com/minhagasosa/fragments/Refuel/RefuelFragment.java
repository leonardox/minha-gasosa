package com.minhagasosa.fragments.Refuel;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.minhagasosa.ChartView;
import com.minhagasosa.NewRefuelActivity;
import com.minhagasosa.R;
import com.minhagasosa.RotaAdapter;
import com.minhagasosa.dao.Abastecimento;
import com.minhagasosa.dao.AbastecimentoDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RefuelFragment extends Fragment{

    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.etTotal) EditText etTotal;
    @BindView(R.id.etLitres) EditText etLitres;
    @BindView(R.id.etKM) EditText etKm;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.cbFullTank) CheckBox cbFull;
    @BindView(R.id.fab) FloatingActionButton fab;
    private Unbinder unbinder;
    private ChartView chartView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_refuel, container, false);
        PieChart pieChart = (PieChart) view.findViewById(R.id.chart);
        chartView = new ChartView(getContext(), pieChart);
        unbinder = ButterKnife.bind(this, view);
        loadRefuel();
        disableTexts();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Refuel Fragment", "Reload: " +MinhaGasosaPreference.getReloadRefuel(getContext()));
            loadRefuel();
    }


    @OnClick(R.id.fab)
    void newRefuel(){
        Intent intent = new Intent(getActivity(), NewRefuelActivity.class);
        startActivity(intent);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void disableTexts(){
        etDate.setEnabled(false);
        etKm.setEnabled(false);
        etLitres.setEnabled(false);
        etPrice.setEnabled(false);
        etTotal.setEnabled(false);
        cbFull.setEnabled(false);
    }

    private void loadRefuel(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        AbastecimentoDao aDao = session.getAbastecimentoDao();
        List<Abastecimento> abastecimentos  = aDao.loadAll();
        if (abastecimentos.size()>0){
            Abastecimento last = abastecimentos.get(abastecimentos.size()-1);
            etDate.setText(last.getDataAbastecimento().toString());
            etKm.setText(last.getOdometro().toString());
            etLitres.setText(last.getLitros().toString());
            etPrice.setText(last.getPrecoCombustivel().toString());
            etTotal.setText(last.getPrecoTotal().toString());
            Log.d("Refuel RESTORED", "" + last.getTanqueCheio());
            cbFull.setChecked(last.getTanqueCheio());
        }
    }
}