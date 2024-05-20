package com.project.shopping.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilsApi {

    public static String get(String requestURL, Map<String, Object> requestParam) {
        String readLine = "";
        StringBuilder builder = new StringBuilder();
        HttpsURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;

        try {
            URL url = new URL(requestURL.concat(convertRequestParam(HttpMethod.GET.name(), requestParam)));
            conn = (HttpsURLConnection) url.openConnection();
            SSLContext context = SSLContext.getInstance("TLS");
            int resCode = conn.getResponseCode();

            context.init(null, null, null);
            // set request method
            conn.setRequestMethod(HttpMethod.GET.name());
            // set request header
            conn.setRequestProperty(requestURL, requestURL);
            // set server connection time
            conn.setConnectTimeout(3000);
            // set server communication time(response time)
            conn.setReadTimeout(2000);
            // set ssl
            conn.setSSLSocketFactory(context.getSocketFactory());

            if (resCode == HttpsURLConnection.HTTP_OK) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }

            br = new BufferedReader(new InputStreamReader(is));

            while ((readLine = br.readLine()) != null) {
                builder.append(readLine);
            }

            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::" + br.readLine());


        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return builder.toString();
    }

    public static String post() {
        return null;
    }

    private static String convertRequestParam(String method, Map<String, Object> requestMap) {
        String res = "";

        if (method == HttpMethod.GET.name()) {
            StringBuilder builder = new StringBuilder("?");
            List<String> keyList = new ArrayList<>(requestMap.keySet());

            for (int i = 0, size = keyList.size(); i < size; i++) {
                String key = keyList.get(i);
                builder.append(key.concat(String.valueOf(requestMap.get(key))));
            }

            res = builder.toString();
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                res = mapper.writeValueAsString(requestMap);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return res;
    }
}
