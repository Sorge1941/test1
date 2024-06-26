package com.itluchao.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itluchao.reggie.mapper.EmployeeMapper;

import jakarta.annotation.Resource;

@SpringBootTest
class ReggieApplicationTests {
@Autowired
EmployeeMapper employeeMapper;
	@Test
	public void contextLoads() {
		employeeMapper.selectList(null).forEach(System.out::println);
	}

}
