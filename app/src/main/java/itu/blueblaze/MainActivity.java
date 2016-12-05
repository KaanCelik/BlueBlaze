package itu.blueblaze;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity implements ParameterDialogFragment.Callback{

    @Override
    public Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public void onParamAdded() {
        ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onParamEdited() {
        ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
