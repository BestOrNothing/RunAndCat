package com.example.lucas.running_cat1;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by yanzhensong on 4/26/16.
 */
public class MyHttp {
    public static HttpResponse sendPost(String url, List<NameValuePair> params) {
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            return  httpClient.execute(httpPost);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
