package com.example.samsungschoolproject.view_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener){
        this.days = days;
        this.onItemListener = onItemListener;
    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (days.size() > 15){
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        }
        else{
            layoutParams.height = (int) (parent.getHeight());
        }
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if (date == null){
            holder.day.setText("");
        }
        else{
            holder.day.setText(String.valueOf(date.getDayOfMonth()));
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView day;
        private final CalendarAdapter.OnItemListener onItemListener;

        public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            onItemListener.onItemClick(getAdapterPosition(), (String) day.getText());
        }
    }
}
