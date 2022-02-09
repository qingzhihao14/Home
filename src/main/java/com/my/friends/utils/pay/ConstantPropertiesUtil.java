package com.my.friends.utils.pay;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Atuhor: qin
 * @Create: 2022-02-09-23-05
 * @Time: 23:05
 * @Description:
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    private String appId="wx808774b6ec37e6d4";

    private String appSecret="2606d07fb31a562f43bbfc080e7c57ba";

    private String redirectUrl="http://4d2f-2409-8900-2b93-6321-988f-f1c4-f9fe-8d52.ngrok.io/login/12345/WeiXinTest/api/ucenter/wx/callback";

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
    }
}
