package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.StantionItemBinding;
import com.example.samsungschoolproject.model.Training;

import java.util.List;

public class TrainListAdapter extends RecyclerView.Adapter<TrainListAdapter.TrainViewHolder> {

    private final List<Training> trainings;

    public TrainListAdapter(List<Training> items) {
        trainings = items;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stantion_item,
                parent,
                false
        );
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrainViewHolder holder, int position) {
        holder.bind(trainings.get(position));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public class TrainViewHolder extends RecyclerView.ViewHolder {
        private StantionItemBinding stantionsItemBinding;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            stantionsItemBinding = StantionItemBinding.bind(itemView);
        }

        public void bind(Training training){
            stantionsItemBinding.name.setText(training.name);
            stantionsItemBinding.line.setText(training.line);
            stantionsItemBinding.color.setText(training.color);
            // MUST BE ADDED A LOGIC !!! (cuz now idk how to make it correctly lol)
        }
    }

    public void Add(Training training){
        trainings.add(training);
        notifyDataSetChanged();
    }
}