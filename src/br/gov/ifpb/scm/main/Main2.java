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

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.model.to.ListPlanningIsCurrentDateResponseData;
import br.gov.ifpb.scm.model.to.ListProductionLineProducingResponseData;
import br.gov.ifpb.scm.model.to.ResponseData;
import br.gov.ifpb.scm.requests.GenericRequest;
import br.gov.ifpb.scm.requests.OrderProductRequest;
import br.gov.ifpb.scm.util.SequentialIncrement;

public class Main2 {

	public static void main(String[] args) throws Exception {
		
//		ListPlanningIsCurrentDateResponseData listOpsInSector = 
//				OrderProductRequest.listPlanningOpInCurrentDateGet("");
//
//		if(listOpsInSector != null && listOpsInSector.getIdOp() != null) {
//			System.out.println("OP: " + listOpsInSector.getIdOp());
//		}
//		
		
//
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("userId", 4);
		params.put("sectorId", 4);
		
		ListProductionLineProducingResponseData rd = OrderProductRequest.listProductionLineProducingGet("", params);
		
		System.out.println("SEQUENTIAL: " + rd.getSequential());
		System.out.println("MESSAGE: " + rd.getMessage());
		
		
		//System.out.println(get("production-lines/producing?userId=1&sectorId=1", "", null));
		
		
		//sendGet("http://localhost:3004/api/v1/production-lines/producing?userId=1&sectorId=1");
	}

	public static Map<String, Object> get(String route, String token, Map<String, Object> requestParams) {
		HttpURLConnection connection = null;

		try {
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

			StringBuilder result = new StringBuilder();

			if(requestParams != null) {
				for (Map.Entry<String,Object> param : requestParams.entrySet()) {
					connection.addRequestProperty(param.getKey(), param.getValue().toString());
					result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
					result.append("=");
					result.append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
					result.append("&");
				}

				//Send request
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(result.toString());
				wr.close();
			}

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

//	// HTTP GET request
//	private static void sendGet(String url) throws Exception {
//
//		URL obj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//		// optional default is GET
//		con.setRequestMethod("GET");
//
//		//add request header
//
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader in = new BufferedReader(
//				new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		//print result
//		System.out.println(response.toString());
//
//	}
}
