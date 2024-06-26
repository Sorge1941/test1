package com.itluchao.reggie.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.entity.AddressBook;
import com.itluchao.reggie.mapper.AddressBookMapper;
import com.itluchao.reggie.service.AddressBookService;
@Service
public class AddressBookServiceimpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService{

}
