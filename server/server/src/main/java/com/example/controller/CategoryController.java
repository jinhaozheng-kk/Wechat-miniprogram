package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品分类公开接口（小程序与管理后台共用）
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** 查询所有分类 */
    @GetMapping
    public Result<List<Category>> list() {
        return Result.ok(categoryService.listAll());
    }

    /** 按 id 查询单个分类 */
    @GetMapping("/{id}")
    public Result<Category> get(@PathVariable String id) {
        return Result.ok(categoryService.getById(id));
    }
}
