package com.invpivot.microservices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
//	        CurrencyExchangeServiceProxy
	        
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,@PathVariable BigDecimal quantity) {
		
		//Feign - Problem 1
		
		//Git hub commit;;;;
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,uriVariables);
		
		CurrencyConversionBean response=responseEntity.getBody();
		//return currencyConversionBean;
		//CurrencyConversionBean currencyConversionBean=new CurrencyConversionBean(1L,from,to,BigDecimal.ONE,quantity,quantity,0);
		//return currencyConversionBean;
		
		CurrencyConversionBean currencyConversionBean=new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
		logger.info("{}",response);
		return currencyConversionBean;
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,@PathVariable BigDecimal quantity) {
		
		//Feign - Problem 1
		CurrencyConversionBean response=proxy.retriveExchangeValue(from, to);
		CurrencyConversionBean currencyConversionBean=new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
		return currencyConversionBean;
		
		
	}
}
