package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.StantionItemBinding;
import com.example.samsungschoolproject.model.Station;

import java.util.List;

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.StationViewHolder> {

    private final List<Station> stations;

    public StationListAdapter(List<Station> items) {
        stations = items;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stantion_item,
                parent,
                false
        );
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StationViewHolder holder, int position) {
        holder.bind(stations.get(position));
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public class StationViewHolder extends RecyclerView.ViewHolder {
        private StantionItemBinding stantionsItemBinding;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            stantionsItemBinding = StantionItemBinding.bind(itemView);
        }

        public void bind(Station station){
            stantionsItemBinding.name.setText(station.name);
            stantionsItemBinding.line.setText(station.line);
            stantionsItemBinding.color.setText(station.color);
            // MUST BE ADDED A LOGIC !!! (cuz now idk how to make it correctly lol)
        }
    }

    public void Add(Station station){
        stations.add(station);
        notifyDataSetChanged();
    }
}