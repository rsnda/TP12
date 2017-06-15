package fr.formation.tp12;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import fr.formation.tp12.database.datasource.DataSource;
import fr.formation.tp12.database.modele.User;

public class AddUserActivity extends AppCompatActivity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        name = (EditText) findViewById(R.id.editText);

    }

    public void button_add(View view){
        User user = new User();
        user.setNom(name.getText().toString());

        // Transformation en JSON :
        String flux = (new Gson()).toJson(user);
        Log.d("Utilisateur en JSON", flux);

        Intent intent = new Intent();
        intent.putExtra("NEWUSER", flux);
        setResult(2, intent);

        // Bye l'activité
        finish();

    }



}
