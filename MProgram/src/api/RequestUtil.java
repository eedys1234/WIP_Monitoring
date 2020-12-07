package api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import encoding.EncoderUtil;
import encoding.StringUtil;
import exception.InvalidException;

public class RequestUtil {
	
	private RequestUtil() {
		new AssertionError();
	}
	
	private static HttpURLConnection connect(String endpoint) throws Exception { 
		
		HttpURLConnection conn = null;
		
		URL url = new URL(XNet.HOST_URL + endpoint);
		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");			
		
		return conn;
		
	}
	
	private static JSONObject validResponseCode(HttpURLConnection conn) throws ParseException, IOException {

		int responseCode = conn.getResponseCode();
		if (responseCode == 400) {
			throw new InvalidException();
        } 
		else if (responseCode == 500) {
			throw new InvalidException();
        } 
		else { // 성공 후 응답 JSON 데이터받기
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        StringBuilder sb = new StringBuilder();
	        String line = "";
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
	        JSONParser parser = new JSONParser();
	        return (JSONObject) parser.parse(EncoderUtil.urlDecoding(sb.toString()).replace(" ", "+"));
		}
	}
		
	public static JSONObject getRequest(String endpoint, JSONObject request) {

		try {
			
			if(request.size() > 0) 
				endpoint = endpoint + "?" + getQueryString(request);
			
			HttpURLConnection conn = connect(endpoint);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			
			return validResponseCode(conn);
		} 
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;		
	}
	
	public static JSONObject postRequest(String endpoint, JSONObject request) {

		try {
			
			HttpURLConnection conn = connect(endpoint);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(request.toJSONString());
			bw.flush();
			bw.close();
			
			return validResponseCode(conn);
		} 
		catch(Exception e) {
			
		}
		return null;		

	}
	
	public static JSONObject putRequest(String endpoint, JSONObject request) {
		
		try {
			
			HttpURLConnection conn = connect(endpoint);
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(request.toJSONString());
			bw.flush();
			bw.close();
			
			return validResponseCode(conn);
		} 
		catch(Exception e) {
			
		}
		return null;		
	}
	
	public static JSONObject deleteRequest(String endpoint, JSONObject request) {

		try {
			if(request.size() > 0)
				endpoint = endpoint + "?" + getQueryString(request);

			HttpURLConnection conn = connect(endpoint);
			conn.setRequestMethod("DELETE");
			conn.setDoInput(true);
			
			return validResponseCode(conn);
		} 
		catch(Exception e) {
			
		}
		return null;		
		
	}
	
	private static String getQueryString(Object request) {

		if(request instanceof JSONObject) {		
			Set<Entry<Object, Object>> entrys = ((JSONObject)request).entrySet();
			return entrys.stream().map(entry->getKeyValuePair(entry, "=")).collect(Collectors.joining("&"));
			 
		}
		return null;
	}
	
	private static String getKeyValuePair(Entry<Object, Object> entry, String seperator) {
		String key = StringUtil.fixNull(entry.getKey());
		String value = StringUtil.fixNull(entry.getValue());
		
		if(!"".equals(key) && !"".equals(value)) {
			return key + seperator + value;
		}
		return "";
	}
}
