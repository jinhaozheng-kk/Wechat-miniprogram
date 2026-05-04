package com.example.service;

import com.example.dto.StatsResponse;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.example.mapper.InquiryMapper;
import com.example.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台首页统计数据业务
 */
@Service
public class StatsService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final InquiryMapper inquiryMapper;

    @Autowired
    public StatsService(CategoryMapper categoryMapper, ProductMapper productMapper, InquiryMapper inquiryMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.inquiryMapper = inquiryMapper;
    }

    /** 汇总首页所需的全部数据 */
    public StatsResponse summary() {
        StatsResponse r = new StatsResponse();
        r.setTotalCategories(categoryMapper.count());
        r.setTotalProducts(productMapper.count());
        r.setTotalInquiries(inquiryMapper.count());

        // 待处理询价数
        List<Map<String, Object>> byStatus = inquiryMapper.countGroupByStatus();
        r.setInquiryByStatus(byStatus);
        r.setPendingInquiries(byStatus.stream()
                .filter(m -> "PENDING".equals(m.get("status")))
                .findFirst()
                .map(m -> ((Number) m.get("count")).longValue())
                .orElse(0L));

        // 各分类产品数：用分类列表的顺序与名称包装一下，方便前端柱状图直接用
        Map<String, Long> countMap = new HashMap<>();
        for (Map<String, Object> row : productMapper.countGroupByCategory()) {
            countMap.put((String) row.get("categoryId"), ((Number) row.get("count")).longValue());
        }
        List<Map<String, Object>> productsByCategory = new ArrayList<>();
        for (Category c : categoryMapper.findAll()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("categoryId", c.getId());
            item.put("name", c.getName());
            item.put("count", countMap.getOrDefault(c.getId(), 0L));
            productsByCategory.add(item);
        }
        r.setProductsByCategory(productsByCategory);

        // 近 7 天询价趋势：把缺失日期补 0，确保折线图连贯
        Map<String, Long> trendMap = new HashMap<>();
        for (Map<String, Object> row : inquiryMapper.countByDay(7)) {
            trendMap.put(String.valueOf(row.get("date")), ((Number) row.get("count")).longValue());
        }
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            String date = today.minusDays(i).format(FMT);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", date);
            item.put("count", trendMap.getOrDefault(date, 0L));
            trend.add(item);
        }
        r.setInquiryTrend(trend);

        return r;
    }
}
