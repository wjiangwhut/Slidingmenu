package wj.slidemenu.com.slidemenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wj.slidemenu.com.slidemenu.widget.SlidingMenu;

/**
 * Created by jiangwei on 17/6/24.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ItemViewHolder>{
    private Context mContext;
    private SlidingMenu mSlidingMenu;
    private int mType;

    public static final int TYPE_MENU = 0X01;
    public static final int TYPE_MENU_SECOND = 0X02;
    public static final int TYPE_MENU_THUMBNAIL_CONTENT = 0X03;
    public static final int TYPE_MENU_FULLSCREEN_CONTENT = 0X04;

    public RecycleAdapter(Context context, SlidingMenu slidingMenu, int type) {
        this.mContext = context;
        this.mSlidingMenu = slidingMenu;
        this.mType = type;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycleview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (mType == TYPE_MENU_THUMBNAIL_CONTENT) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingMenu.showContent(true);
                }
            });
        } else if (mType == TYPE_MENU) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingMenu.switchMenu(true);
                }
            });
        } else if (mType == TYPE_MENU_SECOND) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingMenu.switchMenu(false);
                }
            });
        } else {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingMenu.showMenu(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected View mItemView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
        }
    }
}
