package com.xuzhiyong.comego.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 91299 on 2017/4/2   0002.
 */

public class PictureInfo extends BmobObject {

    private Integer id;
    private Integer pictureGirlsId;
    private String imgUrl;
    private Integer isCover;
    private Integer category;

    public PictureInfo() {
        this.setTableName("PictureInfo");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPictureGirlsId() {
        return pictureGirlsId;
    }

    public void setPictureGirlsId(Integer pictureGirlsId) {
        this.pictureGirlsId = pictureGirlsId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getIsCover() {
        return isCover;
    }

    public void setIsCover(Integer isCover) {
        this.isCover = isCover;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
