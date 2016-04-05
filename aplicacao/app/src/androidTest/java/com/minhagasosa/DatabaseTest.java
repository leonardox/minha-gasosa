package com.minhagasosa;


import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.minhagasosa.dao.Carro;
import com.minhagasosa.dao.CarroDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Modelo;
import com.minhagasosa.dao.ModeloDao;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        JobManager jobManager = new JobManager(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        final DaoSession session = daoMaster.newSession();
        final ModeloDao mDao = session.getModeloDao();
        final CarroDao cDao = session.getCarroDao();

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

            InputStream is = getContext().getAssets().open(fileName);

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

    public void testADD() {
        assertEquals("casa", "casa");
        Log.d("TESTANDO", "OIIIIIII AQUIIIIIIIIIIIIIII");
    }
}
