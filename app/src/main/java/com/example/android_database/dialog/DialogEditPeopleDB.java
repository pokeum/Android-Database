package com.example.android_database.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android_database.activity.MainActivity;
import com.example.android_database.databinding.DialogEditPeopleDatabaseBinding;
import com.example.android_database.model.People;
import com.example.android_database.throwable.PeopleException;
import com.google.android.material.snackbar.Snackbar;

public class DialogEditPeopleDB extends DialogFragment {

    private People mPeople;
    private MainActivity mMainActivity;

    private DialogEditPeopleDatabaseBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DialogEditPeopleDatabaseBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        setDefaultView();

        binding.btnEdit.setOnClickListener(view -> {
            int id = Integer.parseInt(binding.txtId.getText().toString());
            String firstName = binding.edtFirstName.getText().toString();
            String lastName = binding.edtLastName.getText().toString();
            if (firstName.equals(mPeople.getFirstName()) && lastName.equals(mPeople.getLastName())) {
                endDialogEditPeopleDB();
            } else {    // Change occurred
                try {
                    mMainActivity.editPeopleRow(new People(id, firstName, lastName));
                    endDialogEditPeopleDB();
                } catch (PeopleException e) {
                    setDefaultView();
                    snackBar("데이터를 입력해주세요.");
                }
            }
        });

        binding.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Delete \"" + mPeople.getFullName() + "\"?")
                    .setPositiveButton("yes", (dialogInterface, i) -> {
                            mMainActivity.deletePeopleRow(mPeople.getId());
                            endDialogEditPeopleDB();
                    })
                    .setNegativeButton("no", null)
                    .show();
        });

        return builder.create();
    }

    private void setDefaultView() {
        binding.txtId.setText(Integer.toString(mPeople.getId()));
        binding.edtFirstName.setText(mPeople.getFirstName());
        binding.edtLastName.setText(mPeople.getLastName());
    }

    private void endDialogEditPeopleDB() { dismiss(); }

    private void snackBar(String msg) { Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show(); }

    // Receive data from the MainActivity
    public void init(MainActivity activity, People people) {
        mMainActivity = activity;
        mPeople = people;
    }
}
