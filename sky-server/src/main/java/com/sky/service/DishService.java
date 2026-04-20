package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
   /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
/**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
/**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);
/**
     * 根据id查询菜品和对应的口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavors(Long id);
/**
     * 修改菜品
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
/**
     * 获取菜品列表
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
/**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    public interface SetmealService {

        /**
         * 条件查询
         * @param setmeal
         * @return
         */
        List<Setmeal> list(Setmeal setmeal);

        /**
         * 根据id查询菜品选项
         * @param id
         * @return
         */
        List<DishItemVO> getDishItemById(Long id);

    }
}
