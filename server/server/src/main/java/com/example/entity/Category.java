package com.example.entity;

import java.time.LocalDateTime;

/**
 * 产品分类实体
 */
public class Category {

    /** 分类编码（小程序中使用的拼音 id，如 guang-xian） */
    private String id;
    /** 分类名称 */
    private String name;
    /** 图标文字（单字） */
    private String icon;
    /** 主色调 */
    private String color;
    /** 背景色 */
    private String bgColor;
    /** 封面图路径 */
    private String image;
    /** 分类简介 */
    private String description;
    /** 产品数（冗余字段） */
    private Integer productCount;
    /** 排序值，越小越靠前 */
    private Integer sortOrder;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
