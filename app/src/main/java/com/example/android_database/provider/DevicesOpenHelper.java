package com.example.android_database.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import com.example.android_database.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DevicesOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = DevicesOpenHelper.class.getSimpleName();
    private static final int SCHEMA_VERSION = 1;
    private static final String DB_NAME = "devices.db";

    private final Context context;

    private static DevicesOpenHelper instance;

    public synchronized static DevicesOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DevicesOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * 간단한 OpenHelper 인스턴스를 생성
     * @param context 에셋(assets)을 읽기 위한 Context.
     */
    private DevicesOpenHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA_VERSION);
        this.context = context;
    }

    @Override
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)    // API 16 이상
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        // write-ahead-log와 외래키를 활성화한다.
        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate(SQLiteDatabase db)");
        for (int i = 1; i <= SCHEMA_VERSION; i++) {
            applySqlFile(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = (oldVersion + 1); i <= newVersion; i++) {
            applySqlFile(db, i);
        }
    }

    private void applySqlFile(SQLiteDatabase db, int version) {
        BufferedReader reader = null;

        try {
            String filename = String.format("%s.%d.sql", DB_NAME, version);
            final InputStream inputStream = context.getAssets().open(filename);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            final StringBuilder statement = new StringBuilder();

            for (String line; (line = reader.readLine()) != null;) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Reading line -> " + line);
                }
                // 빈 문자열과 주석 처리 부분 무시
                if (!TextUtils.isEmpty(line) && !line.startsWith("--")) {
                    statement.append(line.trim());
                }

                if (line.endsWith(";")) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Running statement -> " + statement);
                    }
                    db.execSQL(statement.toString());
                    statement.setLength(0);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not apply SQL file", e);
        } finally {
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) { Log.w(TAG, "Could not close reader", e); }
            }
        }
    }
}
