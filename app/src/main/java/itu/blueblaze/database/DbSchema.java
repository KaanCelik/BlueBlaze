package itu.blueblaze.database;

/**
 * Created by KaaN on 5-12-2016.
 */

public class DbSchema {
    public static final class ParametersTable {
        public static final String NAME = "parameters";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String VALUE = "value";
        }
    }
}
