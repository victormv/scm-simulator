package br.gov.ifpb.scm.main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main2 {

	public static void main(String[] args) {
		
		Main2 main = new Main2();
		String loginJSON = new Main2().login();
		
		Gson gson = new Gson();
		Map<String, Object> map = 
			gson.fromJson(loginJSON, new TypeToken<Map<String, Object>>(){}.getType());
		
		
		String result = main.startTask(map.get("token").toString());
				
		System.out.println(result);
	}

	public String login() {
		HttpURLConnection connection = null;

		try {
			URL url = new URL("http://localhost:3004/api/v1/login");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Language", "pt-BR");  

			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			Map<String,Object> params = new LinkedHashMap<>();
	        params.put("email", "admin@scm.com");
	        params.put("password", "12345");
	        
	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.write(postDataBytes);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public String startTask(String token) {
		System.out.println(token);
		
		HttpURLConnection connection = null;

		try {
			URL url = new URL("http://localhost:3004/api/v1/pocket/start-task");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Language", "pt-BR");  
			
			connection.addRequestProperty("Authorization", "BEARER " + token);

			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			Map<String,Object> params = new LinkedHashMap<>();
	        params.put("idOp", "1");
	        params.put("idUser", "1");
	        params.put("sequential", "1");
	        
	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.write(postDataBytes);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
