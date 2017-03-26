package com.minhagasosa.fragments.carsettings;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.minhagasosa.R;
import com.minhagasosa.dao.Carro;
import com.minhagasosa.dao.CarroDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Modelo;
import com.minhagasosa.dao.ModeloDao;
import com.minhagasosa.fragments.Home.HomeFragment;
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
import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.Query;

public class CarSettingsFragment extends Fragment {


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
    public static CarSettingsFragment self;
    /**
     * instancia da classe CarroDao
     */
    private CarroDao cDao;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Configurar Carro");
        View view = inflater.inflate(R.layout.activity_main, container, false);

        setHasOptionsMenu(true);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        self = this;
        JobManager jobManager = new JobManager(getContext());
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

        // todo é para deixar? start
//        if (MinhaGasosaPreference.getDone(getContext())) {
//            Fragment fragment = new HomeFragment();
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//            return view;
//        }
        // todo é para deixar? end
        spinnerMarca = (Spinner) view.findViewById(R.id.spinnerMarca);
        spinnerModelo = (Spinner) view.findViewById(R.id.spinnerModelo);
        spinnerVersao = (Spinner) view.findViewById(R.id.spinnerVersao);
        spinnerPotencia = (Spinner) view.findViewById(R.id.spinnerPot);

        String[] marcas = {"Aston Martin", "Audi", "Bentley", "BMW", "Chery", "Chevrolet", "Citroen", "Dodge", "Ferrari", "Fiat",
                "Ford", "Geely", "Honda", "Hyundai", "JAC", "Jaguar", "Jeep", "Kia", "Lamborghini", "Land Rover", "Lexus", "Lifan",
                "Maserati", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Peugeot", "Porsche", "Rely", "Renault", "Shineray",
                "Smart", "Ssangyong", "Subaru", "Suzuki", "Toyota", "Troller", "Volkswagen", "Volvo"};
        spinnerMarca.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, marcas));

        String selectedMarca = MinhaGasosaPreference.getMarca(getContext());

        if (selectedMarca != null) {
            Log.d("tenta carregar marca", selectedMarca);
            spinnerMarca.setSelection(Arrays.asList(marcas).indexOf(selectedMarca), true);
            popularModelos(session);
        } else Log.d("tenta carregar marca", "null");

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
            getActivity().setFinishOnTouchOutside(false);
            progress = new ProgressDialog(getContext());
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
                    MinhaGasosaPreference.putWithPotency(false, getContext());
                    MinhaGasosaPreference.putPotency(0.0f, getContext());
                } else {
                    MinhaGasosaPreference.putWithPotency(true, getContext());
                    MinhaGasosaPreference.putPotency(Float.valueOf(selectedPot), getContext());
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });
        String selectedPotencia = String.valueOf(MinhaGasosaPreference.getPotency(getContext()));
        String[] potencias = {"", "1.0", "1.4", "1.6", "1.8", "2.0", "2.2", "3.0"};
        spinnerPotencia.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, potencias));
        if (selectedPotencia != null) {
            Log.d("tenta carregar potencia", selectedPotencia);
            spinnerPotencia.setSelection(Arrays.asList(potencias).indexOf(selectedPotencia), true);
        } else Log.d("tenta carregar potencia", "null");

        return view;
    }

    /**
     * metodo que salva todas as informacoes do carro no banco
     *
     * @param cDao
     */
    private void salvarInformacoesCarro(final CarroDao cDao, View view) {

        String marca = (String) spinnerMarca.getSelectedItem();
        Modelo modelo = (Modelo) spinnerModelo.getSelectedItem();
        long modeloId = modelo.getId();
        String versao = (String) spinnerVersao.getSelectedItem();
        Query query = cDao.queryBuilder().where(CarroDao.Properties.MODELO_ID.eq(modeloId),
                CarroDao.Properties.MARCA.eq(marca),
                CarroDao.Properties.VERSION.eq(versao)).build();
        Carro carro = (Carro) query.list().get(0);

        if (carro.getMarca() != null) {
            MinhaGasosaPreference.setMarca(carro.getMarca(), getContext());
            Log.d("Salvando Marca", carro.getMarca());
        }

        if (carro.getModelo().getMODELO() != null) {
            MinhaGasosaPreference.setModelo(carro.getModelo().getMODELO(), getContext());
            Log.d("Salvando Modelo", carro.getModelo().toString());
        }

        if (carro.getVersion() != null) {
            MinhaGasosaPreference.setVersao(carro.getVersion(), getContext());
            Log.d("Salvando Versao", carro.getVersion());
        }

        if (carro.getIsFlex() != null) {
            MinhaGasosaPreference.setCarroIsFlex(carro.getIsFlex(), getContext());
        }
        if (carro.getConsumoUrbanoGasolina() != null) {
            MinhaGasosaPreference.setConsumoUrbanoPrimario(carro.getConsumoUrbanoGasolina(),
                    getContext());
        }else{
            MinhaGasosaPreference.setConsumoUrbanoPrimario(10.0f, getContext());

        }
        if (carro.getConsumoRodoviarioGasolina() != null) {
            MinhaGasosaPreference.setConsumoRodoviarioPrimario(carro.getConsumoRodoviarioGasolina(),
                    getContext());
        }else {
            MinhaGasosaPreference.setConsumoRodoviarioPrimario(10.5f, getContext());
        }
        if (carro.getConsumoUrbanoAlcool() != null) {
            MinhaGasosaPreference.setConsumoUrbanoSecundario(carro.getConsumoUrbanoAlcool(),
                    getContext());
        }else {
            MinhaGasosaPreference.setConsumoUrbanoSecundario(11.0f, getContext());

        }
        if (carro.getConsumoRodoviarioAlcool() != null) {
            MinhaGasosaPreference.setConsumoRodoviarioSecundario(carro.getConsumoRodoviarioAlcool(),
                    getContext());
        }else {
            MinhaGasosaPreference.setConsumoRodoviarioSecundario(11.5f, getContext());

        }
        EditText edCapacidadeTanuqe = (EditText) view.findViewById(R.id.ed_capacidade_tanque);
        String capacidade = edCapacidadeTanuqe.getText().toString();
        if (!capacidade.trim().isEmpty()) {
            try {
                float cap = Float.valueOf(capacidade);
                MinhaGasosaPreference.putCapacidadeDoTanque(cap, getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MinhaGasosaPreference.setDone(true, getContext());
        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    /**
     * metodo que popula as versoes do carro para o banco de dados
     *
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
        spinnerVersao.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listaVersoes));

        if (selectedModel.equals(MinhaGasosaPreference.getModelo(getContext()))) {

            String selectedVersion = MinhaGasosaPreference.getVersao(getContext());
            if (selectedVersion != null) {
                Log.d("tenta carregar verao", selectedVersion);
                spinnerMarca.setSelection(Arrays.asList(listaVersoes).indexOf(selectedVersion), true);
            } else Log.d("tenta carregar versao", "null");


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
        spinnerModelo.setAdapter(new ArrayAdapter<Modelo>(getContext(),
                android.R.layout.simple_spinner_item, listaModelos));


        if (marca.equals(MinhaGasosaPreference.getMarca(getContext()))) {

            String selectedModelo = MinhaGasosaPreference.getModelo(getContext());
            if (selectedModelo != null) {
                Log.d("tenta carregar modelo", selectedModelo);
                spinnerMarca.setSelection(listaModelos.indexOf(selectedModelo), true);
                popularVersoes(cDao);
            } else Log.d("tenta carregar modelo", "null");
        }
    }

    /**
     * metodo que popula o modelo e o carro no banco
     *
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
//            Intent i = new Intent(self, MainActivity.class);
//            self.startActivity(i);
            Fragment fragment = new CarSettingsFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle argsCar = new Bundle();
            argsCar.putBoolean("fromHome", true);
            fragment.setArguments(argsCar);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName
     * @return
     */
    public final String loadJSONFromAsset(final String fileName) {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open(fileName);

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
     *
     * @return
     */
    public final float getValorMaximo() {
        return MinhaGasosaPreference.getValorMaximoParaGastar(getActivity());
    }

    @Override
    public final void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.distance_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_route:
                salvarInformacoesCarro(cDao, getView());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
