package wilson.com.project_master2.Expand_List;

import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;

public class CustomExpListView extends ExpandableListView {

   public CustomExpListView(Context context) {
        super(context);
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //Log.e("TAG", "onMeasure is working: " + widthMeasureSpec + ", " + heightMeasureSpec);
      // 螢幕寬度的關鍵
      //widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.EXACTLY);
      //widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(20000, MeasureSpec.AT_MOST);
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
   }
}

