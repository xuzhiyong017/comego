package com.xuzhiyong.comego.ui.main.picdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.KvoBinder;
import com.duowan.fw.util.JEndLessList;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.bean.PictureInfo;
import com.xuzhiyong.comego.module.Bmob.BmobInterface;
import com.xuzhiyong.comego.module.Bmob.BmobModuleData;
import com.xuzhiyong.comego.module.Bmob.CountResponseListener;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.ui.utils.UIHelper;

import java.util.List;

/**
 * Created by 91299 on 2017/4/3   0003.
 */

public class ImageDetailDialog extends Dialog {

    private ViewPager mViewPager;
    private TextView mCurrent;
    private TextView mTotals;
    private InnerPagerAdapter mAdapter;
    private KvoBinder mBinder = new KvoBinder(this);
    private int girlsId;
    private Integer curPosition = 1;
    private Integer mTotalNum = 0;

    public ImageDetailDialog(Context context,Integer girlsId) {
        super(context,R.style.gc_style_fulldialog);
        this.girlsId = girlsId;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_image_detail);
        mViewPager = UIHelper.getView(this,R.id.image_detail_viewpager);
        mCurrent = UIHelper.getView(this,R.id.image_current_position);
        mTotals = UIHelper.getView(this,R.id.image_totals);
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

        initViewPagerChange();
    }

    private void initViewPagerChange() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrent.setText((position+1)+"");
                if(position == mAdapter.getCount()-1 && mAdapter.getCount() < mTotalNum){
                    DModule.ModuleBmob.cast(BmobInterface.class).getNextGirlsPages(girlsId, DConst.KC_PageCount);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void bindData() {
        mBinder.singleBindSourceToClassObj(DData.bmobModuleData.data());
        DModule.ModuleBmob.cast(BmobInterface.class).getFirstGirlsPages(girlsId);
        DModule.ModuleBmob.cast(BmobInterface.class).getGirlsCount(girlsId, new CountResponseListener() {
            @Override
            public void onResponse(Integer count) {
                mTotalNum = count;
                mCurrent.setText(curPosition+"");
                mTotals.setText("/"+count);
            }
        });
    }


    @KvoAnnotation(targetClass = BmobModuleData.class, name = BmobModuleData.Kvo_girlsList, thread = ThreadBus.Main)
    public void onList(Kvo.KvoEvent event) {
        if (null != event.newValue) {
            List<PictureInfo> list = (JEndLessList<PictureInfo>) (event.newValue);
            mAdapter.setDataList(list);
        }
    }


    void release(){
        DModule.ModuleBmob.cast(BmobInterface.class).clearGirlsData();
        if(null != mBinder){
            mBinder.clearAllKvoConnections();
            mBinder = null;
        }
    }


}
