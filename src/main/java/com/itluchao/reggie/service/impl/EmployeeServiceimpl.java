package com.itluchao.reggie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.Employee;
import com.itluchao.reggie.mapper.EmployeeMapper;
import com.itluchao.reggie.service.EmployeeService;


@Service
public class EmployeeServiceimpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
@Autowired
EmployeeMapper employeeMapper;

@Override
public Employee login(String username){
    QueryWrapper<Employee> queryWrapper=new QueryWrapper<Employee>();
    queryWrapper.eq("username",username);
    return employeeMapper.selectOne(queryWrapper);
}


//添加员工
@Override
public void addEmployee(Employee employee) {
    employeeMapper.insert(employee);
}


@Override
public Page<Employee> getEmployee(Integer page,Integer pageSize,String name) {
    //先查姓名
    QueryWrapper<Employee> queryWrapper=new QueryWrapper<Employee>();

    //模糊查询
    //StringUtils.isNotEmpty(name)表示name不为空
    if (StringUtils.isNotEmpty(name)) {
        queryWrapper.like("name", name);
    }
    //添加排序条件
    queryWrapper.orderByDesc("update_time");
   // queryWrapper.eq("name",name);
    //分页
    Page<Employee> page1=new Page<>(page,pageSize);
    Page<Employee> list= employeeMapper.selectPage(page1,queryWrapper);
    return list;
}

//启用，禁用员工，编辑员工
@Override
public void stop(Employee employee) {
    QueryWrapper<Employee> queryWrapper =new QueryWrapper<Employee>();
    //根据id更新
    queryWrapper.eq("id",employee.getId());
    
    //更新数据库
    employeeMapper.update(employee,queryWrapper);
    return;
}



}
