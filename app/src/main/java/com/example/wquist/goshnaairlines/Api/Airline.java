package com.example.wquist.goshnaairlines.Api;

public class Airline {
    public int id;
    public String airline_short;
    public String airline_full;

    public Airline(int id, String airline_short, String airline_full) {
        this.id = id;
        this.airline_short = airline_short;
        this.airline_full = airline_full;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "id=" + id +
                ", airline_short='" + airline_short + '\'' +
                ", airline_full='" + airline_full + '\'' +
                '}';
    }
}
