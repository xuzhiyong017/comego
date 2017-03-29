package com.example.testlibrary;


/**
 * Created by 91299 on 2017/3/29   0029.
 */

public class Bean{


    private QQBind qqBind;

    private WXBind wxBind;

    public QQBind getQqBind() {
        return qqBind;
    }

    public void setQqBind(QQBind qqBind) {
        this.qqBind = qqBind;
    }

    public WXBind getWxBind() {
        return wxBind;
    }

    public void setWxBind(WXBind wxBind) {
        this.wxBind = wxBind;
    }


    public static class QQBind{
        private String code;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class WXBind{
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
