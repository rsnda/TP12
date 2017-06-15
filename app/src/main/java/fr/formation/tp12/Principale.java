package fr.formation.tp12;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.formation.tp12.database.datasource.DataSource;
import fr.formation.tp12.database.modele.User;

public class Principale extends AppCompatActivity {

    DataSource<User> dataSource;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<String> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);

        // Instanciation du recyclerview :
        recyclerView = (RecyclerView) findViewById(R.id.myListSimple);

        adapter = new RecyclerViewAdapter(items, android.R.layout.simple_list_item_1);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (dataSource == null) {
                dataSource = new DataSource<>(this, User.class);
                dataSource.open();
            }
        } catch (Exception e) {
            // Traiter le cas !
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            dataSource.close();
        } catch (Exception e) {
            // Traiter le cas !
            e.printStackTrace();
        }
    }

    private long insertRecord(User user) throws Exception {

        // Insert the line in the database
        long rowId = dataSource.insert(user);

        // Test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when creating an User", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User created and stored in database", Toast.LENGTH_LONG).show();
        }
        return rowId;
    }

    /**
     * * Update a record
     *
     * @return the updated row id
     */
    private long updateRecord(User user) throws Exception {

        int rowId = dataSource.update(user);

        // test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when updating an User", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User updated in database", Toast.LENGTH_LONG).show();
        }
        return rowId;
    }

    private void deleteRecord(User user) {
        long rowId = dataSource.delete(user);
        if (rowId == -1) {
            Toast.makeText(this, "Error when deleting an User", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User deleted in database", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Query a the database
     */
    private void queryTheDatabase() {
        List<User> users = dataSource.readAll();
        displayResults(users);
    }

    private void displayResults(List<User> users) {

        int count = 0;
        for (User user : users
                ) {
            Toast.makeText(this,
                    "Utilisateur :" + user.getNom() + "(" + user.getId() + ")", Toast.LENGTH_LONG).show();
            count++;
        }
        Toast.makeText(this,
                "The number of elements retrieved is " + count, Toast.LENGTH_LONG).show();

    }

    public void fab_click(View view){
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String flux = data.getStringExtra("NEWUSER"); // Tester si pas null ;-)
        User utilisateur = new Gson().fromJson(flux, User.class);

        try {
            dataSource.insert(utilisateur);
        } catch (Exception e) {
            // Que faire :-(
            e.printStackTrace();
        }

        // Indiquer un changement au RecycleView
        queryTheDatabase();
        adapter.notifyDataSetChanged();
    }
}
