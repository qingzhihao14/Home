package com.my.friends.pay.paymentdemo.service.impl;

import com.my.friends.pay.paymentdemo.entity.Product;
import com.my.friends.pay.paymentdemo.mapper.ProductMapper;
import com.my.friends.pay.paymentdemo.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
