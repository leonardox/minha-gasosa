package com.minhagasosa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Classe Main.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * atributo de MARCA do carro
     */
    private Spinner spinnerMarca;
    /**
     * Atributo de modelo do carro
     */
    private Spinner spinnerModelo;
    /**
     * atributo da versao do carro
     */
    private Spinner spinnerVersao;
    /**
     * atributo da potencia do carro
     */
    private Spinner spinnerPotencia;
    /**
     * atributo progresso
     */
    private ProgressDialog progress;
    /**
     * verifica
     */
    private int check = 0;
    /**
     *
     */
    public static Activity self;
    /**
     * instancia da classe CarroDao
     */
    private CarroDao cDao;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Configurar Carro");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "casosa-db", null);
        self = this;
        JobManager jobManager = new JobManager(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession session = daoMaster.newSession();
        final ModeloDao mDao = session.getModeloDao();
        cDao = session.getCarroDao();

        //ADIÇÃO DE ALGUMAS ROTAS FICTICIAS
        //rDao.insert(new Rota((long)1, "Olar", true, (float)10.5, (float)5.5, false, 0));
        //rDao.insert(new Rota((long)2, "Olar2", true, (float)5.5, (float)5.5, true, 1));
        //rDao.insert(new Rota((long)3, "Olar3", false, (float)8.5, (float)0, true, 1));
        //rDao.insert(new Rota((long)4, "Olar4", false, (float)10.0, (float)0, true, 5));

//        if (MinhaGasosaPreference.getDone(getApplicationContext())
//                && !getIntent().getBooleanExtra("fromHome", false)) {
//            Log.d("ta passando aqui", "ola");
//            Intent i = new Intent(this, HomeActivity.class);
//            this.startActivity(i);
//            return;
//        }
        spinnerMarca = (Spinner) findViewById(R.id.spinnerMarca);
        spinnerModelo = (Spinner) findViewById(R.id.spinnerModelo);
        spinnerVersao = (Spinner) findViewById(R.id.spinnerVersao);
        spinnerPotencia = (Spinner) findViewById(R.id.spinnerPot);

        String[] marcas = {"Aston Martin", "Audi", "Bentley", "BMW", "Chery", "Chevrolet", "Citroen", "Dodge", "Ferrari", "Fiat",
                "Ford", "Geely", "Honda", "Hyundai", "JAC", "Jaguar", "Jeep", "Kia", "Lamborghini", "Land Rover", "Lexus", "Lifan",
                "Maserati", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Peugeot", "Porsche", "Rely", "Renault", "Shineray",
                "Smart", "Ssangyong", "Subaru", "Suzuki", "Toyota", "Troller", "Volkswagen", "Volvo"};
        spinnerMarca.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, marcas));

        String selectedMarca = MinhaGasosaPreference.getMarca(getApplicationContext());
        if(selectedMarca != null){
            spinnerMarca.setSelection(Arrays.asList(marcas).indexOf(selectedMarca), true);
            popularModelos(session);
        }

        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                popularModelos(session);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                popularVersoes(cDao);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        if (mDao.count() == 0) {
            setFinishOnTouchOutside(false);
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("Populando carros, isso so sera feito uma vez");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();

            jobManager.addJobInBackground(new Job(new Params(1)) {
                @Override
                public void onAdded() {

                }

                @Override
                public void onRun() throws Exception {
                    CarroDao cDao = session.getCarroDao();
                    populateCars(mDao, cDao);
                }

                @Override
                protected void onCancel() {

                }
            });


        }
        spinnerPotencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                String selectedPot = (String) spinnerPotencia.getSelectedItem();
                if (selectedPot.isEmpty()) {
                    MinhaGasosaPreference.putWithPotency(false, getApplicationContext());
                    MinhaGasosaPreference.putPotency(0.0f, getApplicationContext());
                } else {
                    MinhaGasosaPreference.putWithPotency(true, getApplicationContext());
                    MinhaGasosaPreference.putPotency(Float.valueOf(selectedPot), getApplicationContext());
                    MinhaGasosaPreference.setConsumoUrbanoPrimario(10.0f, getApplicationContext());
                    MinhaGasosaPreference.setConsumoRodoviarioPrimario(10.5f, getApplicationContext());
                    MinhaGasosaPreference.setConsumoUrbanoSecundario(11.0f, getApplicationContext());
                    MinhaGasosaPreference.setConsumoUrbanoSecundario(11.5f, getApplicationContext());
                }
            }
            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });
        String[] potencias = {"", "1.0", "1.4", "1.6", "1.8", "2.0", "2.2", "3.0"};
        spinnerPotencia.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, potencias));

        String selectedPotencia = MinhaGasosaPreference.getVersao(getApplicationContext());
        if(selectedPotencia != null){
            spinnerMarca.setSelection(Arrays.asList(potencias).indexOf(selectedPotencia), true);
        }

    }

    /**
     * metodo que salva todas as informacoes do carro no banco
     * @param cDao
     */
    private void salvarInformacoesCarro(final CarroDao cDao) {
        if (!MinhaGasosaPreference.getWithPotency(getApplicationContext())) {
            String marca = (String) spinnerMarca.getSelectedItem();
            Modelo modelo = (Modelo) spinnerModelo.getSelectedItem();
            long modeloId = modelo.getId();
            String versao = (String) spinnerVersao.getSelectedItem();
            Query query = cDao.queryBuilder().where(CarroDao.Properties.MODELO_ID.eq(modeloId),
                    CarroDao.Properties.MARCA.eq(marca),
                    CarroDao.Properties.VERSION.eq(versao)).build();
            Carro carro = (Carro) query.list().get(0);

            if(carro.getMarca() != null){
                MinhaGasosaPreference.setMarca(carro.getMarca() ,getApplicationContext());
            }

            if(carro.getModelo().getMODELO() != null){
                MinhaGasosaPreference.setModelo(carro.getModelo().getMODELO() ,getApplicationContext());
            }

            if(carro.getVersion() != null){
                MinhaGasosaPreference.setVersao(carro.getVersion() ,getApplicationContext());
            }

            if (carro.getIsFlex() != null) {
                MinhaGasosaPreference.setCarroIsFlex(carro.getIsFlex(), getApplicationContext());
            }
            if (carro.getConsumoUrbanoGasolina() != null) {
                MinhaGasosaPreference.setConsumoUrbanoPrimario(carro.getConsumoUrbanoGasolina(),
                        getApplicationContext());
            }
            if (carro.getConsumoRodoviarioGasolina() != null) {
                MinhaGasosaPreference.setConsumoRodoviarioPrimario(carro.getConsumoRodoviarioGasolina(),
                        getApplicationContext());
            }
            if (carro.getConsumoUrbanoAlcool() != null) {
                MinhaGasosaPreference.setConsumoUrbanoSecundario(carro.getConsumoUrbanoAlcool(),
                        getApplicationContext());
            }
            if (carro.getConsumoRodoviarioAlcool() != null) {
                MinhaGasosaPreference.setConsumoRodoviarioSecundario(carro.getConsumoRodoviarioAlcool(),
                        getApplicationContext());
            }
        }
        EditText edCapacidadeTanuqe = (EditText) findViewById(R.id.ed_capacidade_tanque);
        String capacidade = edCapacidadeTanuqe.getText().toString();
        if(!capacidade.trim().isEmpty()){
            try {
                float cap  = Float.valueOf(capacidade);
                Log.e("OrigCap:", cap+"");
                MinhaGasosaPreference.putCapacidadeDoTanque(cap, getApplicationContext());
            }catch (Exception e){
                Log.e("Treta", "Treta!");
                e.printStackTrace();
            }
        }
        MinhaGasosaPreference.setDone(true, getApplicationContext());
        Intent i = new Intent(this, HomeActivity.class);
        this.startActivity(i);
    }

    /**
     *  metodo que popula as versoes do carro para o banco de dados
     * @param cDao
     */
    private void popularVersoes(final CarroDao cDao) {
        Modelo selectedModel = (Modelo) spinnerModelo.getSelectedItem();
        long idx = selectedModel.getId();
        ArrayList<Carro> listaCarros = (ArrayList<Carro>) cDao.queryBuilder().where(CarroDao.Properties.MODELO_ID.eq(idx)).list();
        String[] listaVersoes = new String[listaCarros.size()];
        for (int i = 0; i < listaCarros.size(); i++) {
            listaVersoes[i] = listaCarros.get(i).toString();
        }
        spinnerVersao.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaVersoes));

        String selectedVersion = MinhaGasosaPreference.getVersao(getApplicationContext());
        if(selectedVersion != null){
            spinnerMarca.setSelection(Arrays.asList(listaVersoes).indexOf(selectedVersion), true);
        }


    }

    /**
     * Metodo que retorna a soma das rotas cadastradas no sistema
     *
     * @param session
     * @return
     */

    private void popularModelos(final DaoSession session) {
        String marca = (String) spinnerMarca.getSelectedItem();
        String select = "SELECT MODELO._id, MODELO.MODELO FROM CARRO, MODELO " +
                "WHERE CARRO.MARCA ='" + marca + "' AND MODELO._id = CARRO.MODELO_ID " +
                "GROUP BY MODELO.MODELO ORDER BY MODELO.MODELO ASC";
        ArrayList<Modelo> listaModelos = (ArrayList<Modelo>) listModelos(session, select);
        spinnerModelo.setAdapter(new ArrayAdapter<Modelo>(this,
                android.R.layout.simple_spinner_item, listaModelos));

        String selectedModelo = MinhaGasosaPreference.getModelo(getApplicationContext());

        if(selectedModelo != null){
            spinnerMarca.setSelection(listaModelos.indexOf(selectedModelo), true);
            popularVersoes(cDao);
        }
    }

    /**
     *  metodo que popula o modelo e o carro no banco
     * @param mDao
     * @param cDao
     */
    private void populateCars(final ModeloDao mDao, final CarroDao cDao) {
        String jsonModelos = loadJSONFromAsset("Modelos.json");
        String jsonCarros = loadJSONFromAsset("carrosminhagasosa.json");
        try {
            JSONArray modelos = new JSONArray(jsonModelos);
            for (int i = 0; i < modelos.length(); i++) {
                JSONObject modeloJson = modelos.getJSONObject(i);
                Modelo modelo = new Modelo(modeloJson.getLong("ID"), modeloJson.getString("MODELO"));
                mDao.insert(modelo);
            }
            JSONArray carros = new JSONArray(jsonCarros);
            for (int i = 0; i < carros.length(); i++) {
                JSONObject cj = carros.getJSONObject(i);
                Carro c = new Carro();
                c.setId(cj.getLong("id"));
                c.setMarca(cj.getString("marca"));
                c.setAno(cj.getString("ano"));
                c.setConsumoUrbanoGasolina((float) cj.getDouble("urbano"));
                c.setConsumoRodoviarioGasolina((float) cj.getDouble("rodoviario"));
                c.setVersion(cj.getString("VERSION"));
                c.setModeloId(cj.getLong("MODEL"));
                if (cj.getInt("FLEX") == 1) {
                    c.setIsFlex(true);
                    c.setConsumoUrbanoAlcool((float) cj.getDouble("urbano_alcol"));
                    c.setConsumoRodoviarioAlcool((float) cj.getDouble("rodoviario_alcool"));
                } else {
                    c.setIsFlex(false);
                }
                cDao.insert(c);

            }
            progress.hide();
            Intent i = new Intent(self, MainActivity.class);
            self.startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public final String loadJSONFromAsset(final String fileName) {
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

    /**
     *
     * @param session
     * @param select
     * @return
     */
    private static List<Modelo> listModelos(final DaoSession session, final String select) {
        ArrayList<Modelo> result = new ArrayList<Modelo>();
        Cursor c = session.getDatabase().rawQuery(select, null);
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

    /**
     * metodo que retorna o valor maximo que deve ser gasto
     * @return
     */
    public final float getValorMaximo() {
        return MinhaGasosaPreference.getValorMaximoParaGastar(MainActivity.this);
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.distance_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_route:
                // Potency value at sharedPreferences
                Log.e("MinhaGasosa", "ValorArmazenadoPotencia = " + String.valueOf(MinhaGasosaPreference.getPotency(
                        getApplicationContext())));
                Log.e("MinhaGasosa", "IsPotencia = " + String.valueOf(MinhaGasosaPreference.getWithPotency(
                        getApplicationContext())));
                salvarInformacoesCarro(cDao);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
