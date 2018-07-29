package wilson.com.project_master2.Activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;

import wilson.com.project_master2.R;
import wilson.com.project_master2.SQLiteOpenHelper.myDB;

public class CalculateAvtivity extends AppCompatActivity {

   EditText per_age, per_sex, per_height, per_weight;
   Button btn_cancel, btn_save;
   int age ;
   String sex, evaluation = null;
   double height, weight, bmi;
   String TAG = "CalculateAvtivity";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_calculate);

      findView();

      btn_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });

      btn_save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            BmiEvaluation();
            Log.e(TAG, "age: " + age + " sex: " + sex + " height: " + height + " weight: " + weight + " BMI: " + bmi);

            myDB dbHelp = new myDB(CalculateAvtivity.this);
            final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM records Where _id=?", new String[]{String.valueOf(1)});

            ContentValues cv = new ContentValues();
            cv.put("age", age);
            cv.put("sex", sex);
            cv.put("height", height);
            cv.put("weight", weight);
            cv.put("bmi", bmi);
            cv.put("evaluation", evaluation);

            if(c.moveToNext()) {
               sqLiteDatabase.update("person", cv, "_id=" + String.valueOf(1), null);
            }
            else {
               sqLiteDatabase.insert("person", null, cv);
            }

         }
      });
   }

   private void findView() {
      per_age = findViewById(R.id.per_age);
      per_sex = findViewById(R.id.per_sex);
      per_height = findViewById(R.id.per_height);
      per_weight = findViewById(R.id.per_weight);
      btn_cancel = findViewById(R.id.btn_cancel);
      btn_save = findViewById(R.id.btn_save);
   }

   private void BmiEvaluation() {
      if(per_age.getText().toString().isEmpty() ||
              per_sex.getText().toString().isEmpty() ||
              per_weight.getText().toString().isEmpty() ||
              per_height.getText().toString().isEmpty()) {
         Toast.makeText(this, "Toast：Something cannot be null", Toast.LENGTH_LONG).show();
      }
      else {
         age = Integer.parseInt(per_age.getText().toString());
         sex = per_sex.getText().toString();
         height = Double.parseDouble(per_height.getText().toString());
         weight = Double.parseDouble(per_weight.getText().toString());

         double heights = height/100;
         bmi = weight/(heights*heights);

         //下面依照BMI給予Evaluation指示

      }
   }
}
