package github.hellocsl.cursorwheellayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import github.hellocsl.cursorwheellayout.R;
import github.hellocsl.cursorwheellayout.data.ImageData;

public class SimpleImageAdapter extends CursorWheelLayout.CycleWheelAdapter {
    private List<ImageData> mMenuItemDatas;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public SimpleImageAdapter(Context context, List<ImageData> menuItemDatas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mMenuItemDatas = menuItemDatas;
    }

    @Override
    public int getCount() {
        return mMenuItemDatas == null ? 0 : mMenuItemDatas.size();
    }

    @Override
    public View getView(View parent, int position) {
        ImageData item = getItem(position);
        View root = mLayoutInflater.inflate(R.layout.wheel_image_item, null, false);
        ImageView imgView = (ImageView) root.findViewById(R.id.wheel_menu_item_iv);


        Glide.with(mContext).load("file://" + item.getmImageRes_1())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgView);

       // Toast.makeText(mContext, item.getmImageRes_1() , Toast.LENGTH_SHORT).show();


        //imgView.setImageResource(item.mImageRes);
        return root;
    }

    @Override
    public ImageData getItem(int position) {
        return mMenuItemDatas.get(position);
    }

}
