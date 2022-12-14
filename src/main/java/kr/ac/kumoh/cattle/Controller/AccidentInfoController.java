package kr.ac.kumoh.cattle.Controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

@RestController
@RequestMapping("/accident")
@CrossOrigin(origins = "http://localhost:5000")
public class AccidentInfoController {
    private String apiKey="059980d47126472a865a1fced65d1948";
    private String url="https://openapi.its.go.kr:9443/eventInfo";
    @GetMapping
    public JSONArray getAccident() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("apiKey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8")); /*공개키*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("all", "UTF-8")); /*도로유형*/
        urlBuilder.append("&" + URLEncoder.encode("eventType","UTF-8") + "=" + URLEncoder.encode("acc", "UTF-8")); /*이벤트유형*/
        urlBuilder.append("&" + URLEncoder.encode("minX","UTF-8") + "=" + URLEncoder.encode("124.363600", "UTF-8")); /*최소경도영역*/
        urlBuilder.append("&" + URLEncoder.encode("maxX","UTF-8") + "=" + URLEncoder.encode("130.919800", "UTF-8")); /*최대경도영역*/
        urlBuilder.append("&" + URLEncoder.encode("minY","UTF-8") + "=" + URLEncoder.encode("33.643000", "UTF-8")); /*최소위도영역*/
        urlBuilder.append("&" + URLEncoder.encode("maxY","UTF-8") + "=" + URLEncoder.encode("38.364000", "UTF-8")); /*최대위도영역*/
        urlBuilder.append("&" + URLEncoder.encode("getType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*출력타입*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "text/json;charset=UTF-8");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        JSONObject responseJson = new JSONObject(sb.toString());
        JSONArray items = responseJson.getJSONObject("body").getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            items.getJSONObject(i).remove("endDate");
        }
        System.out.println(items);
        rd.close();
        conn.disconnect();
        return items;
    }
}