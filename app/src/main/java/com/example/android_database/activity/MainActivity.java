package com.example.android_database.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_database.R;
import com.example.android_database.database.PeopleTable;
import com.example.android_database.provider.DatabaseOpenHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseOpenHelper databaseOpenHelper;
    private PeopleTable peopleTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseOpenHelper = DatabaseOpenHelper.getInstance(this);

        peopleTable = new PeopleTable(databaseOpenHelper);

        peopleTable.insert(1, "Bob", "Smith");
        peopleTable.insert(2, "Ralph", "Taylor");
        peopleTable.insert(3, "Sabrina", "Anderson");
        peopleTable.insert(4, "Elizabeth", "Hoffman");
        peopleTable.insert(5, "Abigail", "Elder");

        //peopleTable.update();
        peopleTable.replace();

        peopleTable.delete(1);
    }
}