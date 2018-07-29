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
public class InstructFragment extends Fragment {

   private final String TAG = "InstructFragment";

   public InstructFragment() {
      // Required empty public constructor
   }


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Log.e(TAG,"InstructFragment被初始化了");

      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_instruct, container, false);
   }

   public static InstructFragment newInstance(String content) {
      Bundle bundle = new Bundle();

      bundle.putString("ARGS", content);
      InstructFragment fragment = new InstructFragment();
      fragment.setArguments(bundle);

      return fragment;
   }

}
