package com.example.service;

import com.example.common.BusinessException;
import com.example.dto.InquiryRequest;
import com.example.entity.Inquiry;
import com.example.entity.Product;
import com.example.mapper.InquiryMapper;
import com.example.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 询价单业务
 */
@Service
public class InquiryService {

    /** 中国大陆手机号简单校验 */
    private static final Pattern PHONE = Pattern.compile("^1[3-9]\\d{9}$");

    private final InquiryMapper inquiryMapper;
    private final ProductMapper productMapper;

    @Autowired
    public InquiryService(InquiryMapper inquiryMapper, ProductMapper productMapper) {
        this.inquiryMapper = inquiryMapper;
        this.productMapper = productMapper;
    }

    /** 提交询价单：校验入参 + 冗余产品名 */
    public Inquiry submit(InquiryRequest req) {
        if (req == null) {
            throw new BusinessException("请求参数为空");
        }
        if (!StringUtils.hasText(req.getContactName())) {
            throw new BusinessException("联系人姓名不能为空");
        }
        if (!StringUtils.hasText(req.getContactPhone()) || !PHONE.matcher(req.getContactPhone()).matches()) {
            throw new BusinessException("联系电话格式不正确");
        }
        if (req.getProductIds() == null || req.getProductIds().isEmpty()) {
            throw new BusinessException("请至少选择一个询价产品");
        }

        // 把产品名称冗余进表，方便后台列表展示（避免每次再 join）
        List<String> names = new ArrayList<>();
        for (String pid : req.getProductIds()) {
            Product p = productMapper.findById(pid);
            if (p != null) {
                names.add(p.getName());
            }
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setProductIds(new ArrayList<>(req.getProductIds()));
        inquiry.setProductNames(String.join(",", names));
        inquiry.setContactName(req.getContactName());
        inquiry.setContactPhone(req.getContactPhone());
        inquiry.setProjectName(req.getProjectName());
        inquiry.setCity(req.getCity());
        inquiry.setRequirement(req.getRequirement());
        inquiry.setRemark(req.getRemark());
        inquiry.setStatus("PENDING");

        inquiryMapper.insert(inquiry);
        return inquiryMapper.findById(inquiry.getId());
    }

    /** 查询询价单列表（管理后台用） */
    public List<Inquiry> list(String status) {
        return inquiryMapper.findList(status);
    }

    /** 更新询价单状态 */
    public void updateStatus(Long id, String status) {
        if (!List.of("PENDING", "CONTACTED", "CLOSED").contains(status)) {
            throw new BusinessException("非法状态：" + status);
        }
        Inquiry exist = inquiryMapper.findById(id);
        if (exist == null) {
            throw new BusinessException(404, "询价单不存在");
        }
        inquiryMapper.updateStatus(id, status);
    }
}
