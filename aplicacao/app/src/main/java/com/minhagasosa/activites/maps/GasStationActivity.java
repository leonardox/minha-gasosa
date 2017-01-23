package com.minhagasosa.activites.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.minhagasosa.API.GasStationService;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.Comments;
import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.activites.BaseActivity;
import com.minhagasosa.adapters.CommentAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GasStationActivity extends BaseActivity {
    private FloatingActionButton fabWrongLocal;
    private FloatingActionButton fabClosedGasStation;
    private FloatingActionButton fabWrongPrices;
    private FloatingActionButton fabAddComment;
    private FloatingActionMenu fabMenu;
    private GasStationService mGasService;
    private List<String> mComments;
    private GasStation mGas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComments = new ArrayList<>();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        mGas = (GasStation) bundle.getParcelable("gas");

        setContentView(R.layout.activity_gas_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(mGas.getName());

        RatingBar rating = (RatingBar) findViewById(R.id.rating);
        rating.setRating(mGas.getRating());

        TextView tvGasPrice = (TextView) findViewById(R.id.tv_gasPage_gasPrice);
        TextView tvGasPlusPrice = (TextView) findViewById(R.id.tv_gasPage_gasPlusPrice);
        TextView tvAlcoolPrice = (TextView) findViewById(R.id.tv_gasPage_alcoolPrice);

        tvGasPrice.setText("R$ " + String.format("%.2f", mGas.getGasPrice()));
        tvGasPlusPrice.setText("R$ " + String.format("%.2f", mGas.getGasPlusPrice()));
        tvAlcoolPrice.setText("R$ " + String.format("%.2f", mGas.getAlcoolPrice()));

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

        mGasService = retrofit.create(GasStationService.class);

    }

    private void openAddCommentDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Digite seu comentário");
        alert.setTitle("Adicionar Comentário");
        alert.setView(edittext);

        final Activity self = this;

        alert.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fabMenu.close(false);
                HashMap<String, String> comment = new HashMap<String, String>();
                comment.put("text", edittext.getText().toString());
                mGasService.addComment(mGas.getId(), comment).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 201){
                            Toast.makeText(self, "Comentário enviado", Toast.LENGTH_SHORT);
                        }else{
                            //TODO tratar tretas
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
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
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Comentários");

        mGasService.getComments(mGas.getId()).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if(response.code() == 200){
                    List<Comments> comments = response.body();
                    final CommentAdapter arrayAdapter = new CommentAdapter(builderSingle.getContext(), comments);
                    builderSingle.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, null);
                    builderSingle.show();
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                System.out.println("Treta: " + t.toString());
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.fab_add_comment:
                    openAddCommentDialog();
                    //Snackbar.make(v, fabWrongLocal.getLabelText() , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                case R.id.fab_wrong_local:
                    reportLocation(v);
                    break;
                case R.id.fab_closed_gas_station:
                    reportClosed(v);
                    break;
                case R.id.fab_wrong_prices:
                    reportWrongPrice(v);
                    break;
            }
        }
    };

    private void reportWrongPrice(final View v) {
        mGasService.reportWrongPrice(mGas.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 400){
                    Snackbar.make(v, "Esta preço já foi reportado por você", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else if(response.code() == 201){
                    Snackbar.make(v,"Preço incorreto reportado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void reportClosed(final View v) {
        mGasService.reportClosed(mGas.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 400){
                    Snackbar.make(v, "Esta posto já foi reportado por você", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else if(response.code() == 201){
                    Snackbar.make(v,"Posto reportado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void reportLocation(final View v) {
        System.out.println("Reporting for id: " + mGas.getId());
        mGasService.reportWrongLocation(mGas.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 400){
                    System.out.println("Already Reported");
                    Snackbar.make(v, "Esta loclaização já foi reportada por você", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else if(response.code() == 201){
                    System.out.println("Reported");
                    Snackbar.make(v,"Localização reportada", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("TretA: " + t.toString());
            }
        });
    }
}
