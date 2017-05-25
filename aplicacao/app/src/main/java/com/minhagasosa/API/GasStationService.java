package com.minhagasosa.API;

import com.minhagasosa.Transfer.Comments;
import com.minhagasosa.Transfer.GasStation;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @PUT("gas/comment/{id}")
    public Call<ResponseBody> addComment(
            @Path("id") String gasId,
            @Body HashMap<String, String> comment);

    @PUT("gas/rating/{id}")
    public Call<ResponseBody> sendRating(
            @Path("id") String gasId,
            @Body HashMap<String, Double> rating);

    @GET("gas/comment/{id}")
    public Call<List<Comments>> getComments(
            @Path("id") String gasId);
    @GET("gas/{id}")
    public Call<GasStation> getGasStation(
            @Path("id") String gasId);
}
