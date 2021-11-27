package com.cmpe275.vms;

import com.cmpe275.vms.filter.JwtFilter;
import com.cmpe275.vms.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VmsApplication {

	@Bean
	public JwtFilter getJwtFilter() {
		return new JwtFilter();
	}

	@Bean
	public JwtUtil getJwtUtil() {
		return new JwtUtil();
	}



	public static void main(String[] args) {
		SpringApplication.run(VmsApplication.class, args);
	}

}
