package hibernate.util;

import java.util.Map;

import com.google.gson.internal.StringMap;

import br.gov.ifpb.scm.model.to.ResponseData;

public class ResponseUtil {
	
	@SuppressWarnings("unchecked")
	public static void handleResponse(Map<String, Object> response, ResponseData data) {
		if(response == null || response.isEmpty() || response.get("status") == null) {
			return;
		}
		
		StringMap<Object> sm = (StringMap<Object>) response.get("status");
		
		String sucesso = (String) sm.get("success");
		if(sucesso != null && !sucesso.isEmpty()) {
			data.setSuccess(true);
			data.setMessage(sm.get("success").toString());
		} else {
			data.setMessage(sm.get("error").toString());
		}
	}
}
