package com.xuzhiyong.comego.model;

import com.duowan.fw.KeepAnnotation;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JLog;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@KeepAnnotation
public class ComegoConfig {

    @KeepAnnotation
    public static class ConfigAccountType{
        @KeepAnnotation
        public int accounttype;

        @KeepAnnotation
        public String name;

        @KeepAnnotation
        public int state;
    }

    @KeepAnnotation
    public static class ConfigLogin{
        @KeepAnnotation
        public ConfigAccountType[] accounttypes;

        // default config login
        public ConfigLogin() {
            accounttypes = new ConfigAccountType[2];
            accounttypes[0] = new ConfigAccountType();
            accounttypes[0].accounttype = 1;
            accounttypes[0].name = "YY";
            accounttypes[0].state = 1;

            accounttypes[1] = new ConfigAccountType();
            accounttypes[1].accounttype = 3;
            accounttypes[1].name = "QQ";
            accounttypes[1].state = 1;
        }
    }

    @KeepAnnotation
    public static class ConfigSplashAct {
        // http://qiniu-image.com/name.[width*height].png

        // http://qiniu-image.com/zhongqiu.1080x720.png
        // http://qiniu-image.com/zhongqiu.1920x1080.png
        @KeepAnnotation
        public String imgs[];

        @KeepAnnotation
        public String marketlogo[];

        @KeepAnnotation
        public String jump;

        @KeepAnnotation
        public long secs;

        @KeepAnnotation
        public ConfigDate date;
    }


    @KeepAnnotation
    public static class ConfigBubble{
        // http://qiniu-image.com/name.[width*height].png

        // http://qiniu-image.com/zhongqiu.1080x720.png
        // http://qiniu-image.com/zhongqiu.1920x1080.png
        @KeepAnnotation
        public String imgs_my[];

        @KeepAnnotation
        public String imgs_other[];

        @KeepAnnotation
        public long version;

        @KeepAnnotation
        public ConfigDate date;
    }

    @KeepAnnotation
    public static class ConfigDate{
        // 生效日期
        @KeepAnnotation
        public String validdate; // YYYY-MM-DD HH:MM:SS

        // 过期日期
        @KeepAnnotation
        public String invaliddate; // YYYY-MM-DD HH:MM:SS
    }

    @KeepAnnotation
    public static class ImageAct{
        // http://qiniu-image.com/name.[width*height].png

        // http://qiniu-image.com/zhongqiu.1080x720.png
        // http://qiniu-image.com/zhongqiu.1920x1080.png
        @KeepAnnotation
        public String imgs[];

        @KeepAnnotation
        public String jump;
    }

    @KeepAnnotation
    public static class ConfigRegAct {
        @KeepAnnotation
        public ImageAct imgs[];

        @KeepAnnotation
        public ConfigDate date;
    }

    @KeepAnnotation
    public static class ConfigSysAct {
        @KeepAnnotation
        public int referee_award;

        @KeepAnnotation
        public int referer_award;
    }

    @KeepAnnotation
    public static class AppConfig{
        // 控制登陆选项：QQ登陆，YY登陆
        @KeepAnnotation
        public ConfigLogin login;

        // 控制云存储是否使用qiniu
        @KeepAnnotation
        public boolean qiniu;
        // 是否打开hiido 统计
        @KeepAnnotation
        public boolean hiido;

        //是否打开二维码
        @KeepAnnotation
        public boolean qrcode;

        // 是否打开日志
        @KeepAnnotation
        public boolean default_log;
        // 控制友盟上报
        @KeepAnnotation
        public boolean ym;
        // 是否打开友盟的崩溃上报
        @KeepAnnotation
        public boolean ym_crash;

        // 控制闪屏的活动信息
        @KeepAnnotation
        public ConfigSplashAct splash;

        // 控制注册页面的动态信息
        @KeepAnnotation
        public ConfigRegAct reg;

        // 推荐人奖励金额
        @KeepAnnotation
        public ConfigSysAct sys;

        //点赞动画图片
        @KeepAnnotation
        public ConfigBubble bubble;

        // 自动填充空洞
        @KeepAnnotation
        public boolean autofillhole;

        //代充优惠气泡
        @KeepAnnotation
        public String discount_info;

        // default config app
        public AppConfig(){
            reset();
        }

        // init the config
        public void init(){
            String json = JConfig.getString("app.config.json", "");
            if (json.length() > 0) {
                serialfrom(json);
            }
        }

        public void reset(){
            login = new ConfigLogin();
            qiniu = true;
            hiido = true;
            default_log = true;
            qrcode = false;
            ym = false;
            ym_crash = false;
            splash = null;
            reg = null;
            autofillhole = true;
        }

        public void serialfrom(String json){
            JLog.debug(this, "AdaConfig serialFrom json:" + json);
            try {
                AppConfig appconfig = new Gson().fromJson(json, AppConfig.class);
                config.qiniu = appconfig.qiniu;
                config.hiido = appconfig.hiido;
                config.default_log = appconfig.default_log;
                config.ym = appconfig.ym;
                config.ym_crash = appconfig.ym_crash;
                config.login = appconfig.login;
                config.splash = appconfig.splash;
                config.bubble = appconfig.bubble;
                config.reg = appconfig.reg;
                config.sys = appconfig.sys;
                config.qrcode = appconfig.qrcode;
                config.autofillhole = appconfig.autofillhole;
                config.discount_info = appconfig.discount_info;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void copyFrom(AppConfig src, AppConfig dst) {
            dst.qiniu = src.qiniu;
            dst.hiido = src.hiido;
            dst.default_log = src.default_log;
            dst.ym = src.ym;
            dst.ym_crash = src.ym_crash;
            dst.login = src.login;
            dst.splash = src.splash;
            dst.bubble = src.bubble;
            dst.reg = src.reg;
            dst.sys = src.sys;
            dst.qrcode = src.qrcode;
            dst.autofillhole = src.autofillhole;
            dst.discount_info = src.discount_info;
        }
    }

    // default config
    public static AppConfig config;
    static{
        config = new AppConfig();
        config.init();
    }

    // fill the config form http json
    public static void fillConfig(String json){
        config.serialfrom(json);
        JConfig.putString("app.config.json", json);
    }

	public static void syncConfigFromServer() {
        doSyncConfigFromServer();
	}

	private static void doSyncConfigFromServer() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URLHelper.getUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        GetConfig config = retrofit.create(GetConfig.class);
        String query = "{\"clientVersion\":" + DConst.KC_ClientVersion + "}";
        config.getConfig(query)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            fillConfig(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            JLog.error("AdaConfig", "get AppConfig failed:" + throwable.getMessage());
                        }
                });
	}

    interface GetConfig {
        @GET(URLHelper.CONFIG)
        Observable<ResponseBody> getConfig(@Query("json") String json);
    }
}

