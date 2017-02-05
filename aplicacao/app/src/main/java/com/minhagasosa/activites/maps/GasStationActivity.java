package com.minhagasosa.activites.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.minhagasosa.API.GasStationService;
import com.minhagasosa.R;
import com.minhagasosa.Transfer.Comments;
import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.activites.BaseActivity;
import com.minhagasosa.adapters.CommentAdapter;

import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private final int CARD_ICON_WIDTH = 85;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComments = new ArrayList<>();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();


        mGas = (GasStation) bundle.getParcelable("gas");
        mGasService = retrofit.create(GasStationService.class);

        setContentView(R.layout.activity_gas_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(mGas.getName());

        RatingBar rating = (RatingBar) findViewById(R.id.rating);
        rating.setRating(mGas.getRating().floatValue());

        TextView tvGasPrice = (TextView) findViewById(R.id.tv_gasPage_gasPrice);
        TextView tvGasPlusPrice = (TextView) findViewById(R.id.tv_gasPage_gasPlusPrice);
        TextView tvAlcoolPrice = (TextView) findViewById(R.id.tv_gasPage_alcoolPrice);
        TextView tvCredit = (TextView) findViewById(R.id.tvCredit);
        TextView tvDebit = (TextView) findViewById(R.id.tvDebit);
        TextView tvEndereco = (TextView) findViewById(R.id.tv_endereco);

        tvGasPrice.setText("R$ " + String.format("%.2f", mGas.getGasPrice()));
        tvGasPlusPrice.setText("R$ " + String.format("%.2f", mGas.getGasPlusPrice()));
        tvAlcoolPrice.setText("R$ " + String.format("%.2f", mGas.getAlcoolPrice()));

        mGasService.getComments(mGas.getId()).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if(response.code() == 200){
                    List<Comments> comments = response.body();
                    MyAdapter adapter = new MyAdapter(comments);
                    RecyclerView rv = (RecyclerView) findViewById(R.id.rv_recycler_view);
                    rv.setHasFixedSize(true);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setItemAnimator(new DefaultItemAnimator());
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                System.out.println("Treta: " + t.toString());
            }
        });


        final Activity self = this;
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float v, boolean b) {
                HashMap<String, Double> rating = new HashMap<String, Double>();
                rating.put("rating", new Double(v));
                mGasService.sendRating(mGas.getId(), rating).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 201) {
                            System.out.println("rated");
                            Snackbar.make(ratingBar, "Posto classificado", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }else if(response.code() == 400){
                            System.out.println("already");
                            Snackbar.make(ratingBar, "Você já classificou este posto", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }else{
                            System.out.println("huh?");
                            //TODO
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("Treta: " + t.toString());
                    }
                });

            }
        });

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(mGas.getLocation().getLat(), mGas.getLocation().getLng(), 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                StringBuilder strReturnedAddress = new StringBuilder();
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
//                Log.d("Endereço", strReturnedAddress.toString());
                tvEndereco.setText(strReturnedAddress.toString());
//                tvEndereco.setText(address);
            }
            else{
                Log.d("Endereço", "Nao retornou");
                 tvEndereco.setText("No Address returned!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Endereço", "Cannot get Address!");
            tvEndereco.setText("Cannot get Address!");//and tell me what is
        }

//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        addresses = geocoder.getFromLocation(mGas.getLocation().getLat(), mGas.getLocation().getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        if(mGas.getPayamentsCredit() != null && !mGas.getPayamentsCredit().isEmpty()){
            GridLayout glCredit = (GridLayout) findViewById(R.id.layoutCredito);

            glCredit.removeAllViews();

            int colCount = 4;
            int rowCount = (int) Math.ceil((mGas.getPayamentsCredit().size() / colCount));

            glCredit.setColumnCount(colCount);
            glCredit.setRowCount(rowCount);

            for(int i=0; i<mGas.getPayamentsCredit().size(); i++){
                ImageView imv = new ImageView(this);
                int id = imv.getContext().getResources().getIdentifier(mGas.getPayamentsCredit().get(i).toLowerCase(), "drawable", getPackageName());
                imv.setImageResource(id);
                if(imv != null){
                    Log.d("card", mGas.getPayamentsCredit().get(i));
                    imv.setLayoutParams(new LayoutParams
                            (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    glCredit.addView(imv);
                }
            }

        }else{
            tvCredit.setVisibility(View.GONE);
        }

        if(mGas.getPayamentsDebit() != null && !mGas.getPayamentsDebit().isEmpty()){
            GridLayout glDebit = (GridLayout) findViewById(R.id.layoutDebito);

            glDebit.removeAllViews();

            int colCount = 4;
            int rowCount = (int) Math.ceil((mGas.getPayamentsDebit().size() / colCount));

            glDebit.setColumnCount(colCount);
            glDebit.setRowCount(rowCount);

            for(int i=0; i<mGas.getPayamentsDebit().size(); i++){
                ImageView imv = new ImageView(this);
                int id = imv.getContext().getResources().getIdentifier(mGas.getPayamentsDebit().get(i).toLowerCase()+"_d", "drawable", getPackageName());
                imv.setImageResource(id);
                if(imv != null){
                    Log.d("card", mGas.getPayamentsDebit().get(i));
                    imv.setLayoutParams(new LayoutParams
                            (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    glDebit.addView(imv);
                }
            }
        }else{
            tvDebit.setVisibility(View.GONE);
        }

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
                            Snackbar.make(edittext, "Comentário enviado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
