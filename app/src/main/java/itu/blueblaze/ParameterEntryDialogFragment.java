package itu.blueblaze;

import android.os.Bundle;

import java.util.UUID;

/**
 * Created by KaaN on 3-12-2016.
 * <p>
 * This fragment displays a dialog and receives user input for new parameters.
 */

public class ParameterEntryDialogFragment extends ParameterDialogFragment {


    public static ParameterEntryDialogFragment newInstance(ParamEntry paramEntry){
        ParameterEntryDialogFragment dialogFragment = new ParameterEntryDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UUID,paramEntry.getId().toString());
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    protected void onPositiveAction() {
        UUID arg_id = UUID.fromString(getArguments().getSerializable(ARG_UUID).toString());
        ParamEntry paramEntry = new ParamEntry(arg_id);
        paramEntry.setName(getParameterNameField().getText().toString());
        paramEntry.setValue(Integer.parseInt(getParameterValueField().getText().toString()));
        setParamEntry(paramEntry);
        ParamKeeper.get(getContext()).add(paramEntry);
        Callback callbackActivity = (Callback)getActivity();
        callbackActivity.onParamAdded();
    }

    @Override
    protected int getTitleStringResId() {
        return R.string.dialog_add_parameter_title;
    }

    @Override
    protected void updateUI() {

    }
}
