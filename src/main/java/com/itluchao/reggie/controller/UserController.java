package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.entity.ShoppingCart;
import com.itluchao.reggie.entity.User;
import com.itluchao.reggie.service.ShoppingCartService;
import com.itluchao.reggie.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ShoppingCartService shoppingCartService;
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request,@RequestBody User phone){
        QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
        //判断user表里有没有，没有就添加
        String phones=phone.getPhone();
        queryWrapper.eq("phone",phones);
        User user= userService.getOne(queryWrapper);
        if(user==null){
            user=new User();
            user.setPhone(phones);
            user.setStatus(1);
            userService.save(user);
            //存入session
        }
        request.getSession().setAttribute("user",user.getId());
        return R.success("登陆成功");
        

      
    }
      @PostMapping("/sendMsg")
        public R<String> send(@RequestBody User user)
        {
            return R.success("");
        }
    


}
