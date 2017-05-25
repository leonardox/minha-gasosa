package com.minhagasosa;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.minhagasosa.dao.Abastecimento;
import com.minhagasosa.dao.AbastecimentoDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 *  classe de rotas.
 */
public class NewRefuelActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.etTotalPrice)
    EditText total;
    @BindView(R.id.etNovaData)
    EditText etDate;
    @BindView(R.id.etKM)
    EditText actualKm;
    @BindView(R.id.etPrice)
    EditText gasPrice;
    @BindView(R.id.etLitres)
    EditText litres;
    @BindView(R.id.cbFullTank)
    CheckBox fullTank;
    @BindView(R.id.btSaveRefuel)
    Button btSave;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    Calendar newDate;


    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refuel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setDateTimeField();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.requestFocus();


        btSave.setEnabled(false);

    }

    private void setDateTimeField() {
        etDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @OnClick(R.id.btSaveRefuel)
    public void submit() {
        if (validateFields()) {
            float odm = Float.parseFloat(actualKm.getText().toString());
            float ltr = Float.parseFloat(litres.getText().toString());
            float totalGasto = Float.parseFloat(total.getText().toString());
            float price = Float.parseFloat(gasPrice.getText().toString());
            boolean checked = fullTank.isChecked();
            Log.d("Refuel Checked", "" + checked);
            Date date = getDate(newDate);
            saveRefuel(odm, ltr, price, totalGasto, checked, date);
        }
    }

    private Date getDate(Calendar newDate) {
        return newDate.getTime();
    }





    @OnTextChanged(value = {R.id.etTotalPrice},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkTotal() {
//        if(!total.getText().toString().trim().equals("")){
//            if(!litres.getText().toString().trim().equals("")) {
//                float preco = Float.parseFloat(total.getText().toString()) / Float.parseFloat(litres.getText().toString());
//                gasPrice.setText(String.valueOf(preco));
//            }else if(!gasPrice.getText().toString().trim().equals("")) {
//                float litros = Float.parseFloat(total.getText().toString()) * Float.parseFloat(gasPrice.getText().toString());
//                litres.setText(String.valueOf(litros));
//            }
//        }
        validateFields();
    }

    @OnTextChanged(value = {R.id.etLitres},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkLitres() {
        validateFields();
    }

    @OnTextChanged(value = {R.id.etPrice},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkPrice() {
        validateFields();
    }

    @OnTextChanged(value = {R.id.etNovaData},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkData() {
        validateFields();
    }
    @OnTextChanged(value = {R.id.etKM},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void checkKm() {
        validateFields();
    }

    private boolean validateFields() {


        if (!total.getText().toString().trim().equals("") && !etDate.getText().toString().trim().equals("")
                && !actualKm.getText().toString().trim().equals("") && !litres.getText().toString().trim().equals("")
                && !fullTank.getText().toString().trim().equals("") && !gasPrice.getText().toString().trim().equals("")) {
            btSave.setEnabled(true);

            return true;
        }
        btSave.setEnabled(false);

        return false;
    }

    private void saveRefuel(final float odometro, final float litros, final float preco,
                            final float total, final boolean tkCheio, final Date theDate) {

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

//        Date today = new Date();
//        today.setTime(0);


        novoAbastecimento.setDataAbastecimento(theDate);
        Log.d("DATA recebida:", theDate.toString());
        novoAbastecimento.setOdometro(odometro);
        novoAbastecimento.setLitros(litros);
        novoAbastecimento.setPrecoCombustivel(preco);
        novoAbastecimento.setTanqueCheio(tkCheio);
        Log.d("Refuel Checked", "Saved " + tkCheio);
        novoAbastecimento.setPrecoTotal(total);

        abastecimentoDao.update(novoAbastecimento);
        Log.d("BD", "atualizou a rota no banco");
        MinhaGasosaPreference.setReloadRefuel(this, true);
        Log.d("NEW REFUEL AC", "reload" + MinhaGasosaPreference.getReloadRefuel(this));
        this.finish();
    }


    @Override
    public void onClick(View view) {
        if (view == etDate) {
            datePickerDialog.show();
        }
    }
}