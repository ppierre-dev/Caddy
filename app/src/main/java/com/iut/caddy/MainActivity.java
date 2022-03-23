package com.iut.caddy;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.iut.caddy.database.DbAdapter;
import com.iut.caddy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DbAdapter dbAdapter;
    private ListView lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lists = (ListView) findViewById(R.id.listView);

        dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        loadLists();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list ,R.id.navigation_new_list, R.id.navigation_store)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    private void loadLists() {
        // Get all of the notes from the database and create the item list
        Cursor c = dbAdapter.fetchAllLists();
        startManagingCursor(c);

        String[] from = new String[] { dbAdapter.KEY_NAME};
        int[] to = new int[] { R.id.listsText};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter listRes =
                new SimpleCursorAdapter(this, R.layout.lists_row, c, from, to,0);
        lists.setAdapter(listRes);
    }
}