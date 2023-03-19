package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.entity.Employee;
import com.hj.bobo.mapper.EmployeeMapper;
import com.hj.bobo.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
}
