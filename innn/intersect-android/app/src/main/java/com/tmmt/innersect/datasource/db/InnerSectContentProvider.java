//package com.tmmt.innersect.datasource.db;
//
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.text.TextUtils;
//
//import com.socks.library.KLog;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class InnerSectContentProvider extends ContentProvider {
//
//    private static final int ADDRESSES = 1;
//    private static final int ADDRESS_ID = 2;
//    private DatabaseHelper mOpenHelper;
//    private static final UriMatcher sUriMatcher;
//    private static final Map<String,String> sProjectionMap;
//    static {
//        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(InnerSect.AUTHORITY, InnerSect.Address.PATH, ADDRESSES);
//        sUriMatcher.addURI(InnerSect.AUTHORITY, InnerSect.Address.PATH_ID, ADDRESS_ID);
//        sProjectionMap = new HashMap<String, String>();
//        // Maps the string "_ID" to the column name "_ID"
////        sProjectionMap.put(GirlData.GirlInfo._ID,  GirlData.GirlInfo._ID);
////        // Maps "title" to "title"
////        sProjectionMap.put(GirlData.GirlInfo.COLUMN_DES, GirlData.GirlInfo.COLUMN_DES);
////        // Maps "note" to "note"
////        sProjectionMap.put( GirlData.GirlInfo.COLUMN_COVER_URL,  GirlData.GirlInfo.COLUMN_COVER_URL);
////        sProjectionMap.put( GirlData.GirlInfo.COLUMN_DETAIL_URL,  GirlData.GirlInfo.COLUMN_DETAIL_URL);
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        // Opens the database object in "write" mode.
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        String finalWhere;
//
//        int count;
//
//        // Does the delete based on the incoming URI pattern.
//        switch (sUriMatcher.match(uri)) {
//
//            // If the incoming pattern matches the general pattern for notes, does a delete
//            // based on the incoming "where" columns and arguments.
//            case ADDRESSES:
//                count = db.delete(
//                        InnerSect.Address.TABLE_NAME,  // The database table name
//                        selection,                     // The incoming where clause column names
//                        selectionArgs                  // The incoming where clause values
//                );
//                break;
//
//            // If the incoming URI matches a single note ID, does the delete based on the
//            // incoming data, but modifies the where clause to restrict it to the
//            // particular note ID.
//            case ADDRESS_ID:
//                /*
//                 * Starts a final WHERE clause by restricting it to the
//                 * desired note ID.
//                 */
//                finalWhere =
//                        InnerSect.Address._ID +                              // The ID column name
//                                " = " +                                          // test for equality
//                                uri.getPathSegments().get(1);
//
//                // If there were additional selection criteria, append them to the final
//                // WHERE clause
//                if (selection != null) {
//                    finalWhere = finalWhere + " AND " + selection;
//                }
//
//                // Performs the delete.
//                count = db.delete(
//                        InnerSect.Address.TABLE_NAME,  // The database table name.
//                        finalWhere,                // The final WHERE clause
//                        selectionArgs                  // The incoming where clause values.
//                );
//                break;
//
//            // If the incoming pattern is invalid, throws an exception.
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//
//        /*Gets a handle to the content resolver object for the current context, and notifies it
//         * that the incoming URI changed. The object passes this along to the resolver framework,
//         * and observers that have registered themselves for the provider are notified.
//         */
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        // Returns the number of rows deleted.
//        return count;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        // TODO: Implement this to handle requests for the MIME type of the data
//        // at the given URI.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues initialValues) {
//        if (sUriMatcher.match(uri) != ADDRESSES) {
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//
//        ContentValues values;
//        // If the incoming values map is not null, uses it for the new values.
//        if (initialValues != null) {
//            values = new ContentValues(initialValues);
//        } else {
//            // Otherwise, create a new value map
//            values = new ContentValues();
//        }
//
//        Long now = Long.valueOf(System.currentTimeMillis());
//        if (!values.containsKey(InnerSect.Address.CREATE_DATE)) {
//            values.put(InnerSect.Address.CREATE_DATE, now);
//        }
//
//        // Opens the database object in "write" mode.
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//
//        // Performs the insert and returns the ID of the new note.
//        long rowId = db.insertWithOnConflict(
//                InnerSect.Address.TABLE_NAME,        // The table to insert into.
//                InnerSect.Address.CREATE_DATE,  // A hack, SQLite sets this column value to null
//                // if values is empty.
//                values, // A map of column names, and the values to insert
//                SQLiteDatabase.CONFLICT_REPLACE
//                // into the columns.
//        );
//
//
//        // If the insert succeeded, the row ID exists.
//        if (rowId > 0) {
//            // Creates a URI with the note ID pattern and the new row ID appended to it.
//            Uri noteUri = ContentUris.withAppendedId(InnerSect.Address.CONTENT_ID_URI_BASE, rowId);
//            // Notifies observers registered against this provider that the data changed.
//            getContext().getContentResolver().notifyChange(noteUri, null);
//            return noteUri;
//        }
//        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
//        throw new SQLException("Failed to insert row into " + uri);
//    }
//
//    @Override
//    public boolean onCreate() {
//        mOpenHelper=new DatabaseHelper(getContext());
//        return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection,
//                        String[] selectionArgs, String sortOrder) {
//
//        // Constructs a new query builder and sets its table name
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(InnerSect.Address.TABLE_NAME);
//
//        /**
//         * Choose the projection and adjust the "where" clause based on URI pattern-matching.
//         */
//        switch (sUriMatcher.match(uri)) {
//            // If the incoming URI is for notes, chooses the Notes projection
//            case ADDRESSES:
//                qb.setProjectionMap(sProjectionMap);
//                break;
//
//           /* If the incoming URI is for a single note identified by its ID, chooses the
//            * note ID projection, and appends "_ID = <noteID>" to the where clause, so that
//            * it selects that single note
//            */
//            case ADDRESS_ID:
//                //qb.setProjectionMap(sProjectionMap);
//                qb.appendWhere(
//                        InnerSect.Address._ID +    // the name of the ID column
//                                "=" +
//                                // the position of the note ID itself in the incoming URI
//                                uri.getPathSegments().get(1));
//                break;
//
//            default:
//                // If the URI doesn't match any of the known patterns, throw an exception.
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//        String orderBy;
//        // If no sort order is specified, uses the default
//        if (TextUtils.isEmpty(sortOrder)) {
//            orderBy = InnerSect.DEFAULT_SORT_ORDER;
//        } else {
//            // otherwise, uses the incoming sort order
//            orderBy = sortOrder;
//        }
//        // Opens the database object in "read" mode, since no writes need to be done.
//        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//
//       /*
//        * Performs the query. If no problems occur trying to read the database, then a Cursor
//        * object is returned; otherwise, the cursor variable contains null. If no records were
//        * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
//        */
//        Cursor c = qb.query(
//                db,            // The database to query
//                projection,    // The columns to return from the query
//                selection,     // The columns for the where clause
//                selectionArgs, // The values for the where clause
//                null,          // don't group the rows
//                null,          // don't filter by row groups
//                orderBy        // The sort order
//        );
//
//        // Tells the Cursor what URI to watch, so it knows when its source data changes
//        c.setNotificationUri(getContext().getContentResolver(), uri);
//        return c;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection,
//                      String[] selectionArgs) {
//        // Opens the database object in "write" mode.
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        int count;
//        String finalWhere;
//
//        // Does the update based on the incoming URI pattern
//        switch (sUriMatcher.match(uri)) {
//
//            // If the incoming URI matches the general notes pattern, does the update based on
//            // the incoming data.
//            case ADDRESSES:
//
//                // Does the update and returns the number of rows updated.
//                count = db.update(
//                        InnerSect.Address.TABLE_NAME, // The database table name.
//                        values,                   // A map of column names and new values to use.
//                        selection,                    // The where clause column names.
//                        selectionArgs                 // The where clause column values to select on.
//                );
//                break;
//
//            // If the incoming URI matches a single note ID, does the update based on the incoming
//            // data, but modifies the where clause to restrict it to the particular note ID.
//            case ADDRESS_ID:
//                // From the incoming URI, get the note ID
//                /*
//                 * Starts creating the final WHERE clause by re
//                 */
//                finalWhere = InnerSect.Address._ID + " = " +  uri.getPathSegments().get(1);
//
//                // If there were additional selection criteria, append them to the final WHERE
//                // clause
//                if (selection !=null) {
//                    finalWhere = finalWhere + " AND " + selection;
//                }
//
//
//                // Does the update and returns the number of rows updated.
//                count = db.update(
//                        InnerSect.Address.TABLE_NAME, // The database table name.
//                        values,                   // A map of column names and new values to use.
//                        finalWhere,               // The final WHERE clause to use
//                        // placeholders for whereArgs
//                        selectionArgs                 // The where clause column values to select on, or
//                        // null if the values are in the where argument.
//                );
//                break;
//            // If the incoming pattern is invalid, throws an exception.
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//
//        /*Gets a handle to the content resolver object for the current context, and notifies it
//         * that the incoming URI changed. The object passes this along to the resolver framework,
//         * and observers that have registered themselves for the provider are notified.
//         */
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        // Returns the number of rows updated.
//        return count;
//    }
//
//
//
//
//    //*****************************//////////////////////
//
//
//    private static final String DATABASE_NAME = "InnerSect.db";
//    private static final int VERSION = 1;
//
//
//    static class DatabaseHelper extends SQLiteOpenHelper {
//
//        DatabaseHelper(Context context) {
//            super(context, DATABASE_NAME, null, VERSION);
//
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE " + InnerSect.Address.TABLE_NAME + " ("
//                    + InnerSect.Address._ID + " INTEGER PRIMARY KEY,"
//                    + InnerSect.Address.ACCOUNTID + " INTEGER,"
//                    + InnerSect.Address.NAME + " TEXT,"
//                    + InnerSect.Address.TEL + " VARCHAR(30),"
//                    + InnerSect.Address.CITY + " TEXT,"
//                    + InnerSect.Address.DETAIL + " TEXT,"
//                    + InnerSect.Address.DEFAULT + " INTEGER DEFAULT 0,"
//                    + InnerSect.Address.CREATE_DATE + " INTEGER"
//                    + ");");
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            // Logs that the database is being upgraded
//            KLog.w("Upgrading database from version " + oldVersion + " to "
//                    + newVersion + ", which will destroy all old data");
//
//            // Kills the table and existing data
//            db.execSQL("DROP TABLE IF EXISTS notes");
//
//            // Recreates the database with a new version
//            onCreate(db);
//        }
//    }
//
//}
