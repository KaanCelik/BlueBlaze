package itu.blueblaze;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ParamEntry {
    private String mName;
    private int mValue;

    public ParamEntry(String name, int value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getValue() {
        return mValue;
    }
    public String getValueText(){
        return Integer.toString(mValue);
    }

    public void setValue(int value) {
        mValue = value;
    }
}
