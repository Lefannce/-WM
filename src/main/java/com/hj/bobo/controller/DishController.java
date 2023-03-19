package com.hj.bobo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.bobo.common.R;
import com.hj.bobo.dto.DishDto;
import com.hj.bobo.entity.Category;
import com.hj.bobo.entity.Dish;
import com.hj.bobo.entity.DishFlavor;
import com.hj.bobo.service.CategoryService;
import com.hj.bobo.service.DishFlavorService;
import com.hj.bobo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
public R<String> save(@RequestBody DishDto dishDto){
    log.info(dishDto.toString());
    dishService.saveWithFlavor(dishDto);
    return R.success("新增成功");

}

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
         Page<DishDto> dishDtoPage = new Page<>();
        //过滤条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //name不等于null就模糊查询name
        queryWrapper.like(name != null,Dish::getName,name);
        //排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);


          BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        //通过流把每一个数据查询出来并手收集给dishDto
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

/**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

/**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> del( Long ids){
        log.info(ids.toString());
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId,ids);
        dishService.remove(queryWrapper);
        return R.success("删除成功");


    }

    /**
     * 状态停售和起售
     * @param st 状态
     * @param ids id
     * @return
     */
    @PostMapping("/status/{st}")
    public R<String> stu(@PathVariable int st,Long ids){
        log.info("ids是{},{}",st,ids);
        Dish dish = new Dish();
        dish.setId(ids);
        dish.setStatus(st);
        dishService.updateById(dish);
        return R.success("修改成功");
    }
    //套餐管理页面添加彩票界面的数据
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        //停售的不查询
//        queryWrapper.eq(Dish::getStatus,1);
//
//        //排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }


     //套餐管理页面添加彩票界面的数据
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //停售的不查询
        queryWrapper.eq(Dish::getStatus,1);

        //排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        //查询口味,给客户端页面使用
         List<DishDto> DishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //把数据复制到dto
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //获取当前菜品id
             Long id = item.getId();
            //构造条件根据id查询口味
             LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
             dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId,id);//根据id等值查询
             List<DishFlavor> dishFlavorList = dishFlavorService.list(dishDtoLambdaQueryWrapper);
             dishDto.setFlavors(dishFlavorList);//把查询出来的值赋给dto的flavors
             return dishDto;
        }).collect(Collectors.toList());

        return R.success(DishDtoList);

    }


}
