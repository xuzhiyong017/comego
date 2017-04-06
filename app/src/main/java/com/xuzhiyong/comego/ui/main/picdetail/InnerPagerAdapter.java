package com.xuzhiyong.comego.ui.main.picdetail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duowan.fw.util.JEndLessList;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.ImageLoader.ImageLoaderUtil;
import com.xuzhiyong.comego.bean.PictureInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by 91299 on 2017/4/3   0003.
 */

public class InnerPagerAdapter extends PagerAdapter {

    private final static String TAG = InnerPagerAdapter.class.getSimpleName();

    private List<PictureInfo> dataList;
    private List<ImageView> imageViews = new ArrayList<>();
    private OnClickImageClick mClickImageClick;
    private Context mContext;

    public void setClickImageClick(OnClickImageClick mClickImageClick) {
        this.mClickImageClick = mClickImageClick;
    }

    public interface OnClickImageClick{
        public void click();
    }

    public InnerPagerAdapter(Context context) {
        this.mContext = context;
        initViews();
    }

    private void initViews() {

//        for(int i = 0 ; i < 10 ; i++){
//            imageViews.add(new ImageView(mContext));
//        }
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
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        JLog.info(TAG, dataList.get(position).getImgUrl());
        ImageLoaderUtil.getInstance().loadImage(dataList.get(position).getImgUrl(),imageView);
        if(imageView.getParent() != null){
            container.removeView(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mClickImageClick){
                    mClickImageClick.click();
                }
            }
        });

        container.addView(imageView);

        TextView text = new TextView(container.getContext());
        text.setText(""+dataList.get(position).getPictureGirlsId());
        container.addView(text);


        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        JLog.info(TAG, dataList.toString());
        container.removeView((View) object);
    }

    public void setDataList(List<PictureInfo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

}
