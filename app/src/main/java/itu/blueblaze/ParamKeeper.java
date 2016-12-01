package itu.blueblaze;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ParamKeeper {

    private static ParamKeeper mParamKeeper;
    private List<ParamEntry> mParamEntries;

    private ParamKeeper(){
        super();
        mParamEntries = new ArrayList<>();

        for(int i = 0 ; i<20;i++){
            mParamEntries.add(new ParamEntry("Parameter "+i, i*10));
        }
    }

    public static ParamKeeper get(){
        if (mParamKeeper == null){
            mParamKeeper = new ParamKeeper();
        }
        return mParamKeeper;

    }

    public   List<ParamEntry> getParamEntries(){
        return mParamEntries;
    }

}
