package com.test.edicourier.gui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.edicourier.ApplicationAdapter;
import com.test.edicourier.NetworkEngine;
import com.test.edicourier.R;
import com.test.edicourier.SessionToken;
import com.test.edicourier.TypedNetworkCallback;
import com.test.edicourier.model.Application;
import com.test.edicourier.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {

    private final static String TAG = "SUPERLOG_LoginActivity";
    private final static String APPLICATION_LIST_KEY = "applicationList";
    private final static String DATE_FROM_KEY = "fulldataFrom";
    private final static String DATE_TO_KEY = "fulldataTo";
    public static final int CODE_REQUEST = 100;
    private SwipeRefreshLayout swipeContainer;
    private ApplicationAdapter adapter;
    private ArrayList<Application> applicationList = new ArrayList<>();
    TextView textViewDateFrom, textViewDateTo;
    Button buttonSelectDateFrom, buttonSelectDateTo;
    int yearFrom, monthFrom, dayFrom;
    int yearTo, monthTo, dayTo;
    String fullDateFrom, fullDateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        textViewDateFrom = findViewById(R.id.textViewDateFrom);
        textViewDateTo = findViewById(R.id.textViewDateTo);
        buttonSelectDateFrom = findViewById(R.id.buttonDateFrom);
        buttonSelectDateTo = findViewById(R.id.buttonDateTo);

        Calendar calendar = Calendar.getInstance();
        yearFrom = calendar.get(Calendar.YEAR);
        monthFrom = calendar.get(Calendar.MONTH) + 1;
        dayFrom = calendar.get(Calendar.DAY_OF_MONTH);
        yearTo = calendar.get(Calendar.YEAR);
        monthTo = calendar.get(Calendar.MONTH) + 1;
        dayTo = calendar.get(Calendar.DAY_OF_MONTH);

        fullDateFrom = Utilities.getInstance().dateInKanadaFormat(yearFrom, monthFrom, dayFrom);
        fullDateTo = Utilities.getInstance().dateInKanadaFormat(yearTo, monthTo, dayTo);

        buttonSelectDateFrom.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ApplicationActivity.this,
                    R.style.DatePickerDialogTheme, (datePicker, mYear, monthOfYear, dayOfMonth) -> {
                yearFrom = mYear;
                monthFrom = monthOfYear + 1;
                dayFrom = dayOfMonth;
                fullDateFrom = Utilities.getInstance().dateInKanadaFormat(yearFrom, monthFrom, dayFrom);
                textViewDateFrom.setText(fullDateFrom);
            }, yearFrom, monthFrom - 1, dayFrom);
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorDialogBackgroundButton));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorDialogBackgroundButton));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        });

        buttonSelectDateTo.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ApplicationActivity.this,
                    R.style.DatePickerDialogTheme, (datePicker, mYear, monthOfYear, dayOfMonth) -> {
                yearTo = mYear;
                monthTo = monthOfYear + 1;
                dayTo = dayOfMonth;
                fullDateTo = Utilities.getInstance().dateInKanadaFormat(yearTo, monthTo, dayTo);
                textViewDateTo.setText(fullDateTo);
            }, yearTo, monthTo - 1, dayTo);
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorDialogBackgroundButton));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorDialogBackgroundButton));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this::update);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApplicationAdapter(this, applicationList);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            update();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewDateFrom.setText(fullDateFrom);
        textViewDateTo.setText(fullDateTo);
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATE_FROM_KEY, fullDateFrom);
        outState.putString(DATE_TO_KEY, fullDateTo);
        outState.putParcelableArrayList(APPLICATION_LIST_KEY, applicationList);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fullDateFrom = savedInstanceState.getString(DATE_FROM_KEY);
        fullDateTo = savedInstanceState.getString(DATE_TO_KEY);
        applicationList.clear();
        List<Application> list = savedInstanceState.getParcelableArrayList(APPLICATION_LIST_KEY);
        if (list != null) {
            applicationList.addAll(list);
        }
    }

    private void update() {
        if (!swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(true);
        }

        if (Utilities.getInstance().isOnline(ApplicationActivity.this)) {
            String newToken = SessionToken.getInstance().getNewToken(getApplicationContext()); // Получаем новый токен
            NetworkEngine.getInstance().loadApplicationList(newToken, fullDateFrom, fullDateTo, getApplicationListCallback());
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.no_internet), Toast.LENGTH_SHORT).show();
            swipeContainer.setRefreshing(false);
        }
    }

    private TypedNetworkCallback<List<Application>> getApplicationListCallback() {
        return new TypedNetworkCallback<List<Application>>() {
            @Override
            public void onLoad(List<Application> applications) {
                applicationList.clear();
                applicationList.addAll(applications);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onError(String errorString) {
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        };
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}