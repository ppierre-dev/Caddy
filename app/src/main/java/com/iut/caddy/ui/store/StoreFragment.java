package com.iut.caddy.ui.store;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.iut.caddy.MainActivity;
import com.iut.caddy.R;
import com.iut.caddy.database.DbAdapter;
import com.iut.caddy.databinding.FragmentStoreBinding;

public class StoreFragment extends Fragment {

    private FragmentStoreBinding binding;
    private ListView productsList;
    private DbAdapter dbAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StoreViewModel newListViewModel =
                new ViewModelProvider(this).get(StoreViewModel.class);

        binding = FragmentStoreBinding.inflate(inflater, container, false);
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

        productsList = (ListView) this.binding.getRoot().findViewById(R.id.store_productsListView);
        registerForContextMenu(productsList);

        this.dbAdapter = ((MainActivity)getActivity()).getDbAdapter();
        loadAllProducts();

        productsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                long _id = id ;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_title);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbAdapter.deletePreDefinedProducts(id);
                        loadAllProducts();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long _id = id;
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choisir la liste");
        Cursor c = dbAdapter.fetchAllLists();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                menu.add(0, v.getId(), 0, c.getString(c.getColumnIndex("name")));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) productsList.getItemAtPosition(info.position);
        final String itemSelected = cursor.getString(0);
        Cursor c = dbAdapter.getListIdFromName(item.toString());
        c.moveToNext();
        dbAdapter.addItemToList(cursor.getInt(0), c.getInt(c.getColumnIndex("_id")));

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadAllProducts() {
        // Get all of the notes from the database and create the item list
        Cursor c = dbAdapter.fetchAllProducts();
        ((MainActivity)getActivity()).startManagingCursor(c);

        String[] from = new String[] { dbAdapter.KEY_LABEL};
        int[] to = new int[] { R.id.listsText};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter listRes =
                new SimpleCursorAdapter(((MainActivity)getActivity()), R.layout.lists_row, c, from, to,0);
        productsList.setAdapter(listRes);
    }
}