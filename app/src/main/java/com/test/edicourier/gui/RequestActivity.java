package com.test.edicourier.gui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.edicourier.Constants;
import com.test.edicourier.NetworkEngine;
import com.test.edicourier.R;
import com.test.edicourier.SessionToken;
import com.test.edicourier.TypedNetworkCallback;
import com.test.edicourier.model.RequestUid;
import com.test.edicourier.utils.Utilities;

public class RequestActivity extends AppCompatActivity {

    private final static String TAG = "SUPERLOG_RequestActivity";
    private final static String REQUEST_KEY = "request";
    private final static String EMPTY_REQUEST_KEY = "emptyRequest";
    private SwipeRefreshLayout swipeContainer;
    TextView textViewClient, textViewMobilePhoneFull, textViewUidFull,
            textViewCityFull, textViewAddressFull, textViewMeeTingDateFull, textViewTimeWindowFull,
            textViewProductFull, textViewDateKkoFull, textViewAssignDateFull, textViewSdoFull;
    String uidIntent;
    RequestUid requestUid;
    boolean isEmptyRequest = false;

    @Override
    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        uidIntent = getIntent().getStringExtra(Constants.INTENT_UID_REQUEST);

        textViewClient = findViewById(R.id.textViewClientFull);
        textViewMobilePhoneFull = findViewById(R.id.textViewMobilePhoneFull);
        textViewUidFull = findViewById(R.id.textViewUidFull);
        textViewCityFull = findViewById(R.id.textViewCityFull);
        textViewAddressFull = findViewById(R.id.textViewAddressFull);
        textViewMeeTingDateFull = findViewById(R.id.textViewMeeTingDateFull);
        textViewTimeWindowFull = findViewById(R.id.textViewTimeWindowFull);
        textViewProductFull = findViewById(R.id.textViewProductFull);
        textViewDateKkoFull = findViewById(R.id.textViewDateKkoFull);
        textViewAssignDateFull = findViewById(R.id.textViewAssignDateFull);
        textViewSdoFull = findViewById(R.id.textViewSdoFull);

        swipeContainer = findViewById(R.id.swipeContainerRequest);
        swipeContainer.setOnRefreshListener(this::update);

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark);

        if (savedInstanceState == null) {
            update();
        }
    }

    @Override
    @SuppressLint("LongLogTag")
    protected void onResume() {
        super.onResume();
        textViewClient.setVisibility(View.GONE);
        textViewMobilePhoneFull.setVisibility(View.GONE);
        textViewUidFull.setVisibility(View.GONE);
        textViewCityFull.setVisibility(View.GONE);
        textViewAddressFull.setVisibility(View.GONE);
        textViewMeeTingDateFull.setVisibility(View.GONE);
        textViewTimeWindowFull.setVisibility(View.GONE);
        textViewProductFull.setVisibility(View.GONE);
        textViewDateKkoFull.setVisibility(View.GONE);
        textViewAssignDateFull.setVisibility(View.GONE);
        textViewSdoFull.setVisibility(View.GONE);
        if (requestUid != null) {
            fillingTextView(requestUid);
        }
        if (isEmptyRequest) {
            textViewClient.setVisibility(View.VISIBLE);
            String message = getText(R.string.no_uid_data) + uidIntent;
            textViewClient.setText(message);
        }
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(REQUEST_KEY, requestUid);
        outState.putBoolean(EMPTY_REQUEST_KEY, isEmptyRequest);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        requestUid = savedInstanceState.getParcelable(REQUEST_KEY);
        isEmptyRequest = savedInstanceState.getBoolean(EMPTY_REQUEST_KEY);
    }

    private void update() {
        if (!swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(true);
        }
        if (Utilities.getInstance().isOnline(RequestActivity.this)) {
            String newToken = SessionToken.getInstance().getNewToken(getApplicationContext()); // Получаем новый токен
            NetworkEngine.getInstance().loadRequestUid(newToken, uidIntent, getRequestUidCallback());
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.no_internet), Toast.LENGTH_SHORT).show();
            swipeContainer.setRefreshing(false);
        }
    }

    private TypedNetworkCallback<RequestUid> getRequestUidCallback() {
        return new TypedNetworkCallback<RequestUid>() {

            @Override
            @SuppressLint("LongLogTag")
            public void onLoad(RequestUid request) {
                requestUid = request;
                fillingTextView(requestUid);
                isEmptyRequest = false;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onError(String errorString) {
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                textViewClient.setVisibility(View.VISIBLE);
                String message = getText(R.string.no_uid_data) + uidIntent;
                textViewClient.setText(message);
                isEmptyRequest = true;
                swipeContainer.setRefreshing(false);
            }
        };
    }

    /*
    private void setTextView(String text, int id) {
        TextView textView = findViewById(id);
        if (!"".equals(text)){
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
     */

    // Упростить эту портянку элегантно не вышло(
    @SuppressLint("LongLogTag")
    private void fillingTextView(RequestUid requestUid) {
        // setTextView(requestUid.getCity(), R.id.textViewClient);
        if (requestUid.getClient() != null && !"".equals(requestUid.getClient())) {
            textViewClient.setText(requestUid.getClient());
            textViewClient.setVisibility(View.VISIBLE);
        } else {
            textViewClient.setVisibility(View.GONE);
        }

        if (requestUid.getMobilePhone() != null && !"".equals(requestUid.getMobilePhone())) {
            textViewMobilePhoneFull.setText(requestUid.getMobilePhone());
            textViewMobilePhoneFull.setVisibility(View.VISIBLE);
        } else {
            textViewMobilePhoneFull.setVisibility(View.GONE);
        }

        if (requestUid.getUid() != null && !"".equals(requestUid.getUid())) {
            textViewUidFull.setText(requestUid.getUid());
            textViewUidFull.setVisibility(View.VISIBLE);
        } else {
            textViewUidFull.setVisibility(View.GONE);
        }

        if (requestUid.getCity() != null && !"".equals(requestUid.getCity())) {
            textViewCityFull.setText(requestUid.getCity());
            textViewCityFull.setVisibility(View.VISIBLE);
        } else {
            textViewCityFull.setVisibility(View.GONE);
        }

        if (requestUid.getAddress() != null && !"".equals(requestUid.getAddress())) {
            textViewAddressFull.setText(requestUid.getAddress());
            textViewAddressFull.setVisibility(View.VISIBLE);
        } else {
            textViewAddressFull.setVisibility(View.GONE);
        }

        if (requestUid.getMeetingDate() != null && !"".equals(requestUid.getMeetingDate())) {
            textViewMeeTingDateFull.setText(requestUid.getMeetingDate());
            textViewMeeTingDateFull.setVisibility(View.VISIBLE);
        } else {
            textViewMeeTingDateFull.setVisibility(View.GONE);
        }

        if (requestUid.getTimeWindow() != null && !"".equals(requestUid.getTimeWindow())) {
            textViewTimeWindowFull.setText(requestUid.getTimeWindow());
            textViewTimeWindowFull.setVisibility(View.VISIBLE);
        } else {
            textViewTimeWindowFull.setVisibility(View.GONE);
        }

        if (requestUid.getProduct() != null && !"".equals(requestUid.getProduct())) {
            textViewProductFull.setText(requestUid.getProduct());
            textViewProductFull.setVisibility(View.VISIBLE);
        } else {
            textViewProductFull.setVisibility(View.GONE);
        }

        if (requestUid.getDateKko() != null && !"".equals(requestUid.getDateKko())) {
            textViewDateKkoFull.setText(requestUid.getDateKko());
            textViewDateKkoFull.setVisibility(View.VISIBLE);
        } else {
            textViewDateKkoFull.setVisibility(View.GONE);
        }

        if (requestUid.getAssignDate() != null && !"".equals(requestUid.getAssignDate())) {
            textViewAssignDateFull.setText(requestUid.getAssignDate());
            textViewAssignDateFull.setVisibility(View.VISIBLE);
        } else {
            textViewAssignDateFull.setVisibility(View.GONE);
        }

        if (requestUid.getSdo() != null && !"".equals(requestUid.getSdo())) {
            textViewSdoFull.setText(requestUid.getSdo());
            textViewSdoFull.setVisibility(View.VISIBLE);
        } else {
            textViewSdoFull.setVisibility(View.GONE);
        }
    }
}
