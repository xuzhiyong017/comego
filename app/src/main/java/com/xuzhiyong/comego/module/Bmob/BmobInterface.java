package com.xuzhiyong.comego.module.Bmob;


/**
 * Created by 91299 on 2017/4/2   0002.
 */

public interface BmobInterface {

    void getFirstPagePictures(Integer index);

    void getNextPagePictures(Integer limit);


    void getFirstGirlsPages(Integer girlsId);
    void getNextGirlsPages(Integer girlsId,Integer limit);
}
