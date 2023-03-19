package com.hj.bobo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.bobo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
