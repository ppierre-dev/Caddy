package com.iut.caddy.ui.viewProductsList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.iut.caddy.MainActivity;
import com.iut.caddy.R;
import com.iut.caddy.database.DbAdapter;
import com.iut.caddy.databinding.ViewProductsListFragmentBinding;
import com.iut.caddy.ui.list.ListFragment;

import java.util.List;

public class ViewProductsList extends Fragment {

    private ViewProductsListFragmentBinding binding;
    private ListView productsList;
    private DbAdapter dbAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewProductsListViewModel newListViewModel =
                new ViewModelProvider(this).get(ViewProductsListViewModel.class);

        binding = ViewProductsListFragmentBinding.inflate(inflater, container, false);
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
        this.productsList = (ListView) this.binding.getRoot().findViewById(R.id.viewProductsList);

        this.dbAdapter = ((MainActivity)getActivity()).getDbAdapter();
        int idToShow = ListFragment.getInstance().getListIdToShow();

        loadProductsFromList(idToShow);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadProductsFromList(int listId) {
        // Get all of the notes from the database and create the item list
        Cursor c = dbAdapter.findProductsByListId(listId);
        ((MainActivity)getActivity()).startManagingCursor(c);

        String[] from = new String[] { dbAdapter.KEY_LABEL};
        int[] to = new int[] { R.id.listsText};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter listRes =
                new SimpleCursorAdapter(((MainActivity)getActivity()), R.layout.lists_row, c, from, to,0);
        productsList.setAdapter(listRes);
    }
}