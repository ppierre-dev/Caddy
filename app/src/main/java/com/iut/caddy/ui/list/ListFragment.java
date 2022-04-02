package com.iut.caddy.ui.list;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.iut.caddy.MainActivity;
import com.iut.caddy.R;
import com.iut.caddy.database.DbAdapter;
import com.iut.caddy.databinding.FragmentListBinding;
import com.iut.caddy.ui.viewProductsList.ViewProductsList;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private static ListFragment instance;
    private ListView lists;
    private DbAdapter dbAdapter;
    private int listIdToShow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        listViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void onStart() {
        super.onStart();
        lists = (ListView) this.binding.getRoot().findViewById(R.id.listView);

        this.dbAdapter = ((MainActivity)getActivity()).getDbAdapter();
        loadLists();

        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long _id = id;
                listIdToShow = (int) _id;
                Navigation.findNavController(view).navigate(R.id.navigation_view_products_list);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadLists() {
        // Get all of the notes from the database and create the item list
        Cursor c = dbAdapter.fetchAllLists();
        ((MainActivity)getActivity()).startManagingCursor(c);

        String[] from = new String[] { dbAdapter.KEY_NAME};
        int[] to = new int[] { R.id.listsText};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter listRes =
                new SimpleCursorAdapter(((MainActivity)getActivity()), R.layout.lists_row, c, from, to,0);
        lists.setAdapter(listRes);
    }

    public int getListIdToShow() {
        return this.listIdToShow;
    }

    public static ListFragment getInstance() {
        return instance;
    }
}