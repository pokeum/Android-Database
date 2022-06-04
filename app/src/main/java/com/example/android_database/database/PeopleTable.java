package com.example.android_database.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.example.android_database.BuildConfig;
import com.example.android_database.provider.DatabaseOpenHelper;

public class PeopleTable {

    private static final String TAG = PeopleTable.class.getSimpleName();
    private static final String TABLE = "people";

    public enum Column {
        ID("id"),
        FIRST_NAME("first_name"),
        LAST_NAME("last_name");

        public final String key;
        private Column(String key) {
            this.key = key;
        }
    }

    private final DatabaseOpenHelper databaseOpenHelper;

    public PeopleTable(DatabaseOpenHelper databaseOpenHelper) {
        this.databaseOpenHelper = databaseOpenHelper;
    }

    /** 테이블에 Row 삽입하기 */
    public void insert(int id, String firstName, String lastName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.ID.key, id);
        contentValues.put(Column.FIRST_NAME.key, firstName);
        contentValues.put(Column.LAST_NAME.key, lastName);

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        try {
            db.insertWithOnConflict(TABLE, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
        } catch (SQLiteException e) {
            if (BuildConfig.DEBUG) { Log.d(TAG, e.getMessage()); }
        } finally {
            db.close();
        }
    }

    /** 테이블에서 Row 수정하기 */
    public void update() {
        String firstName = "Robert";

        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.FIRST_NAME.key, firstName);

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.update(TABLE, contentValues,
                Column.ID.key + " = ? OR " + Column.LAST_NAME.key + " = ?",
                new String[] { "1", "Hoffman" });
        db.close();
    }

    /** 테이블에서 Row 치환하기 */
    // INSERT OR REPLACE : 테이블에 로우가 존재하지 않으면 삽입하고, 존재한다면 수정한다.
    public void replace() {
        int id = 1;
        String firstName = "Robert";

        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.ID.key, id);
        contentValues.put(Column.FIRST_NAME.key, firstName);

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.replace(TABLE, null, contentValues);
        db.close();
    }

    /** 테이블에서 Row 삭제하기 */
    public void delete(int id) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.delete(TABLE, "id = ?", new String[] { Integer.toString(id) });
        db.close();
    }

    /** Transaction */
    // 집합의 원자성(독립적으로 성공하거나 실패한다)을 갖는다.
    public void transaction() {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            // insert/update/delete
            // ...
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
