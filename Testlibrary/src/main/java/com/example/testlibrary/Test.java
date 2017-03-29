package com.example.testlibrary;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by 91299 on 2017/3/29   0029.
 */

public class Test {

    public static void main(String[] args){

        System.out.println("test");

        ResultData<Bean> resultData = new ResultData<Bean>();

        ResultData.Result<Bean> result = new ResultData.Result<>();
        result.setCode(0);
        result.setMessage("success");

        Bean.QQBind bind = new Bean.QQBind();
        bind.setCode("QQ");

        Bean.WXBind wxBind = new Bean.WXBind();
        wxBind.setCode("WX");

        Bean bean = new Bean();
        bean.setQqBind(bind);
        bean.setWxBind(wxBind);

        result.setData(bean);

        resultData.setResult(result);

        Gson gson =new Gson();

        String json = gson.toJson(resultData);

        System.out.println(json);

        ResultData<Bean> test = gson.fromJson(json,new TypeToken<ResultData<Bean>>(){}.getType());

        System.out.println(gson.toJson(test));

    }
}
