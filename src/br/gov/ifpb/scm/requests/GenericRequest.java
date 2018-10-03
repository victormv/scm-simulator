package br.gov.ifpb.scm.requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.gov.ifpb.scm.core.CoreConstants;

public abstract class GenericRequest {

	public static Map<String, Object> post(String route, String token, Map<String, Object> bodyParams) {
		HttpURLConnection connection = null;

		try {
			URL url = new URL(CoreConstants.URL_API + route);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "pt-BR");
			if(token != null) {
				connection.addRequestProperty("Authorization", "BEARER " + token);
			}
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : bodyParams.entrySet()) {
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
			Map<String, Object> map = 
					new Gson().fromJson(response.toString(), new TypeToken<Map<String, Object>>(){}.getType());
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static Map<String, Object> get(String route, String token, Map<String, Object> requestParams) {
		HttpURLConnection connection = null;

		try {
			if(requestParams != null) {
				route += "?";
				for (Map.Entry<String,Object> param : requestParams.entrySet()) {
					route += URLEncoder.encode(param.getKey(), "UTF-8");
					route += "=";
					route += URLEncoder.encode(param.getValue().toString(), "UTF-8");
					route += "&";
				}
			}
			
			URL url = new URL(CoreConstants.URL_API + route);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "pt-BR");
			if(token != null) {
				connection.addRequestProperty("Authorization", "BEARER " + token);
			}
			connection.setUseCaches(false);
			connection.setDoOutput(true);

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
			Map<String, Object> map = 
					new Gson().fromJson(response.toString(), new TypeToken<Map<String, Object>>(){}.getType());
			return map;
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
