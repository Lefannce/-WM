package com.hj.bobo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.bobo.dto.SetmealDto;
import com.hj.bobo.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
  /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

  /**
   * 删除套餐
   * @param ids
   */
  public void deleteWithDish(List<Long> ids);
}
