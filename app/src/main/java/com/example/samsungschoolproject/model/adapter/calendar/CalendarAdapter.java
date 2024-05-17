package com.example.samsungschoolproject.model.adapter.calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.model.repository.WorkoutRepository;
import com.example.samsungschoolproject.model.util.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnCalendarItemListener onCalendarItemListener;
    private final WorkoutRepository repository;

    public CalendarAdapter(ArrayList<LocalDate> days, OnCalendarItemListener onCalendarItemListener, WorkoutRepository repository){
        this.days = days;
        this.onCalendarItemListener = onCalendarItemListener;
        this.repository = repository;
    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        // Задает размер для "месячного" режима (в нем количество дней > 15)
        if (days.size() > 15){
            layoutParams.height = (int) (parent.getHeight() * 0.14);
        }
        // Задает размер для "недельного" режима
        else{
            layoutParams.height = (parent.getHeight());
        }

        return new CalendarViewHolder(view, onCalendarItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if (date == null){
            holder.day.setText("");
        }
        else{
            holder.day.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.selectedDate)){ // Устанавливает серый цвет, если элемент был выбран
                holder.itemView.setBackgroundColor(Color.parseColor("#B3808080"));
                CalendarUtils.selectedDatePosition = holder.getAdapterPosition();
                return;
            }
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                if (repository.getPlannedWorkoutsByDate(date.toString()).size() > 0) return "true";
                return "false";
            });
            try {
                if (future.get().equals("true")) holder.itemView.setBackgroundColor(Color.parseColor("#80BDECB6"));
                else holder.itemView.setBackgroundColor(Color.TRANSPARENT); // Очищает цвет, если элемент не должен быть подсвечен
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    // Обновляет две ячейки календаря: новую выбранную и предыдущую
    public void resetBacklitItem(int new_position){
        notifyItemChanged(new_position);
        notifyItemChanged(CalendarUtils.selectedDatePosition);
    }

    public interface OnCalendarItemListener {
        void onItemClick(int position, String dayText);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final TextView day;
        private final OnCalendarItemListener onCalendarItemListener;

        public CalendarViewHolder(@NonNull View itemView, OnCalendarItemListener onCalendarItemListener) {
            super(itemView);
            this.itemView = itemView;
            this.onCalendarItemListener = onCalendarItemListener;
            day = itemView.findViewById(R.id.day);

            setOnClickListeners();
        }

        private void setOnClickListeners(){
            itemView.setOnClickListener(v -> {
                onCalendarItemListener.onItemClick(getAdapterPosition(), (String) day.getText());
            });
        }
    }
}
