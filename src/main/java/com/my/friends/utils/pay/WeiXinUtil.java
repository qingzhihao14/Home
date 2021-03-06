package com.my.friends.utils.pay;

import com.alibaba.fastjson.JSON;
import com.my.friends.controller.PersonController;
import com.my.friends.service.CommandService;
import com.mysql.jdbc.StringUtils;
import io.netty.channel.ConnectTimeoutException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeiXinUtil {


    private static final Log log= LogFactory.getLog(PersonController.class);

    public final static String session_key_openid = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=APPSECRET&js_code=CODE&grant_type=authorization_code";

    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public final static String oauth2_1_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public final static String oauth2_2_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public final static String get_userInfo_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    public final static String get_hangye_url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";

    //??????????????????????????????????????????appid???appsecret????????????
    public final static String appid = "wx44d840fe4bbc9ef7";
    public final static String appSecret = "81573a408a091d9781d52617f9e31658";

    public final static String access_token_get_phone = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN&code=CODE";

    /*
    * Q?????????????????????Openid???Sessionkey
    * */
    public static JSONObject getSessionkeyAndOpenid(String code) {
        String requestUrl = session_key_openid
                .replace("APPID",appid)
                .replace("APPSECRET",appSecret)
                .replace("CODE",code);
        //??????json??????
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

        //????????????json???????????????????????????accesstoken???
        log.info("??????appid+appSecret+code??????session_key+openid =>"+jsonObject.toString());

        System.out.println("==" + jsonObject.toString());
        return jsonObject;
    }

    public static AccessToken getAccessToken(String appid, String appSecret) {
        //????????????appid???appsecret
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appSecret);
        log.info("requestUrl"+requestUrl);
        AccessToken accesstoken = new AccessToken();
        //??????json??????
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        //????????????json???????????????????????????accesstoken???
        System.out.println("=getAccessToken=" + jsonObject.toString());
        accesstoken.setToken(jsonObject.getString("access_token"));
        accesstoken.setExpiresIn(jsonObject.getInt("expires_in"));
        log.info("=getAccessToken="+jsonObject.toString());
        log.info("=getAccessToken access_token="+jsonObject.getString("access_token"));
        log.info("=getAccessToken expires_in="+jsonObject.getInt("expires_in"));
        return accesstoken;
    }

    public static String getPhone(String code) {
        AccessToken accessToken = getAccessToken(appid, appSecret);
//        //????????????appid???appsecret
//        String requestUrl = access_token_get_phone.replace("ACCESS_TOKEN", accessToken.getToken()).replace("CODE", code);
//        log.info("requestUrl"+requestUrl);
//        //??????json??????
//        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "POST", null);
//
//        //????????????json???????????????????????????accesstoken???
//        System.out.println("==" + jsonObject.toString());
//        log.info("=getPhone="+jsonObject.toString());
//        log.info("=getPhone errcode="+jsonObject.getString("errcode"));
//        if("0".equals(jsonObject.getString("errcode"))){
//            JSONObject phone_info = jsonObject.getJSONObject("phone_info");
//            String phoneNumber = phone_info.getString("phoneNumber");
//            log.info("=getPhone phone_info="+jsonObject.getInt("phone_info"));
//            log.info("=getPhone phoneNumber="+phoneNumber);
//            log.info("getPhone()??????????????????????????????phone="+phoneNumber);
//            return phoneNumber;
//        }else {
//            return "";
//        }
            // ??????accessToken
//            AppletAccessToken accessToken = ProgramInfoUtil.getAccessToken(weChatConfig.getAppletAppId(), weChatConfig.getAppletSecret());
            String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken.getToken();
            Map<String, String> map = new HashMap<>(1);
            map.put("code", code);
            String response = postJson(url, JSON.toJSONString(map), null);
//        JSONObject.fromObject(response)errcode -> {Integer@14468} 0 errmsg -> ok phone_info -> {JSONObject@14488}  size = 4 phoneNumber -> 18234775750
        JSONObject jsonObject = JSONObject.fromObject(response);
        String errcode = jsonObject.getString("errcode");
        if((!StringUtils.isNullOrEmpty(errcode) && Integer.parseInt(errcode)==0)){
            JSONObject phone_info = (JSONObject)jsonObject.get("phone_info");
            String phoneNumber = phone_info.getString("phoneNumber");
            return phoneNumber;
        }else{
            return "";
        }
    }
    /**
     * ????????? URL ??????json???????????????POST???????????????
     *
     * @param url  ??????????????? URL
     * @param json json??????????????????
     * @return ????????????????????????????????????
     */
    public static String postJson(String url, String json, Map<String, String> headMap) {
        String returnStr;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build());
            httpClient = HttpClientBuilder.create().build();
            StringEntity s = new StringEntity(json, Consts.UTF_8);
            s.setContentType("application/json");
            if (headMap != null && headMap.size() > 0) {
                Set<String> keySet = headMap.keySet();
                for (String key : keySet) {
                    post.addHeader(key, headMap.get(key));
                }
            }
            post.setEntity(s);
            httpResponse = httpClient.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),
                    StandardCharsets.UTF_8.name()));
            StringBuilder stringBuffer = new StringBuilder(100);
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            returnStr = stringBuffer.toString();
            reader.close();
            return returnStr;
        } catch (SocketTimeoutException e) {
            log.error("??????: ["+url+"]|??????: ["+json+"] ????????????");
            return "";
        } catch (ConnectTimeoutException e) {
            log.error("??????: [\"+url+\"]|??????: [\"+json+\"] ????????????");
            return "";
        } catch (Exception e) {
            log.error("??????: [\"+url+\"]|??????: [\"+json+\"] ????????????: "+e);
            return "";
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
    /**
     * ??????????????????
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {

        String requestUrl = oauth2_1_url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        //????????????????????????????????????
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, EnumMethod.GET.name(), null);
        WeixinOauth2Token wxo = new WeixinOauth2Token();
        wxo.setAccessToken(jsonObject.getString("access_token"));
        wxo.setExpiresIn(jsonObject.getInt("expires_in"));
        wxo.setRefreshToken(jsonObject.getString("refresh_token"));
        wxo.setOpenId(jsonObject.getString("openid"));
        wxo.setScope(jsonObject.getString("scope"));

        return wxo;

    }

    /**
     * ???????????????????????????
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        String requestUrl = oauth2_2_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        SNSUserInfo snsuserinfo = new SNSUserInfo();
        //????????????????????????????????????
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, EnumMethod.GET.name(), null);
        String jsonObjects = jsonObject.toString();
        log.info("jsonObjects==="+jsonObjects);
        if(jsonObject.containsKey("openid")){
            snsuserinfo.setOpenId(jsonObject.getString("openid"));
        }
        if(jsonObject.containsKey("nickname")){
            snsuserinfo.setNickname(jsonObject.getString("nickname"));
        }
        if(jsonObject.containsKey("sex")){
            snsuserinfo.setSex(jsonObject.getInt("sex"));
        }
        if(jsonObject.containsKey("country")){
            snsuserinfo.setCountry(jsonObject.getString("country"));
        }
        if(jsonObject.containsKey("province")){
            snsuserinfo.setProvince(jsonObject.getString("province"));
        }
        if(jsonObject.containsKey("city")){
            snsuserinfo.setCity(jsonObject.getString("city"));
        }
        if(jsonObject.containsKey("headimgurl")){
            snsuserinfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
        }

        //snsuserinfo.setPrivilegeList(JSONArray._fromArray(jsonObject.getString("privilege"),String.class));
        return snsuserinfo;
    }

    /**
     * ?????????????????????url
     *
     * @param redirectUri
     * @return
     */
    public static String createUrl(String redirectUri) {

        String url = get_userInfo_url.replace("APPID", appid).replace("REDIRECT_URI", CommonUtil.urlEncodeUTF8(redirectUri)).replace("SCOPE", "snsapi_userinfo");
        System.out.println(url);
        return url;
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param
     */

    public static String shortURL(String longURL, String wxAppId, String secret) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        try {
            //???????????????access_token,????????????
            requestUrl = requestUrl.replace("ACCESS_TOKEN", getAccessToken(wxAppId, secret).getToken());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("=111222333==");
        String jsonMsg = "{\"action\":\"long2short\",\"long_url\":\"%s\"}";
        //?????????url
        String.format(jsonMsg, longURL);
        JSONObject jsonobject = CommonUtil.httpsRequest(requestUrl, "POST", String.format(jsonMsg, longURL));
        //????????????????????????
        System.out.println("===" + jsonobject.toString());
        return jsonobject.getString("short_url");
    }



}
