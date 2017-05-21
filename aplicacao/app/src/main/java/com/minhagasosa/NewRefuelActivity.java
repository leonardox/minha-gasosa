package com.minhagasosa;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.minhagasosa.dao.Abastecimento;
import com.minhagasosa.dao.AbastecimentoDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;



/**
 *  classe de rotas.
 */
public class NewRefuelActivity extends AppCompatActivity {

    @BindView(R.id.etTotalPrice) EditText total;
    @BindView(R.id.etNovaData) EditText date;
    @BindView(R.id.etKM) EditText actualKm;
    @BindView(R.id.etPrice) EditText gasPrice;
    @BindView(R.id.etLitres) EditText litres;
    @BindView(R.id.cbFullTank) CheckBox fullTank;
    @BindView(R.id.btSaveRefuel) Button btSave;



    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refuel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        btSave.setEnabled(false);
    }

    @OnClick(R.id.btSaveRefuel)
    public void submit() {
        if(validateFields()){
            float odm =  Float.parseFloat(actualKm.getText().toString());
            float ltr =  Float.parseFloat(litres.getText().toString());
            float totalGasto =  Float.parseFloat(total.getText().toString());
            float price =  Float.parseFloat(gasPrice.getText().toString());
            boolean checked =  fullTank.isChecked();
            Log.d("Refuel Checked", ""+ checked);
            saveRefuel(odm, ltr,price, totalGasto,checked);
        }
    }

    @OnTextChanged(value = { R.id.etTotalPrice, R.id.etNovaData, R.id.etKM,R.id.etLitres, R.id.cbFullTank },
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkField(){
        validateFields();
    }


    private boolean validateFields() {

        if (! total.getText().toString().trim().equals("")
//                && date.getText().toString() != null
                && !actualKm.getText().toString().trim().equals("") && !litres.getText().toString().trim().equals("")
                && !fullTank.getText().toString().trim().equals("") && !gasPrice.getText().toString().trim().equals("")) {
            btSave.setEnabled(true);

//
//        if (total.getText().toString() != null
////                && date.getText().toString() != null
//                && actualKm.getText().toString() != null && litres.getText().toString() != null
//                && fullTank.getText().toString() != null && gasPrice.getText().toString() != null) {
//            btSave.setEnabled(true);
            return true;
        }
        btSave.setEnabled(false);

        return false;
    }

    private void saveRefuel(final float odometro, final float litros, final float preco,
                            final float total, final boolean tkCheio) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        AbastecimentoDao abastecimentoDao = session.getAbastecimentoDao();
        Abastecimento novoAbastecimento = new Abastecimento();
        abastecimentoDao.insert(novoAbastecimento);

        novoAbastecimento.setId(abastecimentoDao.getKey(novoAbastecimento));
        Log.d("RoutesActivity", "id da rota inserida: " + novoAbastecimento.getId());
        Log.d("RoutesActivity", "id da rota inserida no bd: " + abastecimentoDao.getKey(novoAbastecimento));

        Date today = new Date();
        today.setTime(0);


        novoAbastecimento.setDataAbastecimento(today);
        novoAbastecimento.setOdometro(odometro);
        novoAbastecimento.setLitros(litros);
        novoAbastecimento.setPrecoCombustivel(preco);
        novoAbastecimento.setTanqueCheio(tkCheio);
        Log.d("Refuel Checked", "Saved "+ tkCheio);
        novoAbastecimento.setPrecoTotal(total);

        abastecimentoDao.update(novoAbastecimento);
        Log.d("BD", "atualizou a rota no banco");
        MinhaGasosaPreference.setReloadRefuel(this,true);
        Log.d("NEW REFUEL AC", "reload" + MinhaGasosaPreference.getReloadRefuel(this));
        this.finish();
    }

}