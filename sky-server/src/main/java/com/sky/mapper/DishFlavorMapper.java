package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
/**
 * 菜品口味数据访问接口
 */
public interface DishFlavorMapper {
    /**
     * 批量插入菜品口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);
}
