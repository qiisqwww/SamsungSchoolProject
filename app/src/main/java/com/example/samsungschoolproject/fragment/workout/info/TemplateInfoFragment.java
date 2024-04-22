package com.example.samsungschoolproject.fragment.workout.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.DTO.TemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.view_adapter.workout.info.TemplateInfoAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TemplateInfoFragment extends BottomSheetDialogFragment {
    public static String TAG;
    private TemplateInfoAdapter templateInfoAdapter;
    private TemplateInfo templateInfo;
    private RecyclerView templateInfoRecycler;
    private Button backButton;

    public TemplateInfoFragment(TemplateInfo templateInfo){
        this.templateInfo = templateInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        initButtonListeners();
        setTemplateInfoRecycler();
    }

    private void initWidgets(View view){
        backButton = view.findViewById(R.id.back);
        templateInfoRecycler = view.findViewById(R.id.templateInfoRecycler);
    }

    private void initButtonListeners(){
        backButton.setOnClickListener( v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        });
    }

    public void setTemplateInfoRecycler() {
        templateInfoAdapter = new TemplateInfoAdapter(templateInfo);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        templateInfoRecycler.setLayoutManager(layoutManager);
        templateInfoRecycler.setAdapter(templateInfoAdapter);
    }
}