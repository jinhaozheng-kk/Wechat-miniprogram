package com.example.controller;

import com.example.common.Result;
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品公开接口（小程序与管理后台共用）
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 产品列表
     *
     * @param categoryId 可空，按分类筛选
     * @param keyword    可空，按名称/型号模糊匹配
     */
    @GetMapping
    public Result<List<Product>> list(@RequestParam(required = false) String categoryId,
                                      @RequestParam(required = false) String keyword) {
        return Result.ok(productService.list(categoryId, keyword));
    }

    /** 产品详情 */
    @GetMapping("/{id}")
    public Result<Product> get(@PathVariable String id) {
        return Result.ok(productService.getById(id));
    }
}
