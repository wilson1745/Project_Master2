package wilson.com.project_master2.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import wilson.com.project_master2.Fragments.AlarmFragment;
import wilson.com.project_master2.MainActivity;
import wilson.com.project_master2.R;

public class RingtonePlayingService extends Service {

   final String TAG = "RingtonePlayingService";
   MediaPlayer media_song;
   int startId;
   boolean isRunning;

   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      Log.e(TAG,"LocalService Received start id " + startId + ": " + intent);

      // fetch the extra string from the alarm on/alarm off values
      String alarmState = intent.getExtras().getString("extra");
      String musicState = intent.getExtras().getString("music");

      // notification set up the notification service
      NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

      // set up an intent that goes to the Main Activity///////////////檢查是使用AlarmFragment還是MainActivity
      Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

      // set up a pending intent
      PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

      // make the notification parameters
      Notification notification_popup = new Notification.Builder(this)
              .setContentTitle("An alarm is going off!")
              .setContentText("Click me!")
              .setSmallIcon(R.drawable.ic_action_call)
              .setContentIntent(pending_intent_main_activity)
              .setAutoCancel(true)
              .build();

      // this converts the extra strings from the intent to start IDs, values 0 or 1
      assert alarmState != null;
      switch (alarmState) {
         case "alarm on":
            startId = 1;
            break;
         case "alarm off":
            startId = 0;
            Log.e("Start ID is ", alarmState);
            break;
         default:
            startId = 0;
            break;
      }

      // if there is no music playing, and the user pressed "alarm on" music should start playing
      if (!this.isRunning && startId == 1) {
         Log.e("there is no music, ", "and you want start");
         this.isRunning = true;
         this.startId = 0;
         // set up the start command for the notification
         notify_manager.notify(0, notification_popup);
         media_song = MediaPlayer.create(this, R.raw.machiko);
         media_song.setLooping(true);
         media_song.start();
      }

      // if there is music playing, and the user pressed "alarm off" music should stop playing
      else if (this.isRunning && startId == 0) {
         Log.e("there is music, ", "and you want end");
         // stop the ringtone
         media_song.stop();
         media_song.reset();
         this.isRunning = false;
         this.startId = 0;
      }

      // these are if the user presses random buttons just to bug-proof the app
      // if there is no music playing, and the user pressed "alarm off" do nothing
      else if (!this.isRunning && startId == 0) {
         Log.e("there is no music, ", "and you want end");
         this.isRunning = false;
         this.startId = 0;
      }

      // if there is music playing and the user pressed "alarm on" do nothing
      else if (this.isRunning && startId == 1) {
         Log.e("there is music, ", "and you want start");
         this.isRunning = true;
         this.startId = 1;
      }

      // can't think of anything else, just to catch the odd event
      else {
         Log.e("else ", "somehow you reached this");
      }

      return START_NOT_STICKY;
   }

   @Override
   public void onDestroy() {
      // Tell the user we stopped.
      Log.e("on Destroy called", "ta da");
      super.onDestroy();
      this.isRunning = false;
   }
}
