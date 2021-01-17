package com.github.lyrric.conf;

import com.github.lyrric.model.Member;

import java.util.List;

/**
 * Created on 2020-07-23.
 *
 * @author wangxiaodong
 */
public class Config {

    /**
     * 微信配置
     */
    public static String cookie = "";

    public static String reqHeader = "GET https://miaomiao.scmttec.com/seckill/seckill/list.do?offset=0&limit=10&regionCode=5101 HTTP/1.1\n" +
            "Host: cloud.cn2030.com\n" +
            "Connection: keep-alive\n" +
            "Accept: application/json, text/plain, */*\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat\n" +
            "X-Requested-With: XMLHttpRequest\n" +
            "content-type: application/json\n" +
            "Referer: https://servicewechat.com/wx2c7f0f3c30d99445/62/page-frame.html" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "\n";

    /**
     * 接种成员ID
     */
    public static Integer memberId;
    /**
     * 接种成员身份证号码
     */
    public static String idCard;
    /**
     * 接种成员姓名
     */
    public static String memberName;
    /**
     * 选择的地区代码
     */
    public static String regionCode = "5101";

}
