# Android-Database
  
### SQLiteOpenHelper
+ onCreate() 메서드 호출 시점
```java 
  DevicesOpenHelper devicesOpenHelper = DevicesOpenHelper.getInstance(this);
  SQLiteDatabase sqlDB;
  sqlDB = devicesOpenHelper.getReadableDatabase();
  //sqlDB = devicesOpenHelper.getWritableDatabase();
  sqlDB.close();
```
&rarr; getReadableDatabase(), getWritableDatabase() 메소드가 호출될 때 호출된다.
