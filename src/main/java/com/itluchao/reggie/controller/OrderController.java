package com.itluchao.reggie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itluchao.reggie.entity.Orders;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.service.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/submit")
    public R<String> pay(@RequestBody Orders orders){
        orderService.pay(orders);
        return R.success("");
    }

}
