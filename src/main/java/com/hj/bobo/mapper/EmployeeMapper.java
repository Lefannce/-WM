package com.hj.bobo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.bobo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
