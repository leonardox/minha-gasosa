package com.minhagasosa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minhagasosa.API.GasStationService;
import com.minhagasosa.Transfer.Comments;
import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.activites.BaseActivity;
import com.minhagasosa.activites.maps.MyAdapter;
import com.minhagasosa.adapters.CommentAdapter2;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListActivity extends BaseActivity implements CommentAdapter2.OnLoadMoreListener
        ,SwipeRefreshLayout.OnRefreshListener{

    private GasStation mGas;
    private GasStationService mGasService;

    private CommentAdapter2 mAdapter;
    private List<Comments> comments;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        mGas = (GasStation) bundle.getParcelable("gas");
        mGasService = retrofit.create(GasStationService.class);
        comments = new ArrayList<>();

        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentAdapter2(this);
        mAdapter.setLinearLayoutManager(mLayoutManager);
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CommentListActivity","onStart");
        loadData();
    }

    @Override
    public void onRefresh() {
        Log.d("CommentListActivity","onRefresh");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                loadData();

            }
        },2000);
    }

    @Override
    public void onLoadMore() {
        Log.d("CommentListActivity","onLoadMore");
        Toast.makeText(getApplicationContext(), "Carregar mais!!!", Toast.LENGTH_SHORT).show();
//        mAdapter.setProgressMore(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                itemList.clear();
//                mAdapter.setProgressMore(false);
//                int start = mAdapter.getItemCount();
//                int end = start + 15;
//                for (int i = start + 1; i <= end; i++) {
//                    itemList.add(new Comments("Comment " + i));
//                }
//                mAdapter.addItemMore(itemList);
//                mAdapter.setMoreLoading(false);
//            }
//        },2000);
    }

    private void loadData() {
//        comments.clear();
//        for (int i = 1; i <= 20; i++) {
//            itemList.add(new Comments("Comment " + i));
//        }
        mGasService.getComments(mGas.getId()).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if(response.code() == 200){
                    comments = response.body();

                    Collections.reverse(comments);
                    mAdapter.addAll(comments);
                }
            }
            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                System.out.println("Error: " + t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
