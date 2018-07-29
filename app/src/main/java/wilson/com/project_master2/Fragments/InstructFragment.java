package wilson.com.project_master2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wilson.com.project_master2.Expand_List.ParentLevelAdapter;
import wilson.com.project_master2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructFragment extends Fragment {

   public static final String TAG = "InstructFragment";

   private View view;
   private Context context;
   private TextView textView;
   private ExpandableListView mExpandableListView;
   private String[] mItemHeaders;

   public InstructFragment() {
      // Required empty public constructor
   }


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Log.e(TAG,"InstructFragment被初始化了");
      view = inflater.inflate(R.layout.fragment_instruct, container, false);
      context = getActivity();
      Log.e(TAG, "Come into InstructFragment");

      // Init top level data
      List<String> listDataHeader = new ArrayList<>();
      mItemHeaders = getResources().getStringArray(R.array.instructions_main);
      Collections.addAll(listDataHeader, mItemHeaders);
      mExpandableListView = view.findViewById(R.id.expandableListView_Parent); //expand_main.xml

      if(mExpandableListView != null) {
         ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(context, listDataHeader);
         mExpandableListView.setAdapter(parentLevelAdapter);
      }

      mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
         @Override
         public void onGroupExpand(int groupPosition) {
            for (int i = 0; i < mItemHeaders.length; i++) {
               if (groupPosition != i) {
                  mExpandableListView.collapseGroup(i);
               }
            }
         }
      });

      // Inflate the layout for this fragment
      return view;
   }

   public static InstructFragment newInstance(String content) {
      Bundle bundle = new Bundle();

      bundle.putString("ARGS", content);
      InstructFragment fragment = new InstructFragment();
      fragment.setArguments(bundle);

      return fragment;
   }

   /**
    * 需要界面重新展示时调用这个方法
    */
   @Override
   public void onHiddenChanged(boolean hidden) {
      // TODO Auto-generated method stub
      super.onHiddenChanged(hidden);
      if(!hidden) {
         for(int i = 0; i < mItemHeaders.length; i++) {
            mExpandableListView.collapseGroup(i);
         }
      }
   }

}
