package com.itluchao.reggie.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itluchao.reggie.entity.Employee;



public interface EmployeeService extends IService<Employee>{
public Employee login(String name);


//插入员工
public void addEmployee(Employee employee);


//查询员工
public Page<Employee> getEmployee(Integer page,Integer pageSize,String name);

//禁用员工
public void stop(Employee employee);
}
