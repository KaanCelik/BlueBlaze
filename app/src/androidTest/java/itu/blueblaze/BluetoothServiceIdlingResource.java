package itu.blueblaze;

import android.support.test.espresso.IdlingResource;

import itu.blueblaze.bluetooth.BluetoothFragment;
import itu.blueblaze.bluetooth.BluetoothService;

/**
 * Created by KaaN on 18-12-2016.
 */

public class BluetoothServiceIdlingResource implements IdlingResource {


    private final BluetoothFragment.Callback mActivity;

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    public BluetoothServiceIdlingResource(BluetoothFragment.Callback activity) {



        this.mActivity = activity;
    }

    @Override
    public String getName() {
        return BluetoothServiceIdlingResource.class.getName();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {

        this.resourceCallback = callback;
    }

    @Override
    public boolean isIdleNow() {

        boolean idle = !isServiceRunning();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;




    }

    private boolean isServiceRunning(){
        int state = mActivity.getBluetoothService().getState();
        if (BluetoothService.STATE_CONNECTING == state) {
            return true;
        }
        return false;
    }


}
