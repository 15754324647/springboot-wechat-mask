package com.example.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author limeiqi
 * @date 2020/1/19
 **/
@Controller
@RequestMapping("/wechat")
public class OAuthController {

    @Value("${wechat.oauthUrl}")
    private String oauthUrl;

    @Value("${wechat.tokenUrl}")
    private String tokenUrl;

    @Value("${wechat.infoUrl}")
    private String infoUrl;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.appsecret}")
    private String appsecret;

    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    @ResponseBody
    public void getCode(HttpServletResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(oauthUrl).append("?").append("appid=").append(appid).append("&redirect_uri=").append(URLEncoder.encode("http://lmq.hn3.mofasuidao.cn/wechat/getAccessToken")).append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        System.out.println(builder.toString());
        response.sendRedirect(builder.toString());
    }

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
    @ResponseBody
    public void getAccessToken(HttpServletRequest request) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        String code = request.getParameter("code");
        System.out.println(code);
        request.getRequestURL().toString();
        StringBuilder builder = new StringBuilder();
        builder.append(tokenUrl).append("?").append("appid=").append(appid).append("&secret=").append(appsecret).append("&code=").append(code).append("&grant_type=authorization_code");

        String s = restTemplate.postForObject(builder.toString(), null, String.class);
        System.out.println(s);

        JSONObject jsonObject = JSON.parseObject(s);

        String accessToken = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        StringBuilder builder1 = new StringBuilder();
        builder1.append(infoUrl).append("?").append("access_token=").append(accessToken).append("&openid=").append(openid).append("&lang=zh_CN");

        String info = restTemplate.postForObject(builder1.toString(), null, String.class);
        String s1 = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println(s1);
    }

}
