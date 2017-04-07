package com.xuzhiyong.comego.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.KvoBinder;
import com.duowan.fw.util.JEndLessList;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.bean.PictureInfo;
import com.xuzhiyong.comego.module.Bmob.BmobInterface;
import com.xuzhiyong.comego.module.Bmob.BmobModuleData;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.DSetting;
import com.xuzhiyong.comego.ui.base.BaseActivity;
import com.xuzhiyong.comego.ui.main.picdetail.ImageDetailDialog;

public class MainActivity extends BaseActivity {

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private LRecyclerView mRecyclerView;
    private DataAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ImageDetailDialog mImageDetailDialog;
    private KvoBinder mBinder = new KvoBinder(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        bindData();
    }

    private void initView() {

        mRecyclerView = getView(R.id.list);
        mDataAdapter = new DataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        //setLayoutManager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentCounter = 0;
                mDataAdapter.clear();
                requestData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    requestData();
                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
            }
        });

        mRecyclerView.refresh();

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mImageDetailDialog = new ImageDetailDialog(MainActivity.this,mDataAdapter.getDataList().get(position).getPictureGirlsId());
                mImageDetailDialog.show();
                int index = DSetting.getSettingValue(DSetting.Kvo_home_page_list_index,0);
                if(index > position) return;
                DSetting.setSettingValue(DSetting.Kvo_home_page_list_index,position);
            }
        });
    }

    private void bindData() { 
        mBinder.singleBindSourceToClassObj(DData.bmobModuleData.data());

        int index = DSetting.getSettingValue(DSetting.Kvo_home_page_list_index,0);

        DModule.ModuleBmob.cast(BmobInterface.class).getFirstPagePictures(index);
    }

    private void requestData() {
        DModule.ModuleBmob.cast(BmobInterface.class).getNextPagePictures(DConst.KC_PageCount);
    }


    @KvoAnnotation(targetClass = BmobModuleData.class, name = BmobModuleData.Kvo_NewestList, thread = ThreadBus.Main)
    public void onList(Kvo.KvoEvent event) {
        if (null != event.newValue) {
            mDataAdapter.setDataList((JEndLessList<PictureInfo>) (event.newValue));
            mRecyclerView.refreshComplete(REQUEST_COUNT);
            if (mDataAdapter != null && mDataAdapter.getItemCount() == 0) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mBinder){
            mBinder.clearAllKvoConnections();
            mBinder = null;
        }

        if(null != mImageDetailDialog){
            mImageDetailDialog.cancel();
            mImageDetailDialog = null;
        }
    }
}
