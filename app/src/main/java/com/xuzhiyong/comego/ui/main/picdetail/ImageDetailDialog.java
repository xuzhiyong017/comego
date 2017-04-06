package com.xuzhiyong.comego.ui.main.picdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.KvoBinder;
import com.duowan.fw.util.JEndLessList;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.bean.PictureInfo;
import com.xuzhiyong.comego.module.Bmob.BmobInterface;
import com.xuzhiyong.comego.module.Bmob.BmobModuleData;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.ui.utils.UIHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;
import java.util.List;

/**
 * Created by 91299 on 2017/4/3   0003.
 */

public class ImageDetailDialog extends Dialog {

    private ViewPager mViewPager;
    private MagicIndicator mIndicator;
    private InnerPagerAdapter mAdapter;

    private KvoBinder mBinder = new KvoBinder(this);


    private int size = 0;
    private int girlsId;

    public ImageDetailDialog(Context context,Integer girlsId) {
        super(context,R.style.gc_style_fulldialog);
        this.girlsId = girlsId;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_image_detail);
        mViewPager = UIHelper.getView(this,R.id.image_detail_viewpager);
        mIndicator = UIHelper.getView(this,R.id.magic_indicator);
        mAdapter = new InnerPagerAdapter(getContext());
        mViewPager.setAdapter(mAdapter);

        bindData();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               release();
            }
        });

        mAdapter.setClickImageClick(new InnerPagerAdapter.OnClickImageClick() {
            @Override
            public void click() {
                dismiss();
            }
        });
        mAdapter.setDataList(null);
    }

    private void initIndicator(int size) {
        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setFollowTouch(false);
        circleNavigator.setCircleCount(size);
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        mIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

    private void bindData() {
        mBinder.singleBindSourceToClassObj(DData.bmobModuleData.data());
        DModule.ModuleBmob.cast(BmobInterface.class).getFirstGirlsPages(girlsId);
    }


    @KvoAnnotation(targetClass = BmobModuleData.class, name = BmobModuleData.Kvo_girlsList, thread = ThreadBus.Main)
    public void onList(Kvo.KvoEvent event) {
        if (null != event.newValue) {
            List<PictureInfo> list = (JEndLessList<PictureInfo>) (event.newValue);
            size = list.size();
            initIndicator(size);
            mAdapter.setDataList(list);
        }
    }


    void release(){
        mAdapter.setDataList(null);
        mAdapter = null;
        if(null != mBinder){
            mBinder.clearAllKvoConnections();
            mBinder = null;
        }
    }


}
