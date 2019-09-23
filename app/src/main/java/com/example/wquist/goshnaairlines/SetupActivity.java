package com.example.wquist.goshnaairlines;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.example.wquist.goshnaairlines.Api.Airline;
import com.example.wquist.goshnaairlines.Api.Flight;
import com.example.wquist.goshnaairlines.ApiResponse.AirlineResponse;
import com.example.wquist.goshnaairlines.ApiResponse.FlightIdResponse;

public class SetupActivity extends AppCompatActivity {
    private Context mContext;

    private Spinner mAirline;
    private List<Airline> mAirlineChoices;

    private EditText mGate;
    private EditText mFlightNumber;
    private EditText mDestination;

    private EditText mDeparture;
    private RadioButton mToday;
    private RadioButton mTomorrow;

    private Button mSubmit;
    private Button mPrevious;

    private Flight mFlight;

    private DialogInterface.OnClickListener retryYesListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            GoshnaAirlines.getApi().getAirlines(airlinesCallback);
        }
    };

    private Callback<AirlineResponse> airlinesCallback = new Callback<AirlineResponse>() {
        @Override
        public void success(AirlineResponse response, Response clientResponse) {
            mSubmit.setEnabled(true);
            mAirlineChoices = response.airlines;

            List<String> airlines = new ArrayList<>();
            for (Airline a : response.airlines)
                airlines.add(a.airline_full);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, airlines);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mAirline.setAdapter(adapter);
        }

        @Override
        public void failure(RetrofitError error) {
            mSubmit.setEnabled(false);

            new AlertDialog.Builder(mContext)
                    .setTitle("Connection Error")
                    .setMessage("Could not connect to the Goshna airport server.")
                    .setPositiveButton(R.string.retry, retryYesListener)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    };

    private Callback<FlightIdResponse> flightCallback = new Callback<FlightIdResponse>() {
        @Override
        public void success(FlightIdResponse response, Response clientResponse) {
            mSubmit.setEnabled(true);

            SharedPreferences.Editor e = GoshnaAirlines.getPreferences().edit();
            e.putString(getString(R.string.preferences_gate), mFlight.gate);
            e.putInt(getString(R.string.preferences_flight_id), response.id);
            e.apply();

            mPrevious.setText(mFlight.gate);
            mPrevious.setEnabled(true);

            Intent it = new Intent(mContext, MessageActivity.class);
            startActivity(it);
        }

        @Override
        public void failure(RetrofitError error) {
            mSubmit.setEnabled(true);

            Toast.makeText(mContext, R.string.bad_flight, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        mAirline = findViewById(R.id.airline);
        mGate = findViewById(R.id.gate);
        mFlightNumber = findViewById(R.id.flight_number);
        mDestination = findViewById(R.id.destination);

        mDeparture = findViewById(R.id.departure);
        mToday = findViewById(R.id.today);
        mTomorrow = findViewById(R.id.tomorrow);

        mSubmit = findViewById(R.id.submit);
        mPrevious = findViewById(R.id.previous);

        String gate = GoshnaAirlines.getPreferences().getString(getString(R.string.preferences_gate), "");
        if (gate.equals("")) {
            mPrevious.setText(getString(R.string.no_gate));
            mPrevious.setEnabled(false);
        } else {
            mPrevious.setText(gate);
            mPrevious.setEnabled(true);
        }

        mSubmit.setEnabled(false);
        GoshnaAirlines.getApi().getAirlines(airlinesCallback);
    }

    public void submit(View v) {
        Airline a = mAirlineChoices.get(mAirline.getSelectedItemPosition());
        boolean bErr = false; // An error occured

        String g = mGate.getText().toString();
        if (g.trim().equalsIgnoreCase("")) {
            String err = mContext.getResources().getString(R.string.empty_gate);
            mGate.setError(err);
            bErr = true;
        }

        String d = mDestination.getText().toString();
        if (d.trim().equalsIgnoreCase("")) {
            String err = mContext.getResources().getString(R.string.empty_dest);
            mDestination.setError(err);
            bErr = true;
        }

        int n = 0;
        try {
            n = Integer.parseInt(mFlightNumber.getText().toString());
        } catch (Exception e) {
            String err = mContext.getResources().getString(R.string.empty_flight);
            mFlightNumber.setError(err);
            bErr = true;
        }

        int t = 0;
        try {
            DateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date time = fmt.parse(mDeparture.getText().toString());

            Calendar then = Calendar.getInstance();
            then.setTime(time);

            int hour = then.get(Calendar.HOUR_OF_DAY);
            int minute = then.get(Calendar.MINUTE);

            then.setTime(new Date());
            if (mTomorrow.isChecked())
                then.add(Calendar.DATE, 1);

            then.set(Calendar.HOUR_OF_DAY, hour);
            then.set(Calendar.MINUTE, minute);

            t = (int)(then.getTime().getTime() / 1000L);
        } catch (Exception e) {
            String err = mContext.getResources().getString(R.string.empty_date);
            mDeparture.setError(err);
            bErr = true;
        }

        if(!bErr) {
            mFlight = new Flight(a.id, d, n, g, t);
            mSubmit.setEnabled(false);
            GoshnaAirlines.getApi().addFlight(mFlight, flightCallback);
        }
    }

    public void previous(View v) {
        Intent it = new Intent(mContext, MessageActivity.class);
        startActivity(it);
    }
}
