package com.minhagasosa.fragments.Refuel;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.minhagasosa.ChartView;
import com.minhagasosa.NewRefuelActivity;
import com.minhagasosa.R;
import com.minhagasosa.dao.Abastecimento;
import com.minhagasosa.dao.AbastecimentoDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.preferences.MinhaGasosaPreference;
import com.minhagasosa.utils.GenerateData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import de.greenrobot.dao.query.Query;

import static butterknife.OnItemSelected.Callback.NOTHING_SELECTED;


/**
 * A simple {@link Fragment} subclass.
 */
public class RefuelFragment extends Fragment {

    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etTotal)
    EditText etTotal;
    @BindView(R.id.etLitres)
    EditText etLitres;
    @BindView(R.id.etKM)
    EditText etKm;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.cbFullTank)
    CheckBox cbFull;
    @BindView(R.id.spinnerRefuel)
    Spinner spinner;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.etKMperLitre)
    EditText etKmPerL;
    private Unbinder unbinder;
    private ChartView chartView;
    List<Abastecimento> abastecimentos;
    List<Abastecimento> data;
    GenerateData mGenerator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_refuel, container, false);
        PieChart pieChart = (PieChart) view.findViewById(R.id.chart);
        chartView = new ChartView(getContext(), pieChart);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Refuel Fragment", "Reload: " + MinhaGasosaPreference.getReloadRefuel(getContext()));
        init();
    }

    private void init() {
        loadRefuel();
        loadSpinnerData();
        getKmPerLitre();
        disableTexts();
    }


    @OnClick(R.id.fab)
    void newRefuel() {
        Intent intent = new Intent(getActivity(), NewRefuelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void disableTexts() {
        disableTexts(etDate);
        disableTexts(etKm);
        disableTexts(etLitres);
        disableTexts(etPrice);
        disableTexts(etTotal);
        disableTexts(etKmPerL);

        cbFull.setFocusable(false);
        cbFull.setEnabled(false);
        cbFull.setCursorVisible(false);
        cbFull.setKeyListener(null);
        cbFull.setBackgroundColor(Color.TRANSPARENT);
        cbFull.setTextColor(Color.BLACK);
    }
    private void disableTexts(EditText editText){

        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.BLACK);
    }

    private void loadRefuel(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        AbastecimentoDao aDao = session.getAbastecimentoDao();
        abastecimentos  = aDao.loadAll();
        if (abastecimentos.size()>0){
            mGenerator = new GenerateData(abastecimentos);
            Abastecimento last = abastecimentos.get(abastecimentos.size()-1);
            loadData(last);
        }
    }


    private void loadData(Abastecimento mAbastecimento){
        etDate.setText(mAbastecimento.getDataAbastecimento().toString());
        etKm.setText(mAbastecimento.getOdometro().toString());
        etLitres.setText(mAbastecimento.getLitros().toString());
        etPrice.setText(mAbastecimento.getPrecoCombustivel().toString());
        etTotal.setText(mAbastecimento.getPrecoTotal().toString());
        Log.d("Refuel RESTORED", "" + mAbastecimento.getTanqueCheio());
        cbFull.setChecked(mAbastecimento.getTanqueCheio());
    }

    @OnItemSelected(R.id.spinnerRefuel)
    public void itemSelected (Spinner spn, int position) {
        String label = spn.getItemAtPosition(position).toString();
        int id = Integer.parseInt(label.split(",")[0]);
        for (Abastecimento abs: abastecimentos){
            if (abs.getId() == id){
                loadData(abs);
                Toast.makeText(getContext(), "You selected: " + abs.toString(), Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @OnItemSelected(value = R.id.spinnerRefuel,
            callback = NOTHING_SELECTED)
    public void nadaSelected () {
    }

    private void loadSpinnerData() {
        ArrayAdapter<Abastecimento> dataAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, abastecimentos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void getKmPerLitre() {
        if(mGenerator != null){
            double kmLitre = mGenerator.getKmPerLitre();
            if(kmLitre>0){
                etKmPerL.setText(String.valueOf(kmLitre));
            }else {
                etKmPerL.setText("ND");
            }
        }else{
            etKmPerL.setText("ND");
        }

    }
}