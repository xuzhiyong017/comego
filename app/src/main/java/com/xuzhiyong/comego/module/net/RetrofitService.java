package com.xuzhiyong.comego.module.net;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 91299 on 2017/3/26   0026.
 */

public interface RetrofitService {

    @POST("/send")
    Observable<Proto> exchangeProto(@Body String json);
}
