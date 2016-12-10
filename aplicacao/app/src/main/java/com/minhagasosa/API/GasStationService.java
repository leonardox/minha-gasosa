package com.minhagasosa.API;

import com.minhagasosa.Transfer.GasStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by GAEL on 10/12/2016.
 */

public interface GasStationService {
    @GET("gas/")
    Call<List<GasStation>> getAllGasStation();
}
