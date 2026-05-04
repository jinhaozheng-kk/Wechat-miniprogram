package com.example.service;

import com.example.common.BusinessException;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品业务
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /** 按条件查询产品 */
    public List<Product> list(String categoryId, String keyword) {
        return productMapper.findList(categoryId, keyword);
    }

    /** 按 id 查询，找不到时抛业务异常 */
    public Product getById(String id) {
        Product p = productMapper.findById(id);
        if (p == null) {
            throw new BusinessException(404, "产品不存在：" + id);
        }
        return p;
    }
}
