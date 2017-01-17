package com.minhagasosa.API;

import com.minhagasosa.Transfer.City;
import com.minhagasosa.Transfer.GasStation;
import com.minhagasosa.Transfer.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by GAEL on 16/01/2017.
 */

public interface LocationService {

    @GET("/states")
    Call<List<State>> getStates();

    @GET("/cities")
    Call<List<City>> getCities(@Query("state") String stateId);
}
