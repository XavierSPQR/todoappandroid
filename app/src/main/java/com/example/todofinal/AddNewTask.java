package com.example.todofinal;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todofinal.Model.ToDoModel;
import com.example.todofinal.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment
{
    public static final String TAG="AddNewTask";
    private EditText mEditText;
    private Button mSaveButton;
    private DatabaseHelper myDB;

    public static AddNewTask newInstance()
    {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);
        myDB = new DatabaseHelper(getActivity());
        boolean isUpdate = false;
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            isUpdate = true;
            String task=bundle.getString("task");
            mEditText.setText(task);
            if(task.length()>0)
            {
                mSaveButton.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.toString().equals(""))
                {
                    mSaveButton.setEnabled(false);
                   mSaveButton.setBackgroundColor(Color.GRAY);
                   mSaveButton.setFocusable(true);
                }
                else
                {
                    mSaveButton.setEnabled(true);
                   mSaveButton.setBackgroundColor(Color.GREEN);
                   mSaveButton.setFocusable(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if (finalIsUpdate)
                {
                    myDB.updateTask(bundle.getInt("id"),text);

                }
                else
                {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);

                }
                dismiss();

            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener)
        {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
