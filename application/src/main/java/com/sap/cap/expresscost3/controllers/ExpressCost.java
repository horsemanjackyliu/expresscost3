package com.sap.cap.expresscost3.controllers;

import com.google.gson.reflect.TypeToken;
import com.sap.cap.expresscost3.service.Service;
import  com.sap.cap.expresscost3.service.Service.*;
import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/express")

public class ExpressCost {

    private String appSecret  = "9a8cb8d5dbfe4665bb17faf5edc752df";
    private String cainiaoUrl = "https://express.xuanquetech.com/express/v1/" ;

    private Service serviceUtil = new Service();


    @RequestMapping(path = "/trace",method = RequestMethod.GET)

    public ResponseEntity<String> expressTrace(@RequestBody JSONObject body){
        System.out.println(body.toString());
//        Service serviceUtil = new Service();
//        String logisticsInterfaceStr = "{\"queCallBackParam\":\"test\",\"cpCode\":\"YUNDA\",\"mailNo\":\"463129106326570\"}";
//        JSONObject json = JSON.parseObject(logisticsInterfaceStr);
//        String sortStr = serviceUtil.getAloneKeys(json);
        String sortStr = serviceUtil.getAloneKeys(body);
        System.out.println("TraceSortStr:"+sortStr);
        Map<String, String> map = new HashMap<String, String>();
        map.put("logistics_interface", sortStr);
        map.put("nonce", "1686210870");
        String sign = serviceUtil.getSign(appSecret, map);
        System.out.println("sign:" + sign);

        Map<String, String> headers = new HashMap<>();
        headers.put("appid", "66d0d24912634800a1c3cd5588b01543");//替换你的app
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //组装参数

        Map<String, String> params = new HashMap();
        params.put("nonce", "1686210870");//时间戳（秒）
        params.put("sign", sign);//签名
        params.put("logistics_interface", body.toString());//订阅接口入参字段


        String url = cainiaoUrl+ "queryExpressRoutes" ;
        System.setProperty("console.encoding","UTF-8");
//        System.out.println("url:" + url);
        String resultData = serviceUtil.httpPostWithForm(url, params, headers);
//        System.out.println("result:" + resultData);
        return ResponseEntity.ok(resultData);
    }

    @RequestMapping(path = "/order",method = RequestMethod.POST)
    public ResponseEntity<JSONObject> order(@RequestBody JSONObject body){



        String url = cainiaoUrl + "orderService";



//        String sortedSenderStr =  serviceUtil.getAloneKeys(JSON.parseObject(body.get("sender").toString()));
//        String sortedReceiverStr =  serviceUtil.getAloneKeys(JSON.parseObject(body.get("receiver").toString()));
//
//        System.out.println("sortedSenderStr:"+sortedSenderStr);
//        System.out.println("sortedReceiverStr:"+sortedReceiverStr);
//
//        body.remove("sender");
//        System.out.println("bodyafterRemoveSender:"+body.toString());
//        body.put("nonce", "1686210870");
////        body.put("sender",JSON.parseObject(sortedSenderStr ));
//        body.put("sender",sortedSenderStr );
////        System.out.println("bodyafterAddSender:"+body.toString());
//        body.remove("receiver");
//        body.put("receiver",sortedReceiverStr );
        String sortStr = serviceUtil.getAloneKeys(body);


//        System.out.println("sender:"+body.get("sender").toString());
//        System.out.println("receiver:"+body.get("receiver").toString());
        System.out.println("OrderSortStr:"+sortStr);
        Map<String, String> map = new HashMap<String, String>();


//        map.put("logistics_interface", sortStr);
//        map.put("nonce", "1686210870");

        for(String key: body.keySet()){
            map.put(key,body.get(key).toString());
        }

        String sign = serviceUtil.getSign(appSecret, map);
        System.out.println("sign:" + sign);


        Map<String, String> params = new HashMap();
//        params.put("nonce", "1686210870");//时间戳（秒）
        params.put("sign", sign);//签名

//        params.put("logistics_interface",body.toString());
//
        for(String key: body.keySet()){
            params.put(key,body.get(key).toString());
        }



//        System.out.println(params.toString());
//        System.out.println("body" + body.toString());

//        params.put("logistics_interface", logisticsInterfaceStr);//订阅接口入参字段
//        params.put("logistics_interface", body.toString());
//        HashMap<String,String> jsonMap = new ObjectMapper().readValue(body,HashMap.class)

//        System.out.println(params.toString());

        Map<String, String> headers = new HashMap<>();
        headers.put("appid", "66d0d24912634800a1c3cd5588b01543");//替换你的app
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");


        String resultData = serviceUtil.httpPostWithForm(url, params, headers);
//        String resultData = serviceUtil.httpPostWithForm(url, body, headers);
        System.out.println("result:" + resultData);

        JSONObject result = new JSONObject(JSON.parseObject(resultData));
//        JSONObject result = JSON.parseObject(resultData);
        return ResponseEntity.ok(result);
    }



}
