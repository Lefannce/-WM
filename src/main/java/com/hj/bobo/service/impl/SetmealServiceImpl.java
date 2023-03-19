package com.hj.bobo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.bobo.common.CustomException;
import com.hj.bobo.dto.SetmealDto;
import com.hj.bobo.entity.Setmeal;
import com.hj.bobo.entity.SetmealDish;
import com.hj.bobo.mapper.SetmealMapper;
import com.hj.bobo.service.SetmealDishService;
import com.hj.bobo.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐方法,把信息分别保存到两个表
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto){
        //保存套餐的基本信息,价格,分类...
        this.save(setmealDto);
        //
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();//获取dto中的菜品
        setmealDishes.stream().map((item)->{//遍历设置套餐的菜品id
            item.setSetmealId(setmealDto.getId());
            return item;
                }).collect(Collectors.toList());//吧套餐id收集起来
        //把菜品id等信息保存到setmealdish表
        setmealDishService.saveBatch(setmealDishes);


    }


    public void deleteWithDish(List<Long> ids){
        //查询套餐是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        //查询数量有多少
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在售卖中,无法删除");//抛出一个自定义异常
        }

        //如果可以删除,先删除setmeal表中的数据(套餐表)
        this.removeByIds(ids);

        //然后删除关系表Setmeal_dish表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        //删除菜单表中所有setmealid 等于套餐id的数据(删除跟套餐id绑定的菜品数据)
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }
}
