package com.itluchao.reggie.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.User;
import com.itluchao.reggie.mapper.UserMapper;
import com.itluchao.reggie.service.UserService;
@Service
public class UserServiceimpl extends ServiceImpl<UserMapper,User> implements UserService{

}
