package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetMealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO);
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
/**
     * 批量删除套餐
     * @param ids
     */
    public void deleteBatch(List<Long> ids);
/**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id);
    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    public SetmealDTO getById(Long id);
    /**
     * 修改套餐
     * @param setmealDTO
     */
    public void update(SetmealDTO setmealDTO);

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
