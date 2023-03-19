package com.hj.bobo.dto;

import com.hj.bobo.entity.Setmeal;
import com.hj.bobo.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
