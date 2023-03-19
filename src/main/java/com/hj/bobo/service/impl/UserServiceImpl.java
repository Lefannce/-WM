package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.entity.User;
import com.hj.bobo.mapper.UserMapper;
import com.hj.bobo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
