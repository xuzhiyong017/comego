package com.xuzhiyong.comego.module.Bmob;

import com.duowan.fw.Module;
import com.duowan.fw.util.JLog;
import com.mozillaonline.providers.downloads.Constants;
import com.xuzhiyong.comego.bean.PictureInfo;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by 91299 on 2017/4/2   0002.
 */

public class BmobModule extends Module implements BmobInterface{

    private BmobModuleData mData;

    public BmobModule() {
        mData = new BmobModuleData();
        DData.bmobModuleData.link(this,mData);

        Bmob.initialize(Module.gMainContext,"9efa951ea901eae0835f7dfdf13b486d");
    }


    @Override
    public void getFirstPagePictures(final Integer index) {
        String sql = "select * from PictureInfo where isCover=1 limit 0,?";
        BmobQuery<PictureInfo> query = new BmobQuery<>();
        query.clearCachedResult(PictureInfo.class);
        query.doSQLQuery(sql,new SQLQueryListener<PictureInfo>(){

            @Override
            public void done(BmobQueryResult<PictureInfo> result, BmobException e) {
                if(e ==null){
                    JLog.info("smile", "查询到："+result.getResults().size()+"符合条件的数据");
                    mData.newestList.combineResult(index,result.getResults());
                }else{
                    JLog.info("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        }, DConst.KC_PageCount);
    }

    @Override
    public void getNextPagePictures(Integer limit) {
        String sql = "select * from PictureInfo where isCover=1 limit ?,?";
        BmobQuery<PictureInfo> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(PictureInfo.class);
        if(isCache){
            query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.doSQLQuery(sql,new SQLQueryListener<PictureInfo>(){

            @Override
            public void done(BmobQueryResult<PictureInfo> result, BmobException e) {
                if(e ==null){
                    JLog.info("smile", "查询到："+result.getResults().size()+"符合条件的数据");
                    mData.newestList.combineResult(mData.newestList.curIndex(),result.getResults());
                }else{
                    JLog.info("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        },mData.newestList.curIndex(),DConst.KC_PageCount);
    }


    @Override
    public void getFirstGirlsPages(Integer girlsId) {
        String sql = "select * from PictureInfo where pictureGirlsId=? limit 0,?";
        BmobQuery<PictureInfo> query = new BmobQuery<>();
        query.clearCachedResult(PictureInfo.class);
        query.doSQLQuery(sql,new SQLQueryListener<PictureInfo>(){

            @Override
            public void done(BmobQueryResult<PictureInfo> result, BmobException e) {
                if(e ==null){
                    JLog.info("smile", "查询到："+result.getResults().size()+"符合条件的数据");
                    mData.girlsList.combineResult(0,result.getResults());
                }else{
                    JLog.info("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        },girlsId,DConst.KC_PageCount);
    }

    @Override
    public void getNextGirlsPages(Integer girlsId, Integer limit) {
        String sql = "select * from PictureInfo where pictureGirlsId=? limit ?,?";
        BmobQuery<PictureInfo> query = new BmobQuery<>();
        query.clearCachedResult(PictureInfo.class);
        query.doSQLQuery(sql,new SQLQueryListener<PictureInfo>(){

            @Override
            public void done(BmobQueryResult<PictureInfo> result, BmobException e) {
                if(e ==null){
                    JLog.info("smile", "查询到："+result.getResults().size()+"符合条件的数据");
                    mData.girlsList.combineResult(mData.newestList.curIndex(),result.getResults());
                }else{
                    JLog.info("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        },girlsId,mData.newestList.curIndex(),DConst.KC_PageCount);
    }
}
