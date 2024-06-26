package com.itluchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itluchao.reggie.entity.Orders;

public interface OrderService extends IService<Orders>{
    //支付
   public void pay(Orders orders);

}
