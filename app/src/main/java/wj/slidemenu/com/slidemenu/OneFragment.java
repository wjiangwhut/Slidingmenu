package wj.slidemenu.com.slidemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wj.slidemenu.com.slidemenu.widget.SlidingMenu;

public class OneFragment extends Fragment {

	private RecyclerView mMenuRecycleView;
	private RecyclerView mMenuSecondRecycleView;
	private RecyclerView mMenuThumbnailContentRecycleView;
	private RecyclerView mMenuFullscreenContentRecycleView;
	private SlidingMenu mSlidingmenu;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
//		System.out.println("OneFragment  onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
//		System.out.println("OneFragment  onCreateView");
		View root = inflater.inflate(R.layout.one, container, false);
		mSlidingmenu = (SlidingMenu)root.findViewById(R.id.sliding_menu);
		mSlidingmenu.setViewPager(((MainActivity)getContext()).mViewPager);
		mSlidingmenu.setCustomMenu(R.layout.recycleview_menu);
		mSlidingmenu.setCustomMenuSecond(R.layout.recycleview_menu_second);
		mSlidingmenu.setCustomThumbnailContent(R.layout.recycleview_thumbnail_content);
		mSlidingmenu.setCustomFullscreenContent(R.layout.recycleview_fullscreen_content);

		mMenuRecycleView = (RecyclerView) mSlidingmenu.findViewById(R.id.recycleview_menu);
		mMenuSecondRecycleView = (RecyclerView) mSlidingmenu.findViewById(R.id.recycleview_menu_second);
		mMenuThumbnailContentRecycleView = (RecyclerView) mSlidingmenu.findViewById(R.id.recycleview_thumbnail_content);
		mMenuFullscreenContentRecycleView = (RecyclerView) mSlidingmenu.findViewById(R.id.recycleview_fullscreen_content);

		RecycleAdapter recycleAdapterMenu = new RecycleAdapter(getContext(), mSlidingmenu, RecycleAdapter.TYPE_MENU);
		RecycleAdapter recycleAdapterMenuSecond = new RecycleAdapter(getContext(), mSlidingmenu, RecycleAdapter.TYPE_MENU_SECOND);
		RecycleAdapter recycleAdapterThumbnailContent = new RecycleAdapter(getContext(), mSlidingmenu, RecycleAdapter.TYPE_MENU_THUMBNAIL_CONTENT);
		RecycleAdapter recycleAdapterFullScreenContent = new RecycleAdapter(getContext(), mSlidingmenu, RecycleAdapter.TYPE_MENU_FULLSCREEN_CONTENT);

		LinearLayoutManager managerMenu = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		LinearLayoutManager managerMenuSecond = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		LinearLayoutManager managerThumbnailContent = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		LinearLayoutManager managerFullscreenContent = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		mMenuRecycleView.setLayoutManager(managerMenu);
		mMenuSecondRecycleView.setLayoutManager(managerMenuSecond);
		mMenuThumbnailContentRecycleView.setLayoutManager(managerThumbnailContent);
		mMenuFullscreenContentRecycleView.setLayoutManager(managerFullscreenContent);
		mMenuRecycleView.setAdapter(recycleAdapterMenu);
		mMenuSecondRecycleView.setAdapter(recycleAdapterMenuSecond);
		mMenuThumbnailContentRecycleView.setAdapter(recycleAdapterThumbnailContent);
		mMenuFullscreenContentRecycleView.setAdapter(recycleAdapterFullScreenContent);
		return root;
	}
	
	@Override
	public void onPause() {
//		System.out.println("OneFragment  onPause");
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
//		System.out.println("OneFragment  onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
//		System.out.println("OneFragment  onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
//		System.out.println("OneFragment  onDestroyView");
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onStop() {
//		System.out.println("OneFragment  onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onStart() {
//		System.out.println("OneFragment  onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}

	public SlidingMenu getSlidingmenu() {
		return mSlidingmenu;
	}
}
