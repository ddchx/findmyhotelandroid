package com.findmyhotel;

import com.findmyhotel.model.Hotel;
import com.findmyhotel.model.HotelMessage;
import com.findmyhotel.model.Hotels;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Chathuranga on 11/27/15.
 */
public interface ServerAPI {
    @GET("/hotel")
    Call<Hotels> loadHotels();

   // @POST("/hotel")
   // void addHotel(@Body Hotel hotel, Callback<Hotel> cb);

    @POST("/hotel")
    Call<HotelMessage> addHotel(@Body Hotel hotel);
}
