package itu.blueblaze;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by KaaN on 5-12-2016.
 */

public abstract class ParameterDialogFragment extends DialogFragment {

    public static final String ARG_UUID = "itu.blueblaze.uuid";
    private static final String TAG = "ParameterDialogFragment";

    private EditText mParameterNameField;
    private EditText mParameterValueField;
    private ParamEntry mParamEntry;

    protected abstract void onPositiveAction();

    protected abstract void updateUI();

    protected abstract int getTitleStringResId();

    public EditText getParameterValueField() {
        return mParameterValueField;
    }

    public EditText getParameterNameField() {
        return mParameterNameField;
    }

    public ParamEntry getParamEntry() {
        return mParamEntry;
    }

    public void setParamEntry(ParamEntry paramEntry) {
        mParamEntry = paramEntry;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_parameter, null);

        mParameterNameField = (EditText) view.findViewById(R.id.dialog_parameter_name);
        mParameterValueField = (EditText) view.findViewById(R.id.dialog_parameter_value);

        updateUI();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(getTitleStringResId());
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (validateInput()) {
                    onPositiveAction();
                } else {
                    showInvalidInputWarning();
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

    private void showInvalidInputWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(R.string.alert_invalid_input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        builder.create().show();
    }

    private boolean validateInput() {

        boolean isValid;
        try {
            boolean isNameValid = mParameterNameField.getText().toString().length() > 0;

            boolean isValueValid = Integer.parseInt(mParameterValueField.getText().toString()) >= 0 &
                    Integer.parseInt(mParameterValueField.getText().toString()) < 256;

            isValid = isNameValid & isValueValid;

        } catch (NumberFormatException nfe) {
            Log.d(TAG, nfe.getMessage());
            isValid = false;
        }

        return isValid;

    }

    public interface Callback {
        void onParamAdded();

        void onParamEdited();
    }
}
