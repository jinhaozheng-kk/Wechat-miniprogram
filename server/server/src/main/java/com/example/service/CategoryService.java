package com.example.service;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品分类业务
 */
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /** 查询所有分类 */
    public List<Category> listAll() {
        return categoryMapper.findAll();
    }

    /** 按 id 查询单个分类 */
    public Category getById(String id) {
        return categoryMapper.findById(id);
    }
}
