package com.xuzhiyong.comego.ui.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuzhiyong.comego.ImageLoader.ImageLoaderUtil;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.bean.PictureInfo;
import com.xuzhiyong.comego.ui.base.adapter.ListBaseAdapter;
import com.xuzhiyong.comego.ui.base.adapter.SuperViewHolder;
import com.xuzhiyong.comego.ui.utils.UIConst;

/**
 * Created by 91299 on 2017/4/2   0002.
 */

public class DataAdapter extends ListBaseAdapter<PictureInfo>{


    private final static int WIDTH = UIConst.SCREEN_WIDTH/2;
    private final static int HEIGHT = WIDTH * 16 / 9;


    public DataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.listview_picture;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        PictureInfo imageInfo = mDataList.get(position);
        ImageLoaderUtil.getInstance().loadImage(imageInfo.getImgUrl(),(ImageView) holder.getView(R.id.image_show));
        holder.getView(R.id.image_show).getLayoutParams().height =HEIGHT;

    }
}
