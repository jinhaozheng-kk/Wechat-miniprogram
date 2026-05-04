package com.example.dto;

import java.util.List;
import java.util.Map;

/**
 * 后台首页统计数据响应
 */
public class StatsResponse {

    /** 总产品数 */
    private long totalProducts;
    /** 总分类数 */
    private long totalCategories;
    /** 总询价数 */
    private long totalInquiries;
    /** 待处理询价数 */
    private long pendingInquiries;

    /** 各分类的产品数（柱状图）：[{categoryId, name, count}] */
    private List<Map<String, Object>> productsByCategory;

    /** 近 N 天询价趋势（折线图）：[{date, count}] */
    private List<Map<String, Object>> inquiryTrend;

    /** 询价状态占比（饼图）：[{status, count}] */
    private List<Map<String, Object>> inquiryByStatus;

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }
    public long getTotalCategories() { return totalCategories; }
    public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }
    public long getTotalInquiries() { return totalInquiries; }
    public void setTotalInquiries(long totalInquiries) { this.totalInquiries = totalInquiries; }
    public long getPendingInquiries() { return pendingInquiries; }
    public void setPendingInquiries(long pendingInquiries) { this.pendingInquiries = pendingInquiries; }
    public List<Map<String, Object>> getProductsByCategory() { return productsByCategory; }
    public void setProductsByCategory(List<Map<String, Object>> productsByCategory) { this.productsByCategory = productsByCategory; }
    public List<Map<String, Object>> getInquiryTrend() { return inquiryTrend; }
    public void setInquiryTrend(List<Map<String, Object>> inquiryTrend) { this.inquiryTrend = inquiryTrend; }
    public List<Map<String, Object>> getInquiryByStatus() { return inquiryByStatus; }
    public void setInquiryByStatus(List<Map<String, Object>> inquiryByStatus) { this.inquiryByStatus = inquiryByStatus; }
}
