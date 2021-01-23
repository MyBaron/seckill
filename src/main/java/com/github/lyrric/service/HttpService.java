package com.github.lyrric.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lyrric.conf.Config;
import com.github.lyrric.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
@Slf4j
public class HttpService {

    private String baseUrl = "https://cloud.cn2030.com";

    private final Logger logger = LogManager.getLogger(HttpService.class);


    /***
     * 获取秒杀资格
     * @param seckillId 疫苗ID
     * @param vaccineIndex 固定1
     * @param linkmanId 接种人ID
     * @param idCard 接种人身份证号码
     * @return 返回订单ID
     * @throws IOException
     * @throws BusinessException
     */
    public String secKill(String seckillId, String vaccineIndex, String linkmanId, String idCard) throws IOException, BusinessException {
        String path = baseUrl + "/seckill/seckill/subscribe.do";
        Map<String, String> params = new HashMap<>();
        params.put("seckillId", seckillId);
        params.put("vaccineIndex", vaccineIndex);
        params.put("linkmanId", linkmanId);
        params.put("idCardNo", idCard);
        //后面替换成接口返回的st
        //目前发现接口返回的st就是当前时间，后面可能会固定为一个加密参数
        long st = System.currentTimeMillis();
        Header header = new BasicHeader("ecc-hs", eccHs(seckillId, st));
//        return get(path, params, header);
        return null;
    }

    /**
     * 获取疫苗列表
     *
     * @return
     * @throws BusinessException
     */
    public  JSONArray getVaccines(String id,String lat,String lng) throws BusinessException, IOException {
        hasAvailableConfig();
        String path = baseUrl + "/sc/wx/HandlerSubscribe.ashx";
        Map<String, String> param = new HashMap<>();
        param.put("act", "CustomerProduct");
        param.put("id", id);
        param.put("lat", lat);
        param.put("lng", "lng");
        String json = get(path, param, null).getString("list");
        return JSONObject.parseArray(json);
    }

    /**
     * 获取所有诊所列表
     * @return
     */
    public JSONArray getClinics() throws IOException {
        hasAvailableConfig();
        String path = baseUrl + "/sc/wx/HandlerSubscribe.ashx";
        Map<String, String> param = new HashMap<>();
        param.put("act", "CustomerList");
        param.put("city", "%5B%22%E5%B9%BF%E4%B8%9C%E7%9C%81%22%2C%22%E8%82%87%E5%BA%86%E5%B8%82%22%2C%22%22%5D");
        param.put("lat", "23.18139");
        param.put("lng", "113.48067");
        param.put("id", "0");
        param.put("cityCode", "441200");
        param.put("product", "1");
        String json = get(path, param, null).getString("list");
        log.info("[获取诊所列表]返回信息:{}",json);
        return JSONObject.parseArray(json);
    }


    /**
     * 获取接种人信息
     *
     * @return
     */
    public Member getMembers() throws IOException, BusinessException {
        //https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=User
        String path = baseUrl + "/sc/wx/HandlerSubscribe.ashx?act=User";
        String json = get(path, null, null).getString("user");
        log.info("[获取接种人信息]返回信息：{}", json);
        return JSONObject.parseObject(json, Member.class);
    }

    /***
     * 获取加密参数st
     * @param vaccineId 疫苗ID
     */
    public String getSt(String vaccineId) throws IOException {
        String path = baseUrl + "/seckill/seckill/checkstock2.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
//        String json = get(path, params, null);
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        return jsonObject.getJSONObject("data").getString("st");
        return null;
    }

    private void hasAvailableConfig() throws BusinessException {
        if (StringUtils.isEmpty(Config.cookie)) {
            throw new BusinessException("0", "请先配置cookie");
        }
    }

    private JSONObject get(String path, Map<String, String> params, Header extHeader) throws IOException, BusinessException {
        if (params != null && params.size() != 0) {
            StringBuilder paramStr = new StringBuilder("?");
            params.forEach((key, value) -> {
                paramStr.append(key).append("=").append(value).append("&");
            });
            String t = paramStr.toString();
            if (t.endsWith("&")) {
                t = t.substring(0, t.length() - 1);
            }
            path += t;
        }
        HttpGet get = new HttpGet(path);
        List<Header> headers = getCommonHeader();
        if (extHeader != null) {
            headers.add(extHeader);
        }
        get.setHeaders(headers.toArray(new Header[0]));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntity httpEntity = httpClient.execute(get).getEntity();
        String json = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSONObject.parseObject(json);
        log.info("[http 请求]返回信息：{}", json);
        if (200 == (Integer) jsonObject.get("status")) {
            // TODO 还没改成统一
            return jsonObject;
        } else {
            throw new BusinessException(jsonObject.getString("code"), jsonObject.getString("msg"));
        }
    }

    private List<Header> getCommonHeader() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; SM-N960F Build/JLS36C; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36 MMWEBID/1042 MicroMessenger/7.0.15.1680(0x27000F34) Process/appbrand0 WeChat/arm32 NetType/WIFI Language/zh_CN ABI/arm32"));
        headers.add(new BasicHeader("Referer", "https://servicewechat.com/wx2c7f0f3c30d99445/62/page-frame.html"));
//        headers.add(new BasicHeader("tk", Config.tk));
        headers.add(new BasicHeader("Accept", "application/json, text/plain, */*"));
        headers.add(new BasicHeader("Host", "cloud.cn2030.com"));
        headers.add(new BasicHeader("cookie", "ASP.NET_SessionId=" + Config.cookie));
        return headers;
    }

    private String eccHs(String seckillId, Long st) {
        String salt = "ux$ad70*b";
        final Integer memberId = Config.memberId;
        String md5 = DigestUtils.md5Hex(seckillId + st + memberId);
        return DigestUtils.md5Hex(md5 + salt);
    }


    /***
     * 获取接种日期
     * @param vaccineId 疫苗ID
     * @param orderId 订单ID
     */
    public List<SubDate> getSkSubDays(String vaccineId, String orderId) throws IOException, BusinessException {
//        String path = baseUrl + "/seckill/seckill/subscribeDays.do";
//        Map<String, String> params = new HashMap<>();
//        params.put("id", vaccineId);
//        params.put("sid", orderId);
//        String json = get(path, params, null);
//        logger.info("日期格式:{}", json);
//        return JSONObject.parseArray(json).toJavaList(SubDate.class);
        return null;
    }

    /**
     * 根据接种日期，获取接种时间段
     *
     * @param vaccineId
     * @param orderId
     * @param day       接种日期 YYYY-MM-DD
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    public List<SubDateTime> getSkSubDayTime(String vaccineId, String orderId, String day) throws IOException, BusinessException {
//        String path = baseUrl + "/seckill/seckill/dayTimes.do";
//        Map<String, String> params = new HashMap<>();
//        params.put("id", vaccineId);
//        params.put("sid", orderId);
//        params.put("day", day);
//        String json = get(path, params, null);
//        System.out.println("根据选择的日期，获取的时间格式" + json);
//        return JSONObject.parseArray(json).toJavaList(SubDateTime.class);
        return null;
    }

    /**
     * 提交接种时间
     *
     * @param vaccineId
     * @param orderId
     * @param day       接种日期 YYYY-MM-DD
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    public void subDayTime(String vaccineId, String orderId, String day, String wid) throws IOException, BusinessException {
//        String path = baseUrl + "/seckill/seckill/submitDateTime.do";
//        Map<String, String> params = new HashMap<>();
//        params.put("id", vaccineId);
//        params.put("sid", orderId);
//        params.put("day", day);
//        params.put("wid", wid);
//        String json = get(path, params, null);
//        logger.info("提交接种时间，返回数据: {}", json);
        return;
    }
}
