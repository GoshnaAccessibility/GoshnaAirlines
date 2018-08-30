package com.example.wquist.goshnaairlines.ApiResponse;

import java.util.List;

import com.example.wquist.goshnaairlines.Api.Airline;

public class AirlineResponse {
    public List<Airline> airlines;

    public AirlineResponse(List<Airline> flights) {
        this.airlines = flights;
    }
}
