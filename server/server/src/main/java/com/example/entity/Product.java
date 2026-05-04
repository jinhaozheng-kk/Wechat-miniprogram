package com.example.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品实体
 */
public class Product {

    /** 产品编码 */
    private String id;
    /** 所属分类编码 */
    private String categoryId;
    /** 产品名称 */
    private String name;
    /** 产品型号 */
    private String model;
    /** 图片路径数组（DB JSON） */
    private List<Object> images;
    /** 规格参数列表（DB JSON），元素形如 {key, value} */
    private List<Object> specs;
    /** 标签数组（DB JSON） */
    private List<Object> tags;
    /** 产品描述 */
    private String description;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public List<Object> getImages() { return images; }
    public void setImages(List<Object> images) { this.images = images; }
    public List<Object> getSpecs() { return specs; }
    public void setSpecs(List<Object> specs) { this.specs = specs; }
    public List<Object> getTags() { return tags; }
    public void setTags(List<Object> tags) { this.tags = tags; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
