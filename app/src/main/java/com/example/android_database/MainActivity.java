package com.example.android_database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_database.databinding.MainBinding;
import com.example.android_database.provider.temp.myDBHelper;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    private MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myHelper = new myDBHelper(this);

        /** 초기화 */
        binding.btnInit.setOnClickListener(view -> {
            sqlDB = myHelper.getWritableDatabase();
            myHelper.onUpgrade(sqlDB, 1, 2);
            sqlDB.close();
            shortToast("데이터 초기화");
        });

        /** 입력 */
        binding.btnInsert.setOnClickListener(view -> {
            String name = binding.edtName.getText().toString();
            String number = binding.edtNumber.getText().toString();
            if (name.isEmpty() || number.isEmpty()) {
                shortToast("데이터를 입력해주세요.");
            } else {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO " + myDBHelper.TBL_NAME + " VALUES ('" + name + "' , " + number + ");");
                sqlDB.close();
                shortToast("데이터 저장");
                resetEdt();
            }
        });

        /** 조회 */
        binding.btnSelect.setOnClickListener(view -> {
            sqlDB = myHelper.getReadableDatabase();

            // 커서를 선언하고 모든 테이블을 조회한 후 커서에 대입한다.
            // 즉, 테이블에 입력된 모든 행 데이터가 커서 변수에 들어있는 상태가 되며, 현재는 첫 번째 행을 가리키고 있다.
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM " + myDBHelper.TBL_NAME + ";", null);

            String strNames = "그룹 이름" + "\r\n" + "----------------" + "\r\n";
            String strNumbers = "인원" + "\r\n" + "----------------" + "\r\n";

            while (cursor.moveToNext()) {
                strNames += cursor.getString(0) + "\r\n";
                strNumbers += cursor.getString(1) + "\r\n";
            }

            binding.edtNameResult.setText(strNames);
            binding.edtNumberResult.setText(strNumbers);

            cursor.close();
            sqlDB.close();
        });
    }

    private void shortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void resetEdt() {
        binding.edtName.setText("");
        binding.edtNumber.setText("");
    }
}