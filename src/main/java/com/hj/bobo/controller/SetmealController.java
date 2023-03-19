package com.hj.bobo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.bobo.common.R;
import com.hj.bobo.dto.SetmealDto;
import com.hj.bobo.entity.Category;
import com.hj.bobo.entity.Setmeal;
import com.hj.bobo.service.CategoryService;
import com.hj.bobo.service.SetmealDishService;
import com.hj.bobo.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息:{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }


    /***
     *             ***难点***
     * 1,先使用分页查询,查询出除套餐分类的其他所以信息.
     * 2,将查询出来的信息拷贝除列表信息以为的其他信息拷贝到dtoPage
     * 3,使用流遍历出把列表信息拷贝到新的dto对象中
     * 4,通过page查询出来的id信息,然后再查询category表 找到分类名称,赋值给dto对象
     * 5,将dto对象收集起来,赋值给list集合,然后返回
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        //分页构造对象
        Page<Setmeal> pageInfo = new Page<>();
        //因为直接查询没有分类名称属性,分类名称属性在dto里面,创建一个新的page拷贝过来
        Page<SetmealDto> dtoPage = new Page<>();

         //分页查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Setmeal::getName,name);//name不为空就模糊查询name
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);//按更新时间排序
        setmealService.page(pageInfo,queryWrapper);//查询


        //拷贝除了records以外的其他数据,如,分页数量,页面大小等
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        //获取查询出来的列表信息,
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();//创建一个dto对象
            //把records的数据拷贝到dto对象
            BeanUtils.copyProperties(item,setmealDto);
            //获取分类id,等下通过分类id查询出分类信息
            Long categoryId = item.getCategoryId();
            //通过id查询出需要的分类对象
            Category category = categoryService.getById(categoryId);

            //如果查询出来的对象不为空,就把数据赋值给dto的name
            if(category != null){
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);

    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
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
        Setmeal setmeal = new Setmeal();
        setmeal.setId(ids);
        setmeal.setStatus(st);
        setmealService.updateById(setmeal);
        return R.success("修改成功");
    }
 /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
