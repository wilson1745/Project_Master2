package wilson.com.project_master2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import wilson.com.project_master2.Fragments.AdvanceFragment;
import wilson.com.project_master2.Fragments.AlarmFragment;
import wilson.com.project_master2.Fragments.InstructFragment;
import wilson.com.project_master2.Fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

   private final String TAG = "MainActivity";
   BottomNavigationBar bottomNavigationBar;
   private ArrayList<Fragment> fragmentList;
   private FrameLayout frameLayout;
   private Fragment mContent; // 上次切換的Fragment

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      frameLayout= (FrameLayout) findViewById(R.id.layFrame);

      //添加標籤標籤頁
      bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom);
      bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
      bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
      bottomNavigationBar
              .addItem(new BottomNavigationItem(R.drawable.sleep_pressed, "Start Tracking")
                      .setActiveColorResource(R.color.bottombar).setInactiveIconResource(R.drawable.sleep_normal))
              .addItem(new BottomNavigationItem(R.drawable.analysis_pressed, "Sleep Records")
                      .setActiveColorResource(R.color.bottombar).setInactiveIconResource(R.drawable.analysis_normal))
              .addItem(new BottomNavigationItem(R.drawable.diary_pressed, "Advance Options")
                      .setActiveColorResource(R.color.bottombar).setInactiveIconResource(R.drawable.diary_normal))
              .addItem(new BottomNavigationItem(R.drawable.music_pressed, "Instructions")
                      .setActiveColorResource(R.color.bottombar).setInactiveIconResource(R.drawable.music_normal))
              .initialise();
      //onTabSelected(0);

      fragmentList = getFragments();
      setDefaultFragment();
      bottomNavigationBar.setTabSelectedListener(this);
   }

   private ArrayList<Fragment> getFragments() {
      ArrayList<Fragment> fragments = new ArrayList<>();

      fragments.add(AlarmFragment.newInstance("Start Tracking"));
      fragments.add(ListFragment.newInstance("Sleep Records"));
      fragments.add(AdvanceFragment.newInstance("Advance Options"));
      fragments.add(InstructFragment.newInstance("Instructions"));

      return fragments;
   }

   //set the home fragment
   private void setDefaultFragment() {
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction transaction = fm.beginTransaction();

      transaction.add(R.id.layFrame, AlarmFragment.newInstance("Start Tracking"));
      transaction.commit();
   }

   @Override
   public void onTabSelected(int position) {
      Log.e(TAG, "onTabSelected position: " + position);
      if(fragmentList != null) {
         if(position < fragmentList.size()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment from = fm.findFragmentById(R.id.layFrame); //當前的fragment
            Fragment to = fragmentList.get(position); //點擊即將跳轉的fragment
            if(to.isAdded()) {
               ft.hide(from).show(to);
            }
            else {
               ft.hide(from).add(R.id.layFrame, to);
               if(to.isHidden()) {
                  ft.show(to);
                  Log.e(TAG,"被隱藏了" + fragmentList.get(position));
               }
            }
            ft.commitAllowingStateLoss();
         }
      }
      //Log.e("TAG", "fragmentList: " + String.valueOf(fragmentList.size()));
   }

   @Override
   public void onTabUnselected(int position) {
      Log.e(TAG, "onTabUnselected position: " + position);
      if(fragmentList != null) {
         if(position < fragmentList.size()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = fragmentList.get(position);

            ft.hide(fragment);
            ft.commitAllowingStateLoss();
         }
      }
   }

   @Override
   public void onTabReselected(int position) {

   }

   private Fragment getFragment(int position){
      Fragment fragment = fragmentList.get(position);
      return fragment;
   }

   /**
    * @param from 剛顯示的Fragment,馬上就要被隱藏了
    * @param to 馬上要切換到的Fragment，一會要顯示
    */
   private void switchFrament(Fragment from,Fragment to) {
      if(from != to){
         mContent = to;
         FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
         // 才切換 判斷有沒有被添加
         if(!to.isAdded()) {
            // to沒有被添加 from隱藏
            if(from != null) {
               ft.hide(from);
            }
            // 添加to
            if(to != null) {
               ft.add(R.id.layFrame,to).commit();
            }
         }
         else {
            // to已經被添加 from隱藏
            if(from != null) {
               ft.hide(from);
            }
            // 顯示to
            if(to != null) {
               ft.show(to).commit();
            }
         }
      }
   }
}