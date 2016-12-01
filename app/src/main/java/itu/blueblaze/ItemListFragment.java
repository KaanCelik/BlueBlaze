package itu.blueblaze;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ItemListFragment extends Fragment {

    private static final String TAG = "ItemListFragment";

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //set adapter for the mRecyclerView here
        mRecyclerView.setAdapter(new ItemAdapter(ParamKeeper.get().getParamEntries()));
    return view;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mItemLabelTextView;
        private TextView mValueTextView;
        private ParamEntry mParamEntry;


        private ItemHolder(View view){
            super(view);
            mItemLabelTextView = (TextView) view.findViewById(R.id.param_name_label);
            mValueTextView = (TextView) view.findViewById(R.id.param_value);
        }

        private void bind(ParamEntry paramEntry){
            mParamEntry = paramEntry;
            mItemLabelTextView.setText(mParamEntry.getName());
            mValueTextView.setText( mParamEntry.getValueText());

        }

        @Override
        public void onClick(View view) {
            Log.e(TAG,"implement onClickListener");
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder>{
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
            View view = layoutInflater.inflate(R.layout.parameter_list_item,parent,false);
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
