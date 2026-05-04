package com.example.mapper;

import com.example.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品 Mapper
 */
public interface ProductMapper {

    /**
     * 按条件查询产品列表
     *
     * @param categoryId 可空，按分类筛选
     * @param keyword    可空，按名称/型号模糊匹配
     */
    List<Product> findList(@Param("categoryId") String categoryId,
                           @Param("keyword") String keyword);

    /** 按 id 查询单个产品 */
    Product findById(@Param("id") String id);

    /** 总数 */
    long count();

    /**
     * 按分类聚合：每个分类的产品数
     *
     * @return [{categoryId, count}]
     */
    List<Map<String, Object>> countGroupByCategory();
}
