package wilson.com.project_master2.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import wilson.com.project_master2.R;
import wilson.com.project_master2.SQLiteOpenHelper.myDB;

public class PersonalActivity extends AppCompatActivity {

   TextView age_v, sex_v, height_v, weight_v;
   TextView bmi_v, evaluation_v;
   Button btn_revise;
   private String TAG = "PersonalActivity";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_personal);
      getSupportActionBar().setTitle("Personal Information");
      setBackbutton();

      findView();

      btn_revise.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(PersonalActivity.this, CalculateAvtivity.class);
            startActivity(intent);
            finish();
         }
      });
   }

   private void findView() {
      age_v    = findViewById(R.id.age_view);
      sex_v    = findViewById(R.id.sex_view);
      height_v = findViewById(R.id.height_view);
      weight_v = findViewById(R.id.weight_view);
      bmi_v    = findViewById(R.id.bmi_view);
      evaluation_v = findViewById(R.id.evalution_view);
      btn_revise = findViewById(R.id.btn_revise);

      myDB dbHelp = new myDB(PersonalActivity.this);
      final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
      Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM person Where _id=?", new String[]{String.valueOf(1)});

      if(c.getCount() == 0) {
         age_v.setText("Null");
         sex_v.setText("Null");
         height_v.setText("Null");
         weight_v.setText("Null");
         bmi_v.setText("Null");
         evaluation_v.setText("Null");
      }
      else {
         c.moveToNext(); // 移到第1筆資料
         age_v.setText(c.getString(1));
         sex_v.setText(c.getString(2));
         height_v.setText(c.getString(3));
         weight_v.setText(c.getString(4));
         bmi_v.setText(c.getString(5));
         evaluation_v.setText(c.getString(6));
      }
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
