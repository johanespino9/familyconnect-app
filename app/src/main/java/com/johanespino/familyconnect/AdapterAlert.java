package com.johanespino.familyconnect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAlert extends RecyclerView.Adapter<AdapterAlert.MyHolder> {
    Context context;
    List<ModelAlert> AlertList;
    @NonNull
    @Override
    public AdapterAlert.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlert.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);

        }


    }
}
