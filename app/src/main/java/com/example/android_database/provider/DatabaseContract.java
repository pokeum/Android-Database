package com.example.android_database.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android_database.BuildConfig;

/**
 * [Contract(계약) Class]
 *  콘텐츠 프로바이더(ContentProvider)가 사용하는 테이블과 로우를 정의한다.
 *  --> 외부 앱에 공개하는 콘텐츠 프로바이더의 외부 API
 */
public final class DatabaseContract {

    public static final String AUTHORITY =
            String.format("%s.provider", BuildConfig.APPLICATION_ID);

    public static final Uri AUTHORITY_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public interface Database extends BaseColumns {
        /* default */ static final String PATH = "people";
        public static final String ID = "id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME  = "last_name";
    }
}
