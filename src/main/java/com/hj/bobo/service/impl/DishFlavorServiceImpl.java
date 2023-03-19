package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.entity.DishFlavor;
import com.hj.bobo.mapper.DishFlavorMapper;
import com.hj.bobo.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
