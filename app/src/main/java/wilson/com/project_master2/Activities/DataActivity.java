package wilson.com.project_master2.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import wilson.com.project_master2.R;

public class DataActivity extends AppCompatActivity {

   TextView start_time_v, end_time_v, hsleep_v, tsleep_v, grade_v, suggestion_v;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data);

      start_time_v = findViewById(R.id.start_time_v);
      end_time_v = findViewById(R.id.end_time_v);
      hsleep_v = findViewById(R.id.hsleep_v);
      tsleep_v = findViewById(R.id.tsleep_v);
      grade_v = findViewById(R.id.grade_v);
      suggestion_v = findViewById(R.id.suggestion_v);

      Bundle bundle = getIntent().getExtras();
      String start_times = bundle.getString("start_time");
      String end_time = bundle.getString("end_time");
      String hsleep = bundle.getString("sleepHour");
      String tsleep = bundle.getString("timeOfSleep");
      String grades = bundle.getString("grade");
      String suggestion = bundle.getString("suggestion");

      start_time_v.setText(start_times);
      end_time_v.setText(end_time);
      hsleep_v.setText(hsleep);
      tsleep_v.setText(tsleep);
      grade_v.setText(grades);
      suggestion_v.setText(suggestion);

      getSupportActionBar().setTitle(start_times);
      setBackbutton();
   }

   private void setBackbutton() {
      if(getSupportActionBar() != null) {
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
      }
   }

   @Override
   public void onBackPressed() {
      int count = getFragmentManager().getBackStackEntryCount();

      if(count == 0) super.onBackPressed();
      else getFragmentManager().popBackStack();
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if(item.getItemId() == android.R.id.home) finish();

      return super.onOptionsItemSelected(item);
   }
}
