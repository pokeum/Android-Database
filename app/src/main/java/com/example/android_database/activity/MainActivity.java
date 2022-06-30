package com.example.android_database.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_database.database.PeopleTable;
import com.example.android_database.dialog.DialogEditPeopleDB;
import com.example.android_database.model.People;
import com.example.android_database.provider.DatabaseOpenHelper;
import com.example.android_database.databinding.ActivityMainBinding;
import com.example.android_database.databinding.DatabasePeopleRowBinding;
import com.example.android_database.throwable.PeopleException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private DatabaseOpenHelper databaseOpenHelper;
    private PeopleTable peopleTable;

    private int nextID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        addPeopleRowDescription();

        databaseOpenHelper = DatabaseOpenHelper.getInstance(this);
        peopleTable = new PeopleTable(databaseOpenHelper);

        for (People people: peopleTable.queryAll()) {
            addPeopleRow(people);
            nextID = people.getId() + 1;
        }

        mainBinding.btnInsert.setOnClickListener(view -> {
            try {
                peopleTable.insert(new People(nextID,
                                mainBinding.edtFirstName.getText().toString(),
                                mainBinding.edtLastName.getText().toString()));
                nextID ++;
                redrawUI();
                shortToast("데이터 저장");
            } catch (PeopleException e) {
                shortToast("데이터를 입력해주세요.");
            } finally { initPeopleInsertField(); }
        });
    }

    private void addPeopleRowDescription() {
        DatabasePeopleRowBinding rowBinding = DatabasePeopleRowBinding.inflate(getLayoutInflater());
        rowBinding.linearLayout.setClickable(false);
        mainBinding.scrollBox.addView(rowBinding.getRoot());
    }

    private void addPeopleRow(People people) {

        if (people == null) return;

        DatabasePeopleRowBinding rowBinding = DatabasePeopleRowBinding.inflate(getLayoutInflater());
        rowBinding.txtId.setText(Integer.toString(people.getId()));
        rowBinding.txtFirstName.setText(people.getFirstName());
        rowBinding.txtLastName.setText(people.getLastName());

        rowBinding.linearLayout.setOnClickListener(view -> {
            DialogEditPeopleDB dialog = new DialogEditPeopleDB();
            dialog.init(this, people);
            dialog.show(getSupportFragmentManager(), "EDIT_PEOPLE_DATABASE");
        });

        mainBinding.scrollBox.addView(rowBinding.getRoot());
    }

    public void deletePeopleRow(int id) {
        peopleTable.delete(id);
        redrawUI();
    }

    public void editPeopleRow(People people) {
        peopleTable.update(people);
        //peopleTable.replace(people);
        redrawUI();
    }

    private void initPeopleInsertField() {
        mainBinding.edtFirstName.setText("");
        mainBinding.edtLastName.setText("");
    }

    private void redrawUI() {
        mainBinding.scrollBox.removeAllViews();
        addPeopleRowDescription();
        for (People people: peopleTable.queryAll()) { addPeopleRow(people); }
    }

    private void shortToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }
}