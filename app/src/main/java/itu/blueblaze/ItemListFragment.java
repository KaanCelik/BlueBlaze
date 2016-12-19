package itu.blueblaze;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import itu.blueblaze.bluetooth.BluetoothFragment;
import itu.blueblaze.bluetooth.BluetoothService;
import itu.blueblaze.bluetooth.DeviceListActivity;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ItemListFragment extends BluetoothFragment {

    public static final String TAG = "ItemListFragment";
    private static final String PARAMETER_ENTRY_DIALOG ="ParameterEntryDialog";
    private static final int REQUEST_PARAMETER_ENTRY = 0;



    /**
     * Member RecyclerView object
     * Displays ParamEntry objects stored at db
     */
    private RecyclerView mRecyclerView;
    /**
     * Adapter for RecyclerView
     * Binds ParamEntry objects to views
     */
    private ItemAdapter mAdapter;


    @Override
    public void onMessageRead(Message msg) {
        Log.i(TAG,"Message Received.");
    }

    @Override
    public void onMessageWrite(Message msg) {
        Log.i(TAG,"Message Written.");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //set adapter for the mRecyclerView here
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
        //mRecyclerView.setAdapter(null);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_list, menu);
        Log.i(TAG,"onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_new_word:
                    ParamEntry paramEntry = new ParamEntry(UUID.randomUUID());
                    ParameterEntryDialogFragment dialogFragment = ParameterEntryDialogFragment.newInstance(paramEntry);
                    startEntryDialog(dialogFragment);
                    return true;
            case R.id.menu_item_sync:
                    sendAll();
                    break;

            case R.id.menu_item_show_console: {
                Callback callback = (Callback) getActivity();
                //callback.onShowConsole();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStackImmediate(this.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment fragment = fragmentManager.findFragmentByTag(ConsoleFragment.TAG);
                if(fragment == null){
                    fragmentTransaction.detach(this);
                    fragment = new ConsoleFragment();
                    fragmentTransaction.add(R.id.fragment_container,fragment,ConsoleFragment.TAG);
                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                }else{
                    fragmentTransaction.detach(this);
                    fragmentTransaction.attach(fragment).addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                }
                return true;

            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sends all the parameters currently saved on database.
     * Messages are sent one value at a time.
     */
    private void sendAll() {
        List<ParamEntry> params = ParamKeeper.get(getContext()).getParameterList();
        for (ParamEntry element : params){
            sendMessage(element.getValueText());
        }
    }

    @Override
    public void updateUI(){
        super.updateUI();
        if(mAdapter == null){
            mAdapter = new ItemAdapter(ParamKeeper.get(getContext()).getParameterList());
            mRecyclerView.setAdapter(mAdapter);
        } else  {
            mAdapter.setParamEntryList(ParamKeeper.get(getContext()).getParameterList());
            mAdapter.notifyDataSetChanged();
        }

    }

    private void startEntryDialog(ParameterDialogFragment dialogFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        dialogFragment.setTargetFragment(ItemListFragment.this,REQUEST_PARAMETER_ENTRY);
        dialogFragment.show(fragmentManager, PARAMETER_ENTRY_DIALOG);
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mItemLabelTextView;
        private TextView mValueTextView;
        private ParamEntry mParamEntry;



        private ItemHolder(View view) {
            super(view);
            mItemLabelTextView = (TextView) view.findViewById(R.id.param_name_label);
            mValueTextView = (TextView) view.findViewById(R.id.param_value);
            ImageButton editButton = (ImageButton) view.findViewById(R.id.button_list_item_edit);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParameterEditDialogFragment dialogFragment =  ParameterEditDialogFragment.newInstance(mParamEntry);
                    startEntryDialog(dialogFragment);
                }
            });

            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.button_list_item_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParamKeeper.get(getContext()).delete(mParamEntry);
                    updateUI();
                }
            });
        }

        private void bind(ParamEntry paramEntry) {
            mParamEntry = paramEntry;
            mItemLabelTextView.setText(mParamEntry.getName());
            mValueTextView.setText(mParamEntry.getValueText());

        }

        @Override
        public void onClick(View view) {
            Log.e(TAG, "implement onClickListener");
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<ParamEntry> mParamEntryList;

        public ItemAdapter(List<ParamEntry> paramEntryList) {
            mParamEntryList = paramEntryList;
        }

        public List<ParamEntry> getParamEntryList() {
            return mParamEntryList;
        }

        public void setParamEntryList(List<ParamEntry> paramEntryList) {
            mParamEntryList = paramEntryList;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.parameter_list_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            ParamEntry paramEntry = mParamEntryList.get(position);
            holder.bind(paramEntry);

        }

        @Override
        public int getItemCount() {
            return mParamEntryList.size();
        }
    }
}
