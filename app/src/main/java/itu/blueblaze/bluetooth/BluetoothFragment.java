package itu.blueblaze.bluetooth;

import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import itu.blueblaze.R;

/**
 * This fragment utilizes basic Bluetooth capabilities.
 */

public abstract class BluetoothFragment extends Fragment {

    protected static final int REQUEST_ENABLE_BT = 3;
    private static final String TAG = "BluetoothFragment";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    /**
     * Local Bluetooth adapter
     */
    protected BluetoothAdapter mBluetoothAdapter = null;
    /**
     * Member object for the chat services
     */
    protected BluetoothService mBtService = null;
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;
    /**
     * The Handler that gets information back from the BluetoothService
     */
    private final Handler mHandler = new Handler() {
    //TODO Research what this warning means :)

        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                //This case Updates subtitle at ActionBar
                case Constants.MESSAGE_STATE_CHANGE:
                    onStateChange(msg);
                    break;

                case Constants.MESSAGE_WRITE:
                    onMessageWrite(msg);
                    break;
                case Constants.MESSAGE_READ:
                    onMessageRead(msg);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    onMessageDeviceName(msg, activity);

                    break;
                case Constants.MESSAGE_TOAST:
                    onMessageToast(msg, activity);
                    break;
            }
        }

        private void onStateChange(Message msg) {
            switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
            }
        }

        private void onMessageToast(Message msg, FragmentActivity activity) {
            if (null != activity) {
                Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                        Toast.LENGTH_SHORT).show();
            }
        }

        private void onMessageDeviceName(Message msg, FragmentActivity activity) {
            // save the connected device's name
            mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
            if (null != activity) {
                Toast.makeText(activity, "Connected to "
                        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
            }
        }

        private void onMessageRead(Message msg) {
            byte[] readBuf = (byte[]) msg.obj;
            // construct a string from the valid bytes in the buffer
            String readMessage = new String(readBuf, 0, msg.arg1);
            //TODO handle message read maybe with an abstract method
            //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
        }

        private void onMessageWrite(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            // construct a string from the buffer
            String writeMessage = new String(writeBuf);
            //TODO handle message write maybe with an abstract method
            // mConversationArrayAdapter.add("Me:  " + writeMessage);
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Local bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupBtSession() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBtService == null) {
            //TODO handle here
            setupBtSession();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBtService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBtService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBtService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBtService != null) {
            mBtService.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }

        }
        return false;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupBtSession();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Makes this device discoverable.
     */
    protected void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    protected void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBtService.connect(device, secure);

    }

    /**
     * Set up the UI and background operations for chat.
     */
    protected void setupBtSession() {
        Log.d(TAG, "setupBtSession()");

        // Initialize the BluetoothService to perform bluetooth connections
        mBtService = new BluetoothService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        //TODO handle here
        //mOutStringBuffer = new StringBuffer("");
    }
}
