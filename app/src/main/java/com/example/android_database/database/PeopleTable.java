package com.example.android_database.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.example.android_database.BuildConfig;
import com.example.android_database.model.People;
import com.example.android_database.provider.DatabaseOpenHelper;
import com.example.android_database.throwable.PeopleException;

import java.util.ArrayList;

public class PeopleTable {

    private static final String TAG = PeopleTable.class.getSimpleName();
    public static final String TABLE_NAME = "people";

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
    public void insert(People people) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.ID.key, people.getId());
        contentValues.put(Column.FIRST_NAME.key, people.getFirstName());
        contentValues.put(Column.LAST_NAME.key, people.getLastName());

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        try {
            db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
        } catch (SQLiteException e) {
            if (BuildConfig.DEBUG) { Log.d(TAG, e.getMessage()); }
        } finally {
            db.close();
        }
    }

    /** 테이블에서 Row 수정하기 */
    public void update(People people) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.FIRST_NAME.key, people.getFirstName());
        contentValues.put(Column.LAST_NAME.key, people.getLastName());

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.update(TABLE_NAME, contentValues,
                String.format("%s = ?", Column.ID.key),
                new String[] { Integer.toString(people.getId()) });
        db.close();
    }

    /** 테이블에서 Row 치환하기 */
    // INSERT OR REPLACE : 테이블에 로우가 존재하지 않으면 삽입하고, 존재한다면 수정한다.
    public void replace(People people) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.ID.key, people.getId());
        contentValues.put(Column.FIRST_NAME.key, people.getFirstName());
        contentValues.put(Column.LAST_NAME.key, people.getLastName());

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.replace(TABLE_NAME, null, contentValues);
        db.close();
    }

    /** 테이블에서 Row 삭제하기 */
    public void delete(int id) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { Integer.toString(id) });
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

    /** Query */
    public ArrayList<People> simpleQuery() {

        ArrayList<People> peopleList = new ArrayList<>();

        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

        String[] columns = { Column.FIRST_NAME.key, Column.LAST_NAME.key, Column.ID.key };

        Cursor cursor = db.query(TABLE_NAME, columns,
                null, null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            int index;
            try {
                index = cursor.getColumnIndexOrThrow(Column.FIRST_NAME.key);
                String firstName = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(Column.LAST_NAME.key);
                String lastName = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(Column.ID.key);
                int id = cursor.getInt(index);

                peopleList.add(new People(id, firstName, lastName));
            } catch (PeopleException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
                break;
            }
        }
        cursor.close();
        return peopleList;
    }
}
