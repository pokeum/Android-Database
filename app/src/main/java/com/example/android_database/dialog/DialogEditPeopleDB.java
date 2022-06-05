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

public class DialogEditPeopleDB extends DialogFragment {

    private People mPeople;
    private MainActivity mMainActivity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        DialogEditPeopleDatabaseBinding binding = DialogEditPeopleDatabaseBinding.inflate(inflater);
        builder.setView(binding.getRoot());

        binding.txtId.setText(Integer.toString(mPeople.getId()));
        binding.edtFirstName.setText(mPeople.getFirstName());
        binding.edtLastName.setText(mPeople.getLastName());

        binding.btnEdit.setOnClickListener(view -> {
            endDialogEditPeopleDB();
        });

        binding.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mMainActivity);
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

    private void endDialogEditPeopleDB() { dismiss(); }

    // Receive data from the MainActivity
    public void init(MainActivity activity, People people) {
        mMainActivity = activity;
        mPeople = people;
    }
}
