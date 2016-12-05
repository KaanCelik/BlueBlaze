package itu.blueblaze.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import itu.blueblaze.database.DbSchema.ParametersTable;


/**
 * Created by KaaN on 22-11-2016.
 */

public class LocalDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "blueblazedatabase.db";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + ParametersTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ParametersTable.Cols.ID + "," +
                ParametersTable.Cols.NAME + "," +
                ParametersTable.Cols.VALUE +
                ")";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
