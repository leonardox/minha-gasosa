package com.minhagasosa.API;

import com.minhagasosa.Transfer.GasStation;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by GAEL on 10/12/2016.
 */

public interface GasStationService {
    @GET("gas/")
    Call<List<GasStation>> getAllGasStation();

    @PUT("gas/wrong-price/{id}")
    public Call<ResponseBody> reportWrongPrice(
            @Path("id") String gasId);

    @PUT("gas/wrong-location/{id}")
    public Call<ResponseBody> reportWrongLocation(
            @Path("id") String gasId);

    @PUT("gas/closed/{id}")
    public Call<ResponseBody> reportClosed(
            @Path("id") String gasId);
}
