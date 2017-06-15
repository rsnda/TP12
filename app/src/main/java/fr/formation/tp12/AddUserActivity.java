package fr.formation.tp12;

import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.formation.tp12.database.datasource.DataSource;
import fr.formation.tp12.database.modele.User;

public class AddUserActivity extends AppCompatActivity {

    DataSource<User> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Create or retrieve the database
        try {
            dataSource = new DataSource<>(this, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void button_add(View view){
        EditText name = (EditText) findViewById(R.id.editText);
        String n = name.getText().toString();

        // open the database
        openDB();

        // Insert a new record
        // -------------------
        User user = new User();
        user.setNom(n);
        try {
            insertRecord(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        openDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDB();
    }

    public void openDB() throws SQLiteException {
        dataSource.open();
    }

    public void closeDB() {
        dataSource.close();
    }

    private long insertRecord(User user) throws Exception {

        // Insert the line in the database
        long rowId = dataSource.insert(user);

        // Test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when creating an User",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User created and stored in database",
                    Toast.LENGTH_LONG).show();
        }
        return rowId;
    }

}
