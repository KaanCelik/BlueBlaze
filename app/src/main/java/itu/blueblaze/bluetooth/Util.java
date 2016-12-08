package itu.blueblaze.bluetooth;

/**
 * Created by KaaN on 21.05.2016.
 */
public class Util {

    public String byteArrayToHexString(byte[] bytes){

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format(" 0x%02X", b));
        }
        return sb.toString().trim();
    }

}
