package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.entity.OrderDetail;
import com.hj.bobo.mapper.OrderDetailMapper;
import com.hj.bobo.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}