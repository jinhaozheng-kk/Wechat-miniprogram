package com.example.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 询价单实体
 */
public class Inquiry {

    /** 主键 */
    private Long id;
    /** 询价产品 id 列表（DB JSON） */
    private List<Object> productIds;
    /** 产品名称（冗余字段，逗号分隔） */
    private String productNames;
    /** 联系人姓名 */
    private String contactName;
    /** 联系电话 */
    private String contactPhone;
    /** 工程名称 */
    private String projectName;
    /** 所在城市 */
    private String city;
    /** 需求描述 */
    private String requirement;
    /** 备注 */
    private String remark;
    /** 状态：PENDING / CONTACTED / CLOSED */
    private String status;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Object> getProductIds() { return productIds; }
    public void setProductIds(List<Object> productIds) { this.productIds = productIds; }
    public String getProductNames() { return productNames; }
    public void setProductNames(String productNames) { this.productNames = productNames; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
