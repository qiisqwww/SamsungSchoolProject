package com.example.samsungschoolproject.view_adapter.workout.info;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.DTO.TemplateInfo;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;

import java.util.List;

public class TemplateInfoAdapter extends RecyclerView.Adapter {
    private TemplateInfo templateInfo;

    public TemplateInfoAdapter(TemplateInfo templateInfo){
        this.templateInfo = templateInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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
