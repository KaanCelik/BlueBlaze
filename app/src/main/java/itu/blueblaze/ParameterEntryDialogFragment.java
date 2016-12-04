package itu.blueblaze;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by KaaN on 3-12-2016.
 * <p>
 * This fragment displays a dialog and receives user input for new parameters.
 */

public class ParameterEntryDialogFragment extends DialogFragment {

    public static final String ARG_NAME = "itu.blueblaze.name";
    public static final String ARG_VALUE = "itu.blueblaze.value";
    private EditText mParameterNameField;
    private EditText mParameterValueField;

    public static ParameterEntryDialogFragment newInstance(ParamEntry paramEntry){
        ParameterEntryDialogFragment dialogFragment = new ParameterEntryDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME,paramEntry.getName());
        args.putString(ARG_VALUE,paramEntry.getValueText());
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_parameter, null);

        mParameterNameField = (EditText) view.findViewById(R.id.dialog_parameter_name);
        mParameterValueField = (EditText) view.findViewById(R.id.dialog_parameter_value);
        if(getArguments() != null) {
            mParameterNameField.setText(getArguments().getString(ARG_NAME));
            mParameterValueField.setText(getArguments().getString(ARG_VALUE));
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(R.string.dialog_parameter_title);
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (validateInput()) {
                    if(getArguments() != null) {
                        //TODO Update paramEnty here
                    }else {
                        processUserAction();
                    }
                }
                else {
                    //TODO Prompt user for a valid input
                }
            }
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return dialogBuilder.create();
    }

    private void processUserAction() {
        ParamEntry paramEntry = new ParamEntry();
        paramEntry.setName(mParameterNameField.getText().toString());
        paramEntry.setValue(Integer.parseInt(mParameterValueField.getText().toString()));
        //TODO Rework after sqlite implementation
        ParamKeeper.get().getParamEntries().add(paramEntry);
    }

    private boolean validateInput() {
        //TODO Rework this implementation during test phase
        return mParameterNameField.getText().toString().length() > 0 &
                Integer.parseInt(mParameterValueField.getText().toString()) >= 0 &
                Integer.parseInt(mParameterValueField.getText().toString()) < 256;

    }
}
