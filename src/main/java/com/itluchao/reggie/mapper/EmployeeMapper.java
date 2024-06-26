package com.itluchao.reggie.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itluchao.reggie.entity.Employee;
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{

}
