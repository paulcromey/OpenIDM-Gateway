package com.cromey.gateway;

import java.net.URI;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author paulcromey
 *
 */
@RestController
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${remote.home}")
	private URI home;

	@GetMapping(path = "/user")
	public @ResponseBody ResponseEntity<Object> proxy(ProxyExchange<Object> proxy) throws Exception {
		
		logger.info("Call user endpoint");
		
		return proxy.uri(home + "/openidm/managed/user/?_queryId=query-all-ids")
				.header("X-OpenIDM-Username", "openidm-admin").header("X-OpenIDM-Password", "openidm-admin")
				.get(response());
	}

	private Function<ResponseEntity<Object>, ResponseEntity<Object>> response() {

		logger.info("Return user response");
		
		return response -> ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders())
				.body(response.getBody());

	}
}
