package itu.blueblaze;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by KaaN on 1-12-2016.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private Fragment mFragment;

    public abstract Fragment createFragment();
    public abstract String getLauncherFragmentTag();

    protected int getLayoutResourceId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentManager.findFragmentById(R.id.fragment_container)== null){
            mFragment = createFragment();
            fragmentTransaction.add(R.id.fragment_container,mFragment,getLauncherFragmentTag());
        }
        fragmentTransaction.commit();
    }
}
