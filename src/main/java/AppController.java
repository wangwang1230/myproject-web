import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.app.sdk.AppCenter;
import com.yonyou.app.sdkutils.JsonResponse;

@RestController
@EnableAutoConfiguration
public class AppController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@RequestMapping("/")
	String home() {
		return "bimart欢迎你！";
	}
		
	@PostMapping("/openApp")
	public String openApp(HttpServletRequest req) {
		Map<String, String[]> param = req.getParameterMap();
		Map<String, String> ps = new HashMap<String, String>();
		for (Map.Entry<String, String[]> entry : param.entrySet()) {
			ps.put(entry.getKey(), entry.getValue()[0]);
		}
		logger.info("---------------  start  -------------");
		boolean b = AppCenter.checkSign(req);
		String activeCode = ps.get("activationCode");
		String tenantId = ps.get("tenantId");
		String resCode = ps.get("resCode");
		//检查
		String checkActiveCodeRet = AppCenter.checkActiveCode(activeCode);
		JsonResponse jr = AppCenter.checkRequestParams(req);
		logger.info("open app ....");
		String pushStatus = AppCenter.openResCallBack(activeCode, tenantId, resCode);
		logger.info("pushStatus {}", pushStatus);
		logger.info("params is ", ps);
		logger.error(" check result " + b);
		return "success";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppController.class, args);
	}
}