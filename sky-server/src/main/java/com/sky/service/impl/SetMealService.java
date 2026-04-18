
package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        // 获取生成的套餐id
        Long setmealId = setmeal.getId();

        // 设置套餐id到套餐菜品关系中
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
            }
            // 批量插入套餐菜品关系数据
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询：{}", setmealPageQueryDTO);
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        //判断是否选择了套餐
        if (ids == null || ids.size() == 0) {
            throw new DeletionNotAllowedException("请选择要删除的套餐");
        }
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal == null) {
                throw new DeletionNotAllowedException("套餐不存在");
            }
            if (setmeal.getStatus() == 1) {
                // 套餐处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteBatch(ids);
        setmealDishMapper.deleteBySetmealId(ids);
    }

    /**
     * 套餐起售停售
     *
     * @param status
     * @param id
     * @return
     */
    public void startOrStop(Integer status, Long id) {
        log.info("套餐起售停售：{}", status, id);
        //判断套餐中是否有停售菜品 有则不能起售套餐
        if (status == 1) {
            List<Long> dishIds = setmealDishMapper.getDishIdsBySetmealId(id);
            if (dishIds != null && dishIds.size() > 0) {
                for (Long dishId : dishIds) {
                    Dish dish = dishMapper.getById(dishId);
                    if (dish != null && dish.getStatus() == 0) {
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
                Setmeal setmeal = Setmeal.builder()
                        .id(id)
                        .status(status)
                        .build();
                setmealMapper.update(setmeal);

    }
/**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    public SetmealDTO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, setmealDTO);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDTO.setSetmealDishes(setmealDishes);
        }
        return setmealDTO;
    }
/**
     * 修改套餐
     *
     * @param setmealDTO
     */
    public void update(SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        Long setmealId = setmealDTO.getId();

        List<Long> ids = new ArrayList<>();
        ids.add(setmealId);
        setmealDishMapper.deleteBySetmealId(ids);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);

        }

    }
}
