package com.itluchao.reggie;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
@EnableCaching//开启注解
@ServletComponentScan
@SpringBootApplication
public class ReggieApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ReggieApplication.class, args);
	}

}
