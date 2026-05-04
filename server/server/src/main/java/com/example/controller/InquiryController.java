package com.example.controller;

import com.example.common.Result;
import com.example.dto.InquiryRequest;
import com.example.entity.Inquiry;
import com.example.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 询价单接口
 * <ul>
 *   <li>POST /api/inquiry — 公开，小程序提交</li>
 *   <li>GET  /api/admin/inquiries — 管理后台列表</li>
 *   <li>PUT  /api/admin/inquiries/{id}/status — 管理后台改状态</li>
 * </ul>
 */
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    /** 公开：提交询价单 */
    @PostMapping("/api/inquiry")
    public Result<Inquiry> submit(@RequestBody InquiryRequest request) {
        return Result.ok(inquiryService.submit(request));
    }

    /** 管理后台：询价单列表 */
    @GetMapping("/api/admin/inquiries")
    public Result<List<Inquiry>> list(@RequestParam(required = false) String status) {
        return Result.ok(inquiryService.list(status));
    }

    /** 管理后台：更新询价单状态 */
    @PutMapping("/api/admin/inquiries/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        inquiryService.updateStatus(id, body.get("status"));
        return Result.ok();
    }
}
