package com.example.samsungschoolproject.view_adapter.workout.info;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.DTO.TemplateInfo;

public class TemplateInfoAdapter extends RecyclerView.Adapter<TemplateInfoAdapter.TemplateViewHolder> {
    private TemplateInfo templateInfo;

    public TemplateInfoAdapter(TemplateInfo templateInfo){
        this.templateInfo = templateInfo;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1 + templateInfo.exercisesInfo.size(); // Поле для названия + количество упражнений
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
