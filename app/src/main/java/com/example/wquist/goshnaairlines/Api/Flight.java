package com.example.wquist.goshnaairlines.Api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight {
    public int id;
    public int airline_id;
    public String dest_airport;
    public int flight_number;
    public String gate;
    public int departure;

    public Flight(int airline_id, String dest_airport, int flight_number, String gate, int departure) {
        this.id = -1;
        this.airline_id = airline_id;
        this.dest_airport = dest_airport;
        this.flight_number = flight_number;
        this.gate = gate;
        this.departure = departure;
    }

    public String getTime() {
        Date d = new Date(departure * 1000L);
        return new SimpleDateFormat("HH:mm").format(d);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "airline=" + airline_id +
                ", dest_airport='" + dest_airport + '\'' +
                ", flight_number=" + flight_number +
                ", gate='" + gate + '\'' +
                ", departure=" + departure +
                ", id=" + id +
                '}';
    }
}
