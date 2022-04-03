package com.iut.caddy.ui.viewProductsList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.MenuInflater;
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.view_products_list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item, int listId) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int listeId = listId;
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.DeleteItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbAdapter.clearProductsList(listeId);
                        loadProductsFromList(listeId);

                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true ;
            case R.id.DeleteStrikeText:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle(R.string.dialog_title);
                builder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbAdapter.clearProductsList(listeId);
                        loadProductsFromList(listeId);

                    }
                });
                builder1.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                return true ;
        }


        return true;

    }



    @Override
    public void onStart() {
        super.onStart();
        this.productsList = (ListView) this.binding.getRoot().findViewById(R.id.viewProductsList);

        this.dbAdapter = ((MainActivity)getActivity()).getDbAdapter();
        int idToShow = ListFragment.getInstance().getListIdToShow();

        loadProductsFromList(idToShow);

        this.productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = (TextView) view ;
                if (txt.getPaint().isStrikeThruText()){
                    txt.setPaintFlags(txt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    txt.setPaintFlags(txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }
        });

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