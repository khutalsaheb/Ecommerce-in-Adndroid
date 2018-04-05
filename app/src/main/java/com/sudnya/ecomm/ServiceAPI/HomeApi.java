package com.sudnya.ecomm.ServiceAPI;

import com.sudnya.ecomm.Model.HomeModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * @ Created by Dell on 1/6/2017.
 */

public interface HomeApi {
    @GET("/catagory.php")
    void getHome(Callback<List<HomeModel>> responce);

}
