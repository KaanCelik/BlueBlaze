package itu.blueblaze;

import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

/**
 * Created by KaaN on 5-12-2016.
 */

public class ParameterEditDialogFragment extends ParameterDialogFragment {

    private static final String TAG = "ParameEditDialog";

    public static ParameterEditDialogFragment newInstance(ParamEntry paramEntry) {
        ParameterEditDialogFragment dialogFragment = new ParameterEditDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID, paramEntry.getId().toString());
        dialogFragment.setArguments(args);
        return dialogFragment;
    }






    @Override
    protected void onPositiveAction() {
        ParamEntry paramEntry = getParamEntry();
        paramEntry.setName(getParameterNameField().getText().toString());
        paramEntry.setValue(Integer.parseInt(getParameterValueField().getText().toString()));
        ParamKeeper.get(getContext()).update(paramEntry);
        Callback callbackActivity = (Callback)getActivity();
        callbackActivity.onParamEdited();

    }

    @Override
    protected int getTitleStringResId() {
        return R.string.dialog_edit_parameter_title;
    }

    @Override
    protected void updateUI() {
        UUID arg_id = UUID.fromString(getArguments().getSerializable(ARG_UUID).toString());
        try {
            ParamEntry paramEntry =ParamKeeper.get(getContext()).getParamEntry(arg_id);
            setParamEntry(paramEntry);
            getParameterNameField().setText(paramEntry.getName());
            getParameterValueField().setText(paramEntry.getValueText());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

    }
}
