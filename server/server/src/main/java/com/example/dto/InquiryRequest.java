package com.example.dto;

import java.util.List;

/**
 * 提交询价单请求体（来自小程序端）
 */
public class InquiryRequest {

    /** 询价产品 id 列表 */
    private List<String> productIds;
    /** 联系人 */
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

    public List<String> getProductIds() { return productIds; }
    public void setProductIds(List<String> productIds) { this.productIds = productIds; }
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
}
