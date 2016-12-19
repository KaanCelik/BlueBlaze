package itu.blueblaze;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import itu.blueblaze.bluetooth.BluetoothFragment;
import itu.blueblaze.bluetooth.BluetoothService;
import itu.blueblaze.bluetooth.Constants;

public class MainActivity extends SingleFragmentActivity implements ParameterDialogFragment.Callback, BluetoothFragment.Callback {


    private static BluetoothService mBluetoothService;
    /**
     * The Handler that gets information back from the BluetoothService
     */
    private  final  Handler mHandler = new Handler(new BluetoothHandler());

    private class BluetoothHandler implements Handler.Callback{
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //This case Updates subtitle at ActionBar
                case Constants.MESSAGE_STATE_CHANGE:
                    onStateChange(msg);
                    return true;
                case Constants.MESSAGE_WRITE:
                    onMessageWrite(msg);
                    return true;
                case Constants.MESSAGE_READ:
                    onMessageRead(msg);
                    return true;
                case Constants.MESSAGE_DEVICE_NAME:
                    onMessageDeviceName(msg);
                    return true;
                case Constants.MESSAGE_TOAST:
                    onMessageToast(msg);
                    return true;
            }
            return false;

        }

    }

    private String mConnectedDeviceName;

    @Override
    public BluetoothService getBluetoothService() {
        return mBluetoothService;
    }

    @Override
    public String getConnectedDeviceName() {
        return mConnectedDeviceName;
    }

    @Override
    public void setConnectedDeviceName(String connectedDeviceName) {
        mConnectedDeviceName = connectedDeviceName;
    }

    @Override
    public Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public String getLauncherFragmentTag() {
        return ItemListFragment.TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void onStateChange(Message msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        btFragment.onStateChange(msg);
    }

    private void onMessageWrite(Message msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        btFragment.onMessageWrite(msg);
    }

    private void onMessageRead(Message msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        btFragment.onMessageRead(msg);
    }

    private void onMessageDeviceName(Message msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        btFragment.onMessageDeviceName(msg, this);
    }

    private void onMessageToast(Message msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        btFragment.onMessageToast(msg, this);
    }

    @Override
    public void setupBluetooth() {
        // Initialize the BluetoothService to perform bluetooth connections
        if (mBluetoothService == null) {
            mBluetoothService = new BluetoothService(this, mHandler);
        }


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
