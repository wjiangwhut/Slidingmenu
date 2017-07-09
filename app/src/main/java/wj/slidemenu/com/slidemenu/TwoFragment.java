package wj.slidemenu.com.slidemenu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TwoFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
//		System.out.println("TwoFragment onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
//		System.out.println("TwoFragment onCreateView");
		return inflater.inflate(R.layout.two, container, false);
	}
	
	
	@Override
	public void onPause() {
//		System.out.println("TwoFragment  onPause");
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
//		System.out.println("TwoFragment  onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
//		System.out.println("TwoFragment  onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
//		System.out.println("TwoFragment  onDestroyView");
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onStop() {
//		System.out.println("TwoFragment  onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onStart() {
//		System.out.println("TwoFragment  onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}
}
