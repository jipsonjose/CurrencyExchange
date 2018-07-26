package com.invpivot.microservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
public class LimitsConfigurationController {
    
	@Autowired
	private Configuration configuration;
	
	@GetMapping("/limits")
	public LimitsConfiguration retriveLimitsFromConfigurations() {
    	return new LimitsConfiguration(configuration.getMaximum(), configuration.getMinimum());
    }
	
	@GetMapping("/fault-tolerance-example")
	@HystrixCommand(fallbackMethod="fallbackRetriveConfiguration")
	public LimitsConfiguration retriveLimitsFromConfiguration() {
    	throw new RuntimeException("Not Available");
    }
	public LimitsConfiguration fallbackRetriveConfiguration() {
		return new LimitsConfiguration(999999,1);
    }
}
