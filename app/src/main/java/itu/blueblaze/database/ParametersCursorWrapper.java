package itu.blueblaze.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import itu.blueblaze.ParamEntry;

/**
 * Created by KaaN on 5-12-2016.
 */

public class ParametersCursorWrapper extends CursorWrapper {

    public  ParametersCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public ParamEntry getParameter(){
        ParamEntry paramEntry = new ParamEntry();

        String idString = getString(getColumnIndex(DbSchema.ParametersTable.Cols.ID));
        paramEntry.setId(UUID.fromString(idString));

        String nameString = getString(getColumnIndex(DbSchema.ParametersTable.Cols.NAME));
        paramEntry.setName(nameString);

        String valueString = getString(getColumnIndex(DbSchema.ParametersTable.Cols.VALUE));
        paramEntry.setValue(Integer.parseInt(valueString));

        return paramEntry;
    }
}
