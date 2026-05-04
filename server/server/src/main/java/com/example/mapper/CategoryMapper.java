package com.example.mapper;

import com.example.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品分类 Mapper
 */
public interface CategoryMapper {

    /** 查询所有分类（按 sortOrder 升序） */
    List<Category> findAll();

    /** 按 id 查询单个分类 */
    Category findById(@Param("id") String id);

    /** 总数 */
    long count();
}
