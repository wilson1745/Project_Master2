package wilson.com.project_master2.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class myDB extends SQLiteOpenHelper {

   private final String TAG = "myDB";
   private static final String DB_NAME = "DB.db";
   private static final String TABLE_NAME = "records";
   private static final String TABLE_PERSON = "person";

   private static final int DB_VERSION = 1;

   public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      super(context, name, factory, version);
   }

   public myDB(Context context) {
      this(context, DB_NAME, null, DB_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      try {
         String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
                 + "(_id INTEGER PRIMARY KEY autoincrement,"
                 + "start_time TEXT,"      //起始時間
                 + "end_time TEXT,"        //結束時間
                 + "sleepHour INTEGER,"    //總睡眠小時
                 + "timeOfSleep DOUBLE,"   //睡眠起始時間(小時幾點)
                 + "grade DOUBLE)";        //評分

         db.execSQL(CREATE_TABLE);

      } catch (Exception e) {
         Log.e(TAG, e.toString());
      }
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.e(TAG, "database update!");

      /*String CREATE_PERSON = "CREATE TABLE if not exists " + TABLE_PERSON
              + "(_id INTEGER PRIMARY KEY autoincrement,"
              + "age INTEGER,"
              + "sex TEXT,"
              + "height DOUBLE,"
              + "weight DOUBLE,"
              + "bmi DOUBLE,"
              + "evaluation TEXT)";

      db.execSQL(CREATE_PERSON);*/
   }
}
