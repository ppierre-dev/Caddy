package com.iut.caddy.ui.newList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iut.caddy.MainActivity;
import com.iut.caddy.R;
import com.iut.caddy.database.DbAdapter;
import com.iut.caddy.databinding.FragmentNewListBinding;

public class NewListFragment extends Fragment {

    private FragmentNewListBinding binding;
    private DbAdapter dbAdapter;

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
    public void onStart() {
        super.onStart();
        this.dbAdapter = ((MainActivity)getActivity()).getDbAdapter();
        EditText inputListName = this.binding.getRoot().findViewById(R.id.inputNewListName);
        Button addNewListButton = this.binding.getRoot().findViewById(R.id.addNewListButton);
        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewList(inputListName.getText().toString());
                inputListName.setText("");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createNewList(String name) {
        this.dbAdapter.newList(name);
    }
}