package com.xuzhiyong.comego.module.Bmob;

import com.duowan.fw.ModuleData;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JEndLessList;
import com.xuzhiyong.comego.bean.PictureInfo;

/**
 * Created by 91299 on 2017/4/2   0002.
 */

public class BmobModuleData extends ModuleData {

    public static final String Kvo_NewestList = "newestList";
    @KvoAnnotation(name=Kvo_NewestList)
    public JEndLessList<PictureInfo> newestList = new JEndLessList<>(this, Kvo_NewestList);


    public static final String Kvo_girlsList = "girlsList";
    @KvoAnnotation(name=Kvo_girlsList)
    public JEndLessList<PictureInfo> girlsList = new JEndLessList<>(this, Kvo_girlsList);

}
