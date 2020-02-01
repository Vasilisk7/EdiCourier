package com.test.edicourier;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.edicourier.gui.ApplicationActivity;
import com.test.edicourier.gui.RequestActivity;
import com.test.edicourier.model.Application;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private final static String TAG = "SUPERLOG_LoginActivity";
    private Activity context;
    private List<Application> applicationList;

    public ApplicationAdapter(Activity context, List<Application> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCity, textViewUid, textViewAddress, textViewClient, textViewMeeTingDate, textViewTimeWindow;
        CardView cardViewApplication;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            cardViewApplication = itemView.findViewById(R.id.cardViewApplication);
            textViewCity = itemView.findViewById(R.id.textViewCity);
            textViewUid = itemView.findViewById(R.id.textViewUid);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewClient = itemView.findViewById(R.id.textViewClient);
            textViewMeeTingDate = itemView.findViewById(R.id.textViewMeeTingDate);
            textViewTimeWindow = itemView.findViewById(R.id.textViewTimeWindow);
        }
    }

    @Override
    @NonNull
    public ApplicationViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.application_row, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        Application application = applicationList.get(position);
        holder.textViewCity.setText(application.getCity());
        holder.textViewUid.setText(application.getUid());
        holder.textViewAddress.setText(application.getAddress());
        holder.textViewClient.setText(application.getClient());
        holder.textViewMeeTingDate.setText(application.getMeetingDate());
        holder.textViewTimeWindow.setText(application.getTimeWindow());

        // Нажимаем на карточку
        holder.cardViewApplication.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestActivity.class);
            intent.putExtra(Constants.INTENT_UID_REQUEST, application.getUid());
            context.startActivityForResult(intent, ApplicationActivity.CODE_REQUEST);
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }
}
