package itu.blueblaze;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.List;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ItemListFragment extends Fragment {

    private static final String TAG = "ItemListFragment";
    private static final String PARAMETER_ENTRY_DIALOG ="ParameterEntryDialog";
    private static final int REQUEST_PARAMETER_ENTRY = 0;
    private RecyclerView mRecyclerView;

    public interface Callback{

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
        mRecyclerView.setAdapter(new ItemAdapter(ParamKeeper.get().getParamEntries()));
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
        Log.i(TAG,"onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(R.id.menu_item_new_word == item.getItemId()){
            ParameterEntryDialogFragment dialogFragment = new ParameterEntryDialogFragment();
            startEntryDialog(dialogFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    private void startEntryDialog(ParameterEntryDialogFragment dialogFragment) {
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
                    ParameterEntryDialogFragment dialogFragment =  ParameterEntryDialogFragment.newInstance(mParamEntry);
                    startEntryDialog(dialogFragment);
                }
            });

            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.button_list_item_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO delete parameter here
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
