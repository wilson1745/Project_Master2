package wilson.com.project_master2.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import wilson.com.project_master2.R;

public class DataActivity extends AppCompatActivity {

   TextView start_time, grade;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data);

      start_time = findViewById(R.id.start_time);
      grade = findViewById(R.id.grade);

      Bundle bundle = getIntent().getExtras();
      String start_times = bundle.getString("start_time");
      String grades = bundle.getString("grade");
      start_time.setText(start_times);
      grade.setText(grades);
   }
}
