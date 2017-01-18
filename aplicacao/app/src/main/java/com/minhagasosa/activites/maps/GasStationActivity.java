package com.minhagasosa.activites.maps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.GasStation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GasStationActivity extends AppCompatActivity {
    private FloatingActionButton fabWrongLocal;
    private FloatingActionButton fabClosedGasStation;
    private FloatingActionButton fabWrongPrices;
    private FloatingActionButton fabAddComment;
    private FloatingActionMenu fabMenu;

    private List<String> mComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComments = new ArrayList<>();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        final GasStation gas = (GasStation) bundle.getParcelable("gas");

        setContentView(R.layout.activity_gas_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(gas.getName());

        RatingBar rating = (RatingBar) findViewById(R.id.rating);
        rating.setRating(gas.getRating());

        TextView tvGasPrice = (TextView) findViewById(R.id.tv_gasPage_gasPrice);
        TextView tvGasPlusPrice = (TextView) findViewById(R.id.tv_gasPage_gasPlusPrice);
        TextView tvAlcoolPrice = (TextView) findViewById(R.id.tv_gasPage_alcoolPrice);

        tvGasPrice.setText("R$ " + String.format("%.2f", gas.getGasPrice()));
        tvGasPlusPrice.setText("R$ " + String.format("%.2f", gas.getGasPlusPrice()));
        tvAlcoolPrice.setText("R$ " + String.format("%.2f", gas.getAlcoolPrice()));

        TextView comments = (TextView) findViewById(R.id.comments);
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommentsDialog();
            }
        });
        fabWrongLocal = (FloatingActionButton) findViewById(R.id.fab_wrong_local);
        fabClosedGasStation = (FloatingActionButton) findViewById(R.id.fab_closed_gas_station);
        fabWrongPrices = (FloatingActionButton) findViewById(R.id.fab_wrong_prices);
        fabAddComment = (FloatingActionButton) findViewById(R.id.fab_add_comment);

        fabAddComment.setOnClickListener(clickListener);
        fabWrongLocal.setOnClickListener(clickListener);
        fabClosedGasStation.setOnClickListener(clickListener);
        fabWrongPrices.setOnClickListener(clickListener);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        mComments.add("A gasolina desse posto é ótima");
        mComments.add("Ótimo atendimento");

//        ListView listViewComments = (ListView) findViewById(R.id.listComments);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, mComments);
//        listViewComments.setAdapter(adapter);

        fabMenu.setClosedOnTouchOutside(true);
//
//        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fabMenu.isOpened()) {
//                    Toast.makeText(getApplicationContext(), fabMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
//                }
//
//                fabMenu.toggle(true);
//            }
//        });

    }

    private void openAddCommentDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Digite seu comentário");
        alert.setTitle("Adicionar Comentário");
        alert.setView(edittext);

        alert.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fabMenu.close(false);
                mComments.add(edittext.getText().toString());
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fabMenu.close(false);
            }
        });
        alert.show();
    }

    private void openCommentsDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Comentários");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.addAll(mComments);

        builderSingle.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, null);
        builderSingle.show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_add_comment:
                    openAddCommentDialog();
                    //Snackbar.make(v, fabWrongLocal.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_wrong_local:
                    Snackbar.make(v, fabWrongLocal.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_closed_gas_station:
                    Snackbar.make(v, fabClosedGasStation.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_wrong_prices:
                    Snackbar.make(v, fabWrongPrices.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
            }
        }
    };
}
