package com.itluchao.reggie.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.OrderDetail;
import com.itluchao.reggie.mapper.OrderDetailMapper;
import com.itluchao.reggie.service.OrderDetailService;

@Service
public class OrderDetailServicecimpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService{

}
