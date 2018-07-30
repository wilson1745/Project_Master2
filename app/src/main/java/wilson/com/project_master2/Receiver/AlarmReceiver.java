package wilson.com.project_master2.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import wilson.com.project_master2.Service.RingtonePlayingService;

public class AlarmReceiver extends BroadcastReceiver {

   private final String TAG = "AlarmReceiver";

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.e(TAG, "The time is up, start the alarm...");
      Calendar c = Calendar.getInstance();
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int minute = c.get(Calendar.MINUTE);

      // Fetch extra strings from the intent tells the app whether the user pressed the alarm on button or the alarm off button
      String alarmState = intent.getExtras().getString("extra");
      String musicState = intent.getExtras().getString("music");
      Log.e(TAG,"alarmState: " + alarmState);
      Log.e(TAG,"musicState: " + musicState);
      Log.e(TAG,"time: " + hour + " : " + minute);

      // Create an intent to the ringtone service
      Intent service_intent = new Intent(context, RingtonePlayingService.class);

      service_intent.putExtra("extra", alarmState);
      service_intent.putExtra("music", musicState);

      // Start the ringtone service
      context.startService(service_intent);

      //	Toast.makeText(arg0, "鬧鐘時間到了！", Toast.LENGTH_SHORT).show();
   }
}
