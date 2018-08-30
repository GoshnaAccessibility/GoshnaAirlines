package com.example.wquist.goshnaairlines;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.example.wquist.goshnaairlines.Api.Message;
import com.example.wquist.goshnaairlines.ApiResponse.MessageIdResponse;

public class MessageActivity extends AppCompatActivity {
    private static final int STT_REQUEST = 1129;

    private Context mContext;

    private EditText mMessage;

    private Button mSpeak;
    private Button mClear;
    private Button mSubmit;

    private Callback<MessageIdResponse> messageCallback = new Callback<MessageIdResponse>() {
        @Override
        public void success(MessageIdResponse response, Response clientResponse) {
            clear(null);
            mSubmit.setEnabled(true);
        }

        @Override
        public void failure(RetrofitError error) {
            mSubmit.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        mMessage = findViewById(R.id.message);

        mSpeak = findViewById(R.id.speak);
        mClear = findViewById(R.id.clear);
        mSubmit = findViewById(R.id.submit);
    }

    public void speak(View v) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, STT_REQUEST);
    }

    public void clear(View v) {
        mMessage.setText("");
    }

    public void submit(View v) {
        int flight_id = GoshnaAirlines.getPreferences().getInt(getString(R.string.preferences_flight_id), 0);
        Message m = new Message(mMessage.getText().toString());

        GoshnaAirlines.getApi().createMessage(flight_id, m, messageCallback);

        mSubmit.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        switch (request) {
            case STT_REQUEST:
                if (result != RESULT_OK)
                    return;

                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (text.size() == 0)
                    return;

                mMessage.setText(text.get(0));
                break;
        }
    }
}
