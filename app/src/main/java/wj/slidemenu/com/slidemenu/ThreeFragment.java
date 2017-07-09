package wj.slidemenu.com.slidemenu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ThreeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
//		System.out.println("ThreeFragment onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
//		System.out.println("ThreeFragment onCreateView");
		return inflater.inflate(R.layout.three, container, false);
	}
	
	
	@Override
	public void onPause() {
//		System.out.println("ThreeFragment  onPause");
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
//		System.out.println("ThreeFragment  onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
//		System.out.println("ThreeFragment  onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
//		System.out.println("ThreeFragment  onDestroyView");
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onStop() {
//		System.out.println("ThreeFragment  onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onStart() {
//		System.out.println("ThreeFragment  onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}
	
}
