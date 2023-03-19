package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.entity.ShoppingCart;
import com.hj.bobo.mapper.ShoppingCartMapper;
import com.hj.bobo.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
