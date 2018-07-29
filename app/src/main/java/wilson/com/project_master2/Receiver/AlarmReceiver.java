package wilson.com.project_master2.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

   private final String TAG = "AlarmReceiver";

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.d(TAG, "The time is up, start the alarm...");
      //	Toast.makeText(arg0, "鬧鐘時間到了！", Toast.LENGTH_SHORT).show();
   }
}
