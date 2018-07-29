package wilson.com.project_master2.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wilson.com.project_master2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvanceFragment extends Fragment {

   private final String TAG = "AdvanceFragment";

   public AdvanceFragment() {
      // Required empty public constructor
   }


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Log.e(TAG,"AdvanceFragment被初始化了");

      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_advance, container, false);
   }

   public static AdvanceFragment newInstance(String content) {
      Bundle bundle = new Bundle();

      bundle.putString("ARGS", content);
      AdvanceFragment fragment = new AdvanceFragment();
      fragment.setArguments(bundle);

      return fragment;
   }
}
