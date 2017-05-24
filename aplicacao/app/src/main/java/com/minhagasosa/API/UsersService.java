package com.minhagasosa.API;

import android.database.Observable;

import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.Transfer.TUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by GAEL on 18/12/2016.
 */

public interface UsersService {
    @GET("/auth")
    public Call<ResponseBody> Auth(
            @Query("fbToken") String token);

    @POST("/register")
    Call<ResponseBody> registerUser(
            @Body TUser us
    );

    @POST("user/update-token/{token}")
    public Call<ResponseBody> updateUserToken(
            @Path("token") String token);
}
