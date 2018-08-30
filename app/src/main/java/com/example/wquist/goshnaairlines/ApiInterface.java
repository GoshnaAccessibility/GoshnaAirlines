package com.example.wquist.goshnaairlines;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.example.wquist.goshnaairlines.Api.Flight;
import com.example.wquist.goshnaairlines.Api.Message;
import com.example.wquist.goshnaairlines.ApiResponse.*;

public interface ApiInterface {
    @POST("/flights")
    void addFlight(@Body Flight flight, Callback<FlightIdResponse> response);

    @GET("/airlines")
    void getAirlines(Callback<AirlineResponse> response);

    @POST("/flights/messages/{flight_id}")
    void createMessage(@Path("flight_id") int flight_id, @Body Message message, Callback<MessageIdResponse> response);
}
