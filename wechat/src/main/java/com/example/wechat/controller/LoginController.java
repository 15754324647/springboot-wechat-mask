package com.example.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechat.utils.AesCbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Graciano
 * @date 2020/1/22 23:04
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private static String APP_ID = "wxdbc3e0b6fedcb9ea";

    private static String APP_SECRET = "a11970e36d69098dacdcdacafca12451";


    @PostMapping("/wxLogin")
    public String wxLogin(String code) {

        log.info("wxlogin - code: " + code);
        StringBuilder builder = new StringBuilder();

//      https://api.weixin.qq.com/sns/jscode2session?
//              appid=APPID&
//              secret=SECRET&
//              js_code=JSCODE&
//              grant_type=authorization_code

        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>(4);
        param.put("appid", APP_ID);
        param.put("secret", APP_SECRET);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        builder.append(url).append("?").append("appid=").append(APP_ID).append("&secret=").append(APP_SECRET).append("&js_code=").append(code).append("&grant_type=authorization_code");
        RestTemplate restTemplate = new RestTemplate();
        final String wxResult = restTemplate.getForObject(builder.toString(), String.class);
        log.info(wxResult);
        JSONObject json = JSON.parseObject(wxResult);
        System.out.println("返回过来的json数据:"+json.toString());
        String sessionkey=json.get("session_key").toString(); //会话秘钥
        String openid=json.get("openid").toString(); //用户唯一标识
//        String decrypts= AesCbcUtil.decrypt(encryptedData,sessionkey,iv,"utf-8");//解密
        return wxResult;
    }
}
