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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;

public class PopulateDatabaseTest extends AndroidTestCase {
    private static final String LOT_TAG_TEST = "PopulateDatabaseTest";
    private final int NUM_CARROS_JSON = 1132;
    private final int NUM_MODELOS_JSON = 528;
    private ModeloDao modeloDao;
    private CarroDao carroDao;
    private DaoSession session;
    private static boolean isFirstTime = true;

    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(LOT_TAG_TEST, "entrou no setUp");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        session = daoMaster.newSession();
        modeloDao = session.getModeloDao();
        carroDao = session.getCarroDao();

        if (isFirstTime) {
            modeloDao.deleteAll();
            carroDao.deleteAll();
            isFirstTime = false;
        }
    }

    public void testPopulateBD() {
        populateCars(modeloDao, session.getCarroDao());
        carroDao = session.getCarroDao();
        modeloDao = session.getModeloDao();

        assertEquals(NUM_MODELOS_JSON, modeloDao.count());
        assertEquals(NUM_CARROS_JSON, carroDao.count());

        modeloDao.deleteAll();
        carroDao.deleteAll();

        assertEquals(0, modeloDao.count());
        assertEquals(0, carroDao.count());
    }

    private String loadJSONFromAsset(String fileName) {
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

    private void populateCars(ModeloDao mDao, CarroDao cDao) {
        String jsonModelos = loadJSONFromAsset("Modelos.json");
        String jsonCarros = loadJSONFromAsset("carrosminhagasosa.json");
        try {
            JSONArray modelos = new JSONArray(jsonModelos);
            Log.d(LOT_TAG_TEST, "num de modelos no jSon: " + modelos.length());
            for (int i = 0; i < modelos.length(); i++) {
                JSONObject modeloJson = modelos.getJSONObject(i);
                Modelo modelo = new Modelo(modeloJson.getLong("ID"), modeloJson.getString("MODELO"));
                Log.d(LOT_TAG_TEST, "Adding model: " + modeloJson.getString("MODELO"));
                //System.out.println();
                mDao.insert(modelo);
            }
            JSONArray carros = new JSONArray(jsonCarros);
            Log.d(LOT_TAG_TEST, "num de carros no jSon: " + modelos.length());
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
                    Log.d(LOT_TAG_TEST, "Inserting car: " + c.getVersion() + " | " + i);
                    cDao.insert(c);
                }
            }
        } catch (JSONException e) {
            System.out.print("TRETAOO");
            e.printStackTrace();
        }
    }
}
