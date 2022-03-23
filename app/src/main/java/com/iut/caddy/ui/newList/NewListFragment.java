package com.iut.caddy.ui.newList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iut.caddy.databinding.FragmentNewListBinding;

public class NewListFragment extends Fragment {

    private FragmentNewListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewListViewModel newListViewModel =
                new ViewModelProvider(this).get(NewListViewModel.class);

        binding = FragmentNewListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

/*
        final TextView textView = binding.textDashboard;
*/
/*
        newListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}