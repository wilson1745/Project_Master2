package wilson.com.project_master2.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.pow;

import wilson.com.project_master2.R;
import wilson.com.project_master2.Receiver.AlarmReceiver;
import wilson.com.project_master2.Receiver.MyReceiver;
import wilson.com.project_master2.SQLiteOpenHelper.myDB;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {

   private Sensor mMagneticSensor;
   private Sensor mAccelerometerSensor;

   private SensorManager mSensorManager;
   private LocationManager mLocationManager;

   //初始化位置服務中的一個位置的數據來源
   private String provider;
   String suggest = "";

   int num1 = 0;

   public static final String TAG = "AlarmFragment";
   private Calendar calendar;
   /**
    * Called when the activity is first created.
    */
   private TextView hourt, maohao1, maohao2, word1, mint, sec;
   private Button start, reset, cancelAlarmBtn;
   private long timeusedinsec;
   private boolean isstop = false;
   private int alarmHour, alarmMinute;
   private int timeOfSleep;
   private LinearLayout jishi;

   private String start_time, end_time;
   java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone("GMT+1"); //目前使用英國時間

   @SuppressLint("HandlerLeak")
   private Handler mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
         super.handleMessage(msg);
         switch(msg.what) {
            case 1:
               // 添加更新ui的代码
               if(!isstop) {
                  updateView();
                  mHandler.sendEmptyMessageDelayed(1, 1000);
               }
               break;
            case 0:
               break;
         }
      }
   };
   public AlarmFragment() {
      // Required empty public constructor
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Log.e(TAG,"AlarmFragment被初始化了");
      View view = inflater.inflate(R.layout.fragment_alarm, container, false);
      // Inflate the layout for this fragment
      init(view);

      return view;
   }

   public static AlarmFragment newInstance(String content) {
      Bundle bundle = new Bundle();

      bundle.putString("ARGS", content);
      AlarmFragment fragment = new AlarmFragment();
      fragment.setArguments(bundle);

      return fragment;
   }

   public void init(View view) {
      initViews(view);
      calendar = Calendar.getInstance(); //獲取日曆實例
      final Button timeBtn = (Button) view.findViewById(R.id.timeBtn); //獲取時間按鈕

      timeBtn.setOnClickListener(new Button.OnClickListener() { //設置時間
         @Override
         public void onClick(View arg0) {
            Log.e(TAG, "Click the time button to set time");
            calendar.setTimeInMillis(System.currentTimeMillis());
            new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
               @Override
               public void onTimeSet(TimePicker arg0, int h, int m) {
                  alarmHour = h;
                  alarmMinute = m;
                  //更新按鈕上的時間
                  timeBtn.setText(formatTime(h, m));
                  //設置日曆的時間，主要是讓日曆的年月日和當前同步
                  calendar.setTimeInMillis(System.currentTimeMillis());
                  //設置日曆的小時和分鐘
                  calendar.set(Calendar.HOUR_OF_DAY, h);
                  calendar.set(Calendar.MINUTE, m);
                  //將秒和毫秒設置為0
                  calendar.set(Calendar.SECOND, 0);
                  calendar.set(Calendar.MILLISECOND, 0);
                  //建立Intent和PendingIntent來調用鬧鐘管理器
                  Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                  PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                  //獲取鬧鐘管理器
                  AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                  //設置鬧鐘
                  alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10 * 1000, pendingIntent);
                  Log.e(TAG, "Set the time to " + formatTime(h, m));
                  //cancelAlarmBtn.setVisibility(View.VISIBLE);
               }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
         }
      });

      //取消鬧鐘按鈕事件監聽
      cancelAlarmBtn.setOnClickListener(new Button.OnClickListener() {
         @Override
         public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            //獲取鬧鐘管理器
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            //cancelAlarmBtn.setVisibility(View.INVISIBLE);
         }
      });

      //獲取感測器的管理器
      mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

      //對Android版本做相容處理，對於Android 6及以上版本需要向使用者請求授權，而低版本的則直接調用
      if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         //Do something here...
      }
   }

   private void initViews(View view) {
      jishi   = (LinearLayout) view.findViewById(R.id.jishi);
      maohao1 = (TextView) view.findViewById(R.id.maohao1);
      maohao2 = (TextView) view.findViewById(R.id.maohao2);
      hourt   = (TextView) view.findViewById(R.id.hourt);
      mint    = (TextView) view.findViewById(R.id.mint);
      sec     = (TextView) view.findViewById(R.id.sec);
      start   = (Button) view.findViewById(R.id.start);
      reset   = (Button) view.findViewById(R.id.reset);
      word1   = (TextView) view.findViewById(R.id.word1);
      cancelAlarmBtn = (Button) view.findViewById(R.id.cancelAlarmBtn);

      //開始記錄
      start.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View arg0) {
            suggest = "";
            String x = "00";
            hourt.setText(x);
            mint.setText(x);
            sec.setText(x);

            findCalendar("start_time");
            Log.e(TAG, "Date_start: " + start_time);


            mHandler.removeMessages(1);
            isstop = false;
            mHandler.sendEmptyMessage(1);
            Calendar cl = Calendar.getInstance();
            cl.setTimeZone(timeZone);
            timeOfSleep = cl.get(Calendar.HOUR_OF_DAY);
            mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);

            //對Android版本做相容處理，對於Android 6及以上版本需要向使用者請求授權，而低版本的則直接調用
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               //Do something here...
            }
            start.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
            //開始計時的字樣
            jishi.setVisibility(View.VISIBLE);
            word1.setText("睡眠開始");
         }
      });

      //結束紀錄
      reset.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View arg0) {
            saveData();
            timeusedinsec = 0;
            isstop = true;
            mSensorManager.unregisterListener(mSensorEventListener);

            //對Android版本做相容處理，對於Android 6及以上版本需要向使用者請求授權，而低版本的則直接調用
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               //Do something here...
            }
            reset.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            word1.setText("Your Sleep Length ");
         }
      });
   }

   public String formatTime(int h, int m) {
      StringBuffer buf = new StringBuffer();

      if(h < 10) buf.append("0" + h);
      else buf.append(h);
      buf.append(" : ");
      if (m < 10) buf.append("0" + m);
      else buf.append(m);

      return buf.toString();
   }

   private void findCalendar(String time) {
      Calendar cl = Calendar.getInstance();
      cl.setTimeZone(timeZone);

      int year = cl.get(Calendar.YEAR);
      int month = cl.get(Calendar.MONTH) + 1;
      int day = cl.get(Calendar.DAY_OF_MONTH);
      int hours = cl.get(Calendar.HOUR_OF_DAY);
      int minutes = cl.get(Calendar.MINUTE);
      int second = cl.get(Calendar.SECOND);

      String month_s = formatZero(month);
      String day_s = formatZero(day);
      String hours_s = formatZero(hours);
      String minutes_s = formatZero(minutes);
      String second_s = formatZero(second);

      if(time.equals("start_time")) start_time = year + "." + month_s + "." + day_s + " " + hours_s + ":" + minutes_s + ":" + second_s;
      else if(time.equals("end_time")) end_time = year + "." + month_s + "." + day_s + " " + hours_s + ":" + minutes_s + ":" + second_s;
   }

   private String formatZero(int time) {
      String shift;

      if(time < 10) shift = "0" + String.valueOf(time);
      else shift = String.valueOf(time);

      return shift;
   }

   private void updateView() {
      timeusedinsec += 1;
      int hour = (int) (timeusedinsec / 60 / 60) % 60;
      int minute = (int) (timeusedinsec / 60) % 60;
      int second = (int) (timeusedinsec % 60);

      hourt.setText(formatZero(hour));
      mint.setText(formatZero(minute));
      sec.setText(formatZero(second));

      /*if(hour < 10) hourt.setText("0" + hour);
      else hourt.setText("" + hour);
      if(minute < 10) mint.setText("0" + minute);
      else mint.setText("" + minute);
      if(second < 10) sec.setText("0" + second);
      else sec.setText("" + second);*/
   }

   //Sensor Event Listener
   private SensorEventListener mSensorEventListener = new SensorEventListener() {
      float[] accValues = new float[3];
      float[] magValues = new float[3];

      //當感測器資料更新的時候，系統會回檔監聽器裡的onSensorChange函數，便可以在這裡對感測器資料進行相應處理
      @Override
      public void onSensorChanged(SensorEvent event) {
         switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
               accValues = event.values;
               //使用加速度感測器可以實現了檢測手機的搖一搖功能，通過搖一搖，彈出是否退出應用的對話方塊，選擇是則退出應用
               double value = 1;
               double max = 3;
               if(Math.abs(accValues[0]) > value || Math.abs(accValues[1]) > value ) {
                  if(Math.abs(accValues[0]) < max || Math.abs(accValues[1]) < max ) {
                     num1++;
                     //Log.e(TAG, "Shake and shake: " + num1);
                  }
               }
               break;
            case Sensor.TYPE_MAGNETIC_FIELD:
               magValues = event.values;
               break;
            default:
               break;
         }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }
   };

   private void saveData() {
      int numberOfPlay =  MyReceiver.getNum2();
      double grade_numberOfPlay = pow(1.01, -numberOfPlay);

      if(numberOfPlay > 20) {
         suggest += "睡覺玩手機會影響主人的學習哦。";
      }

      double grade_numberOfTouch;
      int numberOfTouch = num1;

      if(numberOfTouch > 2000) {
         grade_numberOfTouch = 0.89;
         suggest += "主人最近是不是輾轉難眠呢~睡眠品質不夠高呢~";
      }
      else {
         grade_numberOfTouch = 1;
      }

      double sleepHour = (double) (timeusedinsec / 60 / 60) % 60;
      double grade_sumOfSleep;// = Math.pow(Math.E, -Math.pow((sleepHour-8), 2));

      Calendar cl = Calendar.getInstance();
      cl.setTimeZone(timeZone);
      int nowHour = cl.get(Calendar.HOUR_OF_DAY);
      int nowMinute = cl.get(Calendar.MINUTE);
      int subOfAlarm;
      double alarmTime = alarmHour + alarmMinute / 60, nowTime = nowHour + nowMinute / 60;
      if(nowTime > alarmTime) {
         subOfAlarm = (nowHour-alarmHour) * 60 + nowMinute - alarmMinute;
      }
      else if(nowTime == alarmTime) {
         subOfAlarm = Math.abs(nowMinute - alarmMinute);
      }
      else {
         subOfAlarm = (alarmHour - nowHour) * 60 + nowMinute - alarmMinute;
      }

      double x1 = subOfAlarm / 20;
      double grade_subOfAlarm; // = (Math.pow(Math.E, (-3-x1))*Math.pow(x1+3, 3))/1.35;

      if(x1 > 2) {
         suggest += "主人最近睡眠不深，不知道怎麼了？";
         grade_subOfAlarm = 0.8;
      }
      else if(x1 > -1) {
         suggest += "主人，您的生物鐘很規律哦~希望您繼續保持呢~";
         grade_subOfAlarm = 0.99;
      }
      else {
         suggest += "主人最近有點賴床哦~";
         grade_subOfAlarm = 0.9;
      }


      if(sleepHour > 9.3) {
         suggest += "主人睡的時間太長了噢~萌萌早就醒了哼~";
         grade_sumOfSleep = 0.88;
      }
      else if(sleepHour > 6) {
         suggest += "主人的睡覺時長很健康呐~";
         grade_sumOfSleep = 0.96;
      }
      else {
         suggest += "萌萌最近和主人一樣，睡眠缺乏~";
         grade_sumOfSleep = 0.6;
      }

      double grade_timeOfSleep;
      if (timeOfSleep > 22 && timeOfSleep < 23) {
         grade_timeOfSleep = 1;
      }
      else if (timeOfSleep >= 23) {
         grade_timeOfSleep = 0.95;
         suggest += "另外，主人最近睡得有點遲啊~";
      }
      else if (timeOfSleep > 0 && timeOfSleep < 2) {
         grade_timeOfSleep = 0.75;
         suggest += "另外，主人最近睡得有點遲啊~";
      }
      else if (timeOfSleep > 11 && timeOfSleep < 15) {
         grade_timeOfSleep = 1;
      }
      else if (timeOfSleep > 2 && timeOfSleep < 6){
         grade_timeOfSleep = 0.65;
         suggest += "最近在趕project嗎？這樣對身體不好的...\n";
      }
      else {
         grade_timeOfSleep = 0.95;
      }

      double grade = 100 * grade_subOfAlarm
              * grade_sumOfSleep * grade_numberOfTouch
              * grade_timeOfSleep * pow(grade_numberOfPlay, 0.2);
      //Math.pow(grade_subOfAlarm, 0.5Math.pow(grade_sumOfSleep, 0.4))*Math.pow(grade_timeOfSleep, 1)*Math.pow(grade_numberOfPlay, 0.2)*grade_numberOfTouch;
      grade = 10 * pow(grade, 0.5);

      findCalendar("end_time");
      Log.e(TAG, "Date_ended: " + end_time);

      SharedPreferences sharedpref = getActivity().getSharedPreferences("info", MODE_PRIVATE);
      float z = sharedpref.getFloat("grade", 100);
      long grade1 = Math.round(grade);
      z = (float) (0.2*z + 0.8*grade1);
      SharedPreferences.Editor editor = sharedpref.edit();
      editor.putFloat("grade", z);
      editor.putString("suggestion", suggest);
      editor.apply();

      //////////////////
      Log.e(TAG, "Suggestion: " + suggest);
      Log.e(TAG, "Grade: " + grade);
      //////////////////

      myDB dbHelp = new myDB(getActivity());
      final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
      ContentValues cv = new ContentValues();
      cv.put("start_time" , start_time);
      cv.put("end_time", end_time);
      cv.put("sleepHour", sleepHour);
      cv.put("timeOfSleep", timeOfSleep);
      cv.put("grade", z);
      sqLiteDatabase.insert("records", null, cv);
   }

}
