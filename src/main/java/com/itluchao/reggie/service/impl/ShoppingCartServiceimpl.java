package com.itluchao.reggie.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.ShoppingCart;
import com.itluchao.reggie.mapper.ShoppingCartMapper;
import com.itluchao.reggie.service.ShoppingCartService;

@Service
public class ShoppingCartServiceimpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService{

}
