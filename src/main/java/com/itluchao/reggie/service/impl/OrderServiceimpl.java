package com.itluchao.reggie.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.AddressBook;
import com.itluchao.reggie.entity.OrderDetail;
import com.itluchao.reggie.entity.Orders;
import com.itluchao.reggie.entity.ShoppingCart;
import com.itluchao.reggie.entity.User;
import com.itluchao.reggie.exception.BaseContext;
import com.itluchao.reggie.exception.CustomException;
import com.itluchao.reggie.mapper.OrderMapper;
import com.itluchao.reggie.service.AddressBookService;
import com.itluchao.reggie.service.OrderDetailService;
import com.itluchao.reggie.service.OrderService;
import com.itluchao.reggie.service.ShoppingCartService;
import com.itluchao.reggie.service.UserService;
@Service
public class OrderServiceimpl extends ServiceImpl<OrderMapper,Orders> implements OrderService{
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;

    @Autowired
    OrderDetailService orderDetailService;
    //支付
    @Override
    public void pay(Orders orders) {
       //获取ID
        Long userId=BaseContext.getCurrentId();

        //查询购物车数据
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<ShoppingCart>();
        queryWrapper.eq("user_id",userId);
        List<ShoppingCart> shoppingCarts=shoppingCartService.list(queryWrapper);

        //判断购物车是不是空的
        if(shoppingCarts==null){
            throw new CustomException("购物车为空");
        }
        

             //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }
             long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        
        this.save(orders); 

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(queryWrapper); 
    }

}
