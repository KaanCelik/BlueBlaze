package itu.blueblaze;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import itu.blueblaze.bluetooth.BluetoothFragment;
import itu.blueblaze.logger.Log;

/**
 * Created by KaaN on 12-12-2016.
 * Console UI for bluetooth interface
 */

public class ConsoleFragment extends BluetoothFragment {

    public static final String TAG = "ConsoleFragment";

    //Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if ((actionId == EditorInfo.IME_ACTION_NONE && event.getAction() == KeyEvent.ACTION_UP)
                    || actionId == EditorInfo.IME_ACTION_SEND ) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupConsole();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupConsole();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_console, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
        setupConsole();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_console, menu);
        android.util.Log.d(TAG, "onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_show_list:

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag(ItemListFragment.TAG);
                if (fragment == null) {
                    fragment = new ItemListFragment();
                    fragmentTransaction.detach(this);
                    fragmentTransaction.add(R.id.fragment_container, fragment, ItemListFragment.TAG);
                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                } else {
                    fragmentTransaction.detach(this);
                    fragmentTransaction.attach(fragment).addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupConsole() {
        Log.d(TAG, "setupConsole()");

        // Initialize the array adapter for the conversation thread
        if(mConversationArrayAdapter == null){
            mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        }
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });


        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }


    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        if (message.length() > 0) {
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    @Override
    public void onMessageRead(Message msg) {
        byte[] readBuf = (byte[]) msg.obj;
        // construct a string from the valid bytes in the buffer
        String readMessage = new String(readBuf, 0, msg.arg1);
        mConversationArrayAdapter.add(getConnectedDeviceName() + ":  " + readMessage);
    }

    @Override
    public void onMessageWrite(Message msg) {
        byte[] writeBuf = (byte[]) msg.obj;
        // construct a string from the buffer
        String writeMessage = new String(writeBuf);
        mConversationArrayAdapter.add("Me:  " + writeMessage);
    }


}


