package com.minhagasosa;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.minhagasosa.dao.Carro;
import com.minhagasosa.dao.CarroDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Modelo;
import com.minhagasosa.dao.ModeloDao;
import com.minhagasosa.preferences.MinhaGasosaPreference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMarca;
    Spinner spinnerModelo;
    Spinner spinnerVersao;
    EditText textPotencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "casosa-db", null);
        JobManager jobManager = new JobManager(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession session = daoMaster.newSession();
        final ModeloDao mDao = session.getModeloDao();
        final CarroDao cDao = session.getCarroDao();

        spinnerMarca = (Spinner) findViewById(R.id.spinnerMarca);
        spinnerModelo = (Spinner) findViewById(R.id.spinnerModelo);
        spinnerVersao = (Spinner) findViewById(R.id.spinnerVersao);
        textPotencia = (EditText) findViewById(R.id.textPotencia);

        String[] marcas = {"Aston Martin", "Audi", "Bentley", "BMW", "Chery", "Chevrolet", "Citroen", "Dodge", "Ferrari", "Fiat", "Ford", "Geely", "Honda", "Hyundai", "JAC", "Jaguar", "Jeep", "Kia", "Lamborghini", "Land Rover", "Lexus", "Lifan", "Maserati", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Peugeot", "Porsche", "Rely", "Renault", "Shineray", "Smart", "Ssangyong", "Subaru", "Suzuki", "Toyota", "Troller", "Volkswagen", "Volvo"};
        spinnerMarca.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, marcas));

        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                popularModelos(session);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                popularVersoes(cDao);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerVersao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salvarInformacoesCarro(cDao);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mDao.count() == 0) {
            jobManager.addJobInBackground(new Job(new Params(1)) {
                @Override
                public void onAdded() {

                }

                @Override
                public void onRun() throws Throwable {
                    CarroDao cDao = session.getCarroDao();
                    populateCars(mDao, cDao);
                }

                @Override
                protected void onCancel() {

                }
            });
            System.out.println("Populating...");

        }
        CarroDao dao = session.getCarroDao();

        textPotencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //empty
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if(s.toString().isEmpty()){
                    MinhaGasosaPreference.putWithPotency(false, getApplicationContext());
                    MinhaGasosaPreference.putPotency(0, getApplicationContext());
                } else{
                    MinhaGasosaPreference.putWithPotency(true, getApplicationContext());
                    MinhaGasosaPreference.putPotency(Integer.valueOf(s.toString()),
                            getApplicationContext());
                }
            }
        });

        final Button btnPrevisoes = (Button) findViewById(R.id.btnPrevisoes);
        btnPrevisoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(final View v) {
                // Potency value at sharedPreferences
                Log.e("MinhaGasosa", "ValorArmazenadoPotencia = " + String.valueOf(MinhaGasosaPreference.getPotency(
                        getApplicationContext())));
                Log.e("MinhaGasosa", "IsPotencia = " + String.valueOf(MinhaGasosaPreference.getWithPotency(
                        getApplicationContext())));
            }
        });
    }

    private void salvarInformacoesCarro(CarroDao cDao) {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String marca = (String) spinnerMarca.getSelectedItem();
        Modelo modelo = (Modelo) spinnerModelo.getSelectedItem();
        long modelo_id = modelo.getId();
        String versao = (String) spinnerVersao.getSelectedItem();
        Query query = cDao.queryBuilder().where(CarroDao.Properties.ModeloId.eq(modelo_id),
                CarroDao.Properties.Marca.eq(marca),
                CarroDao.Properties.Version.eq(versao)).build();
        Carro carro = (Carro) query.list().get(0);

        if (carro.getIsFlex() != null) {
            editor.putBoolean(
                    getString(R.string.is_flex), carro.getIsFlex());
        }
        if (carro.getConsumoUrbanoGasolina() != null) {
            editor.putFloat(getString(R.string.consumoUrbanoPrimario),
                    carro.getConsumoUrbanoGasolina());
        }
        if (carro.getConsumoRodoviarioGasolina() != null) {
            editor.putFloat(getString(R.string.consumoRodoviarioPrimario),
                    carro.getConsumoRodoviarioGasolina());
        }
        if (carro.getConsumoUrbanoAlcool() != null) {
            editor.putFloat(getString(R.string.consumoUrbanoSecundario),
                    carro.getConsumoUrbanoAlcool());
        }
        if (carro.getConsumoRodoviarioAlcool() != null) {
            editor.putFloat(getString(R.string.consumoRodoviarioSecundario),
                    carro.getConsumoRodoviarioAlcool());
        }
        editor.commit();
    }

    private void popularVersoes(CarroDao cDao) {
        Modelo selectedModel = (Modelo) spinnerModelo.getSelectedItem();
        long idx = selectedModel.getId();
        ArrayList<Carro> listaCarros = (ArrayList<Carro>) cDao.queryBuilder().where(CarroDao.Properties.ModeloId.eq(idx)).list();
        String[] listaVersoes = new String[listaCarros.size()];
        for (int i = 0; i < listaCarros.size(); i++) {
            listaVersoes[i] = listaCarros.get(i).toString();
        }
        spinnerVersao.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaVersoes));
    }

    private void popularModelos(DaoSession session) {
        String marca = (String) spinnerMarca.getSelectedItem();
        String select = "SELECT MODELO._id, MODELO.MODELO FROM CARRO, MODELO " +
                "WHERE CARRO.MARCA ='" + marca + "' AND MODELO._id = CARRO.MODELO_ID " +
                "GROUP BY MODELO.MODELO ORDER BY MODELO.MODELO ASC";
        ArrayList<Modelo> listaModelos = (ArrayList<Modelo>) listModelos(session, select);
        spinnerModelo.setAdapter(new ArrayAdapter<Modelo>(this,
                android.R.layout.simple_spinner_item, listaModelos));
    }

    private void populateCars(ModeloDao mDao, CarroDao cDao) {
        String jsonModelos = loadJSONFromAsset("Modelos.json");
        String jsonCarros = loadJSONFromAsset("carrosminhagasosa.json");
        try {
            JSONArray modelos = new JSONArray(jsonModelos);
            for (int i = 0; i < modelos.length(); i++) {
                JSONObject modeloJson = modelos.getJSONObject(i);
                Modelo modelo = new Modelo(modeloJson.getLong("ID"), modeloJson.getString("MODELO"));
                System.out.println("Adding model: " + modeloJson.getString("MODELO"));
                mDao.insert(modelo);
            }
            JSONArray carros = new JSONArray(jsonCarros);
            for (int i = 0; i < carros.length(); i++) {
                JSONObject cj = carros.getJSONObject(i);
                if (cj.getInt("FLEX") == 1) {
                    Carro c = new Carro();
                    c.setId(cj.getLong("id"));
                    c.setMarca(cj.getString("marca"));
                    c.setAno(cj.getString("ano"));
                    c.setConsumoUrbanoGasolina((float) cj.getDouble("urbano"));
                    c.setConsumoRodoviarioGasolina((float) cj.getDouble("rodoviario"));
                    c.setVersion(cj.getString("VERSION"));
                    c.setModeloId(cj.getLong("MODEL"));
                    if (cj.getInt("FLEX") == 1) {
                        c.setConsumoUrbanoAlcool((float) cj.getDouble("urbano_alcol"));
                        c.setConsumoRodoviarioAlcool((float) cj.getDouble("rodoviario_alcool"));
                    }
                    System.out.println("Inserting car: " + c.getVersion() + " | " + i);
                    cDao.insert(c);
                }

            }
        } catch (JSONException e) {
            System.out.print("TRETAOO");
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {

            InputStream is = getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private static List<Modelo> listModelos(DaoSession session, String select) {
        ArrayList<Modelo> result = new ArrayList<Modelo>();
        Cursor c = session.getDatabase().rawQuery(select, null);
        ModeloDao mDao = session.getModeloDao();
        try {
            if (c.moveToFirst()) {
                do {
                    Modelo m = new Modelo();
                    m.setId(c.getLong(0));
                    m.setMODELO(c.getString(1));
                    result.add(m);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }


}
