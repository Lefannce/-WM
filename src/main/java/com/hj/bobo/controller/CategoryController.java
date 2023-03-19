package com.hj.bobo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.bobo.common.R;
import com.hj.bobo.entity.Category;
import com.hj.bobo.service.CategoryService;
import com.hj.bobo.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
@Autowired
private CategoryService categoryService;

@PostMapping
public R<String> save(@RequestBody Category category){
    categoryService.save(category);
    return R.success("添加成功");

}
    /**
     * 分页查询
     */

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> del(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功");
    }

     @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("更新成功");
     }


    /**
     * 回显菜品类型
     * @param category
     * @return
     */
     @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
         LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //构造条件
         queryWrapper.eq(category.getType() !=null,Category::getType,category.getType());
         //添加排序条件
         queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
         //
         List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

     }
}
