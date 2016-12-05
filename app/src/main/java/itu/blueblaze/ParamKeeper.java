package itu.blueblaze;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import itu.blueblaze.database.DbSchema.ParametersTable;
import itu.blueblaze.database.LocalDbHelper;
import itu.blueblaze.database.ParametersCursorWrapper;

/**
 * Created by KaaN on 1-12-2016.
 */

public class ParamKeeper {

    private static final String TAG = "blueblaze.ParamKeeper";
    private static ParamKeeper mParamKeeper;
    private SQLiteDatabase mDatabase;
    private Context mContext;



    public static ParamKeeper get(Context context){
        if (mParamKeeper == null){
            mParamKeeper = new ParamKeeper(context);
        }
        return mParamKeeper;

    }

    public ParamKeeper(Context context) {
        mContext = context;
        mDatabase = new LocalDbHelper(mContext).getWritableDatabase();
    }

    public List<ParamEntry> getParameterList(){

        List<ParamEntry> paramEntryList = new LinkedList<>();
        ParametersCursorWrapper cursor = queryParams(null,null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                paramEntryList.add(cursor.getParameter());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return paramEntryList;
    }

    ParametersCursorWrapper queryParams(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ParametersTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new ParametersCursorWrapper(cursor);

    }

    public ParamEntry getParamEntry(UUID paramId) throws Exception{

        ParametersCursorWrapper cursor = queryParams(
                ParametersTable.Cols.ID + " = ? ",
                new String[]{paramId.toString()});

        try {
            if(cursor.getCount() == 0){
                throw new Exception(TAG + " : "+ R.string.ex_parameter_not_found);
            }
            cursor.moveToFirst();
            return cursor.getParameter();
        } finally {
            cursor.close();
        }
    }

    public void delete(ParamEntry paramEntry){

        mDatabase.delete(ParametersTable.NAME,ParametersTable.Cols.ID + " =? ",new String[]{paramEntry.getId().toString()});

    }

    public void add(ParamEntry paramEntry){

        mDatabase.insert(ParametersTable.NAME,null,getContentValues(paramEntry));

    }


    public  void update(ParamEntry paramEntry){

        mDatabase.update(
                ParametersTable.NAME,
                getContentValues(paramEntry),
                ParametersTable.Cols.ID + " = ? ",
                new String[]{paramEntry.getId().toString()});
    }



    private static ContentValues getContentValues(ParamEntry paramEntry){
        ContentValues values = new ContentValues();
        values.put(ParametersTable.Cols.ID,paramEntry.getId().toString());
        values.put(ParametersTable.Cols.NAME,paramEntry.getName());
        values.put(ParametersTable.Cols.VALUE, paramEntry.getValue());

        return values;
    }
}
