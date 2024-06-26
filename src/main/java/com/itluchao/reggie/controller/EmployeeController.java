package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itluchao.reggie.entity.Employee;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    // 登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        Employee e = employeeService.login(employee.getUsername());

        // 对传递回来的数据进行判断
        // 如果为空，则错误
        if (e == null)
            return R.error("用户名或密码错误");

        // 判断密码能不能对上
        // 对传递上来的进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 判断密码是否正确
        if (!password.equals(e.getPassword()))
            return R.error("用户名或密码错误");

        // 判断状态是否正确
        if (e.getStatus() == 0)
            return R.error("用户名或密码错误");
        


        // 将结果存入session
        request.getSession().setAttribute("employee", e.getId());
        return R.success(e);
    }

    // 退出
    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {

        // 清除session
        request.getSession().removeAttribute("employee");
        return R.success(null);
    }


    //添加员工
    @PostMapping("")
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee)
    {
        //设置默认密码,并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // //从session里拿到操作人数据
        // Long EmpId=(Long) request.getSession().getAttribute("employee");
        // //添加操作人信息
        // employee.setUpdateUser(EmpId);
        // employee.setCreateUser(EmpId);
        employeeService.addEmployee(employee);
        return R.success(null);
    }

    //查询员工
    @GetMapping("/page")
    public R<Page> getEmployee(HttpServletRequest request,
    @RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer pageSize,
    @RequestParam(defaultValue = "") String name){
       Page<Employee> list= employeeService.getEmployee(page,pageSize,name);
       
        return R.success(list);
    }
    

    //禁用员工
    @PutMapping("")
    public R stop(HttpServletRequest request,@RequestBody Employee employee){
        //修改人ID
      /*   Long EmpId=(long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(EmpId); */
        employeeService.stop(employee);
        return R.success(null);
    }


    //编辑员工
    @GetMapping("/{id}")
    public R<Employee> add(@PathVariable long id){
        Employee employee=employeeService.getById(id);
        return R.success(employee);
    }
    
}
