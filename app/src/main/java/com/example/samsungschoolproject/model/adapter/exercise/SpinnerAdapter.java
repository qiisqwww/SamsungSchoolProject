package com.example.samsungschoolproject.model.adapter.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.samsungschoolproject.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int mResource; // id вьюхи для свернутого списка
    private final int dropDownResource; // id вьюхи для выпадающего списка
    private final List<String> items;

    public SpinnerAdapter(@NonNull Context context, int mResource, int dropDownResource, List<String> items) {
        super(context, mResource, items);
        this.context = context;
        this.mResource = mResource;
        this.dropDownResource = dropDownResource;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(mResource, parent, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(items.get(position));
        //Здесь делаешь с tvTitle то что тебе нужно. Меняешь фон, цвет текста и т.д.
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(dropDownResource, parent, false);
        TextView tvTitle = view.findViewById(R.id.tvDropDown);
        tvTitle.setText(items.get(position));
        return view;
    }
}