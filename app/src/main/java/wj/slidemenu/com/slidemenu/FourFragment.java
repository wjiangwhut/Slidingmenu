package wj.slidemenu.com.slidemenu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FourFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
//		System.out.println("FourFragment onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
//		System.out.println("FourFragment onCreateView");
		return inflater.inflate(R.layout.four, container, false);
	}
	
	
	@Override
	public void onPause() {
//		System.out.println("FourFragment  onPause");
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
//		System.out.println("FourFragment  onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
//		System.out.println("FourFragment  onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
//		System.out.println("FourFragment  onDestroyView");
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onStop() {
//		System.out.println("FourFragment  onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onStart() {
//		System.out.println("FourFragment  onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}
	
}
