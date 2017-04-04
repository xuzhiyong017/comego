package com.xuzhiyong.comego.ui.main.picdetail;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duowan.fw.util.JEndLessList;
import com.xuzhiyong.comego.ImageLoader.ImageLoaderUtil;
import com.xuzhiyong.comego.bean.PictureInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 91299 on 2017/4/3   0003.
 */

public class InnerPagerAdapter extends PagerAdapter {

    private List<PictureInfo> dataList;
    private List<ImageView> imageViews = new ArrayList<>();
    private OnClickImageClick mClickImageClick;

    public void setClickImageClick(OnClickImageClick mClickImageClick) {
        this.mClickImageClick = mClickImageClick;
    }

    public interface OnClickImageClick{
        public void click();
    }

    public InnerPagerAdapter() {

    }

    @Override
    public int getCount() {
        return dataList == null ? 0:dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageViews.add(imageView);
        ImageLoaderUtil.getInstance().loadImage(dataList.get(position).getImgUrl(),imageView);
        if(imageView.getParent() == null){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mClickImageClick){
                    mClickImageClick.click();
                }
            }
        });

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public void setDataList(List<PictureInfo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}
