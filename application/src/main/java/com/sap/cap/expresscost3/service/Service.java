package com.sap.cap.expresscost3.service;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
public class Service {

    public static String httpPostWithForm(String url, Map<String, String> params, Map<String, String> headers) {
        // 用于接收返回的结果
        String resultData = "";
        try {
            HttpPost post = new HttpPost(url);
            //设置头部信息
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            List<BasicNameValuePair> pairList = new ArrayList<>();
            for (String key : params.keySet()) {
                pairList.add(new BasicNameValuePair(key, params.get(key)));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "utf-8");
            post.setEntity(uefe);
            // 创建一个http客户端
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            // 发送post请求
            HttpResponse response = httpClient.execute(post);
            resultData = EntityUtils.toString(response.getEntity(), "UTF-8");// 返回正常数据
        } catch (Exception e) {
            System.out.println("接口连接失败 e:" + e);
        }
        return resultData;
    }





    public static void wordSort(ArrayList<String> words) {
        for (int i = words.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (words.get(j).compareToIgnoreCase(words.get(j + 1)) > 0) {
                    String temp = words.get(j);
                    words.set(j, words.get(j + 1));
                    words.set(j + 1, temp);
                }
            }
        }
    }

    public static String formatUrlParam(Map<String, String> paraMap, String encode, boolean isLower) {
        String params = "";
        Map<String, String> map = paraMap;
        try {
            List<Map.Entry<String, String>> itmes = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    // TODO Auto-generated method stub
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });
            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    val = URLEncoder.encode(val, encode);
                    if (isLower) {
                        sb.append(key.toLowerCase() + "=" + val);
                    } else {
                        sb.append(key + "=" + val);
                    }
                    sb.append("&");
                }
            }
            params = sb.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return params;
    }

    public static String getAloneKeys(JSONObject json) {
        ArrayList<String> aloneKeys = new ArrayList<>();
        for (String key : json.keySet()) {
            aloneKeys.add(key);
        }
        // 排序
        wordSort(aloneKeys);
        // 整理排序后的json
        JSONObject newJson = new JSONObject(new LinkedHashMap<>());
        for (String key : aloneKeys) {
            newJson.put(key, json.get(key));
        }
        return newJson.toJSONString();
    }

    public static String getSign(String appSecret, Map<String, String> valueMap) {
        String soreValueMap = formatUrlParam(valueMap, "utf-8", true);//对参数按key进行字典升序排列
        String signValue = appSecret + soreValueMap;//将key拼接在请求参数的前面
        String md5SignValue = MD5Utils.MD5Encode(signValue, "utf8");//形成MD5加密后的签名
        return md5SignValue;
    }



}
