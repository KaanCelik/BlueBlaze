package itu.blueblaze.bluetooth;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

/**
 * Created by KaaN on 13-12-2016.
 */

public interface Responder {
    void onStateChange(Message msg);

    void onMessageWrite(Message msg);
    void onMessageRead(Message msg);

    void onMessageDeviceName(Message msg, FragmentActivity activity);
    void onMessageToast(Message msg, FragmentActivity activity);


}
